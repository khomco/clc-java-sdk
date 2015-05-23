/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.common.management.client.QueueClient;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.OperationFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.JobInfo;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.ResourceJobInfo;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.JobFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.NoWaitingJobFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.ParallelJobsFuture;
import com.centurylink.cloud.sdk.common.management.services.domain.queue.job.future.SequentialJobsFuture;
import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.services.QueryService;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.ip.PublicIpMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.*;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.ModifyPublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.PublicIpConverter;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.remote.SshClient;
import com.centurylink.cloud.sdk.servers.services.domain.remote.SshjClient;
import com.centurylink.cloud.sdk.servers.services.domain.server.CreateServerConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.ModifyServerConfig;
import com.centurylink.cloud.sdk.servers.services.domain.server.ServerConverter;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.future.CreateServerJobFuture;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.Server;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerByIdRef;
import com.google.inject.Inject;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.centurylink.cloud.sdk.core.function.Predicates.alwaysTrue;
import static com.centurylink.cloud.sdk.core.function.Predicates.combine;
import static com.centurylink.cloud.sdk.core.function.Predicates.in;
import static com.centurylink.cloud.sdk.core.function.Predicates.isAlwaysTruePredicate;
import static com.centurylink.cloud.sdk.core.function.Predicates.notNull;
import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static com.centurylink.cloud.sdk.core.services.filter.Filters.nullable;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toList;

/**
 * @author ilya.drabenia
 */
public class ServerService implements QueryService<Server, ServerFilter, ServerMetadata> {
    private final ServerConverter serverConverter;
    private final GroupService groupService;
    private final ServerClient client;
    private final QueueClient queueClient;
    private final PublicIpConverter publicIpConverter;

    @Inject
    public ServerService(ServerConverter serverConverter, ServerClient client, QueueClient queueClient,
                         GroupService groupService, PublicIpConverter publicIpConverter) {
        this.serverConverter = serverConverter;
        this.client = client;
        this.queueClient = queueClient;
        this.groupService = groupService;
        this.publicIpConverter = publicIpConverter;
    }

    public OperationFuture<ServerMetadata> create(CreateServerConfig command) {
        BaseServerResponse response = client
            .create(serverConverter.buildCreateServerRequest(command));

        ServerMetadata serverInfo = client
            .findServerByUuid(response.findServerUuid());

        return new OperationFuture<>(
            serverInfo,
            new SequentialJobsFuture(
                () ->
                    new CreateServerJobFuture(response.findStatusId(), serverInfo.getId(), queueClient, client),

                () ->
                    addPublicIpIfNeeded(command, serverInfo)
            )
        );
    }

    /**
     * Modify existing server
     * @param server server reference
     * @param modifyServerConfig server config
     * @return OperationFuture wrapper for ServerRef
     */
    public OperationFuture<Server> modify(Server server, ModifyServerConfig modifyServerConfig) {
        List<ModifyServerRequest> request = serverConverter.buildModifyServerRequest(modifyServerConfig);

        Link response = client.modify(idByRef(server), request);

        return new OperationFuture<>(
            server,
            response.getId(),
            queueClient
        );
    }

    /**
     * Modify list of servers
     * @param serverList server list
     * @param modifyServerConfig server config
     * @return OperationFuture wrapper for list of Servers
     */
    public OperationFuture<List<Server>> modify(List<Server> serverList, ModifyServerConfig modifyServerConfig) {
        List<JobFuture> futures = serverList.stream()
            .map(
                server ->
                    baseServerResponse(
                        server,
                        client.modify(
                            idByRef(server),
                            serverConverter.buildModifyServerRequest(modifyServerConfig)
                        )
                    )
                    .jobFuture()
            )
            .collect(toList());

        return new OperationFuture<>(
            serverList,
            new ParallelJobsFuture(futures)
        );
    }

    /**
     * Modify servers by filter
     * @param serverFilter server filter
     * @param modifyServerConfig server config
     * @return OperationFuture wrapper for list of Servers
     */
    public OperationFuture<List<Server>> modify(ServerFilter serverFilter, ModifyServerConfig modifyServerConfig) {
        List<Server> serverList = find(serverFilter)
            .stream()
            .map(ServerMetadata::asRefById)
            .collect(toList());

        return modify(serverList, modifyServerConfig);
    }

    private JobFuture addPublicIpIfNeeded(CreateServerConfig command, ServerMetadata serverInfo) {
        if (command.getNetwork().getPublicIpConfig() != null) {
            return
                addPublicIp(
                    serverInfo.asRefById(),
                    command.getNetwork().getPublicIpConfig()
                )
                .jobFuture();
        } else {
            return
                new NoWaitingJobFuture();
        }
    }

    /**
     * Delete existing server
     * @param server server reference
     * @return OperationFuture wrapper for ServerRef
     */
    public OperationFuture<Server> delete(Server server) {
        BaseServerResponse response = client.delete(idByRef(server));

        return new OperationFuture<>(
            server,
            response.findStatusId(),
            queueClient
        );
    }

    /**
     * Delete existing servers
     * @param servers the array of servers to delete
     * @return OperationFuture wrapper for list of ServerRef
     */
    public OperationFuture<List<Server>> delete(Server... servers) {
        List<JobFuture> futures = Arrays.asList(servers).stream()
            .map(serverRef -> delete(serverRef).jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            Arrays.asList(servers),
            new ParallelJobsFuture(futures)
        );
    }

    /**
     * Delete existing servers
     * @param filter server filter object
     * @return OperationFuture wrapper for list of ServerRef
     */
    public OperationFuture<List<Server>> delete(ServerFilter filter) {
        List<Server> serverRefs = find(filter).stream()
            .map(metadata -> metadata.asRefById())
            .collect(toList());
        return delete(serverRefs.toArray(new Server[serverRefs.size()]));
    }

    String idByRef(Server ref) {
        if (ref.is(ServerByIdRef.class)) {
            return ref.as(ServerByIdRef.class).getId();
        } else {
            return findByRef(ref).getId();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<ServerMetadata> findLazy(ServerFilter filter) {
        return filter
            .applyFindLazy(serverFilter -> {
                if (isAlwaysTruePredicate(serverFilter.getPredicate())
                    && isAlwaysTruePredicate(serverFilter.getGroupFilter().getPredicate())
                    && isAlwaysTruePredicate(serverFilter.getGroupFilter().getDataCenterFilter().getPredicate())
                    && serverFilter.getServerIds().size() > 0) {
                    return
                        serverFilter
                            .getServerIds()
                            .stream()
                            .map(nullable(client::findServerById))
                            .filter(notNull());
                } else {
                    return
                        groupService
                            .findLazy(serverFilter.getGroupFilter())
                            .flatMap(group -> {
                                    if (serverFilter.isSearchInSubGroups()) {
                                        return group.getAllServers().stream();
                                    } else {
                                        return group.getServers().stream();
                                    }
                                }
                            )
                            .filter(serverFilter.getPredicate())
                            .filter((serverFilter.getServerIds().size() > 0) ?
                                combine(ServerMetadata::getId, in(serverFilter.getServerIds())) : alwaysTrue()
                            );
                }
            });
    }

    /**
     * Power on a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> powerOn(Server... serverRefs) {
        return powerOperationResponse(
                Arrays.asList(serverRefs),
                "Power On",
                client.powerOn(ids(serverRefs))
        );
    }

    /**
     * Power on a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> powerOn(ServerFilter serverFilter) {
        List<Server> serverList = findServers(serverFilter);

        return powerOperationResponse(
            serverList,
            "Power On",
            client.powerOn(ids(serverList))
        );
    }

    /**
     * Power off a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> powerOff(Server... serverRefs) {
        return powerOperationResponse(
            Arrays.asList(serverRefs),
            "Power Off",
            client.powerOff(ids(serverRefs))
        );
    }

    /**
     * Power off a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> powerOff(ServerFilter serverFilter) {
        List<Server> serverList = findServers(serverFilter);

        return powerOperationResponse(
            serverList,
            "Power Off",
            client.powerOff(ids(serverList))
        );
    }

    /**
     * Start maintenance mode on a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> startMaintenance(Server... serverRefs) {
        return powerOperationResponse(
            Arrays.asList(serverRefs),
            "Start Maintenance",
            client.startMaintenance(ids(serverRefs))
        );
    }

    /**
     * Start maintenance mode on a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> startMaintenance(ServerFilter serverFilter) {
        List<Server> serverList = findServers(serverFilter);

        return powerOperationResponse(
            serverList,
            "Start Maintenance",
            client.startMaintenance(ids(serverList))
        );
    }

    /**
     * Stop maintenance mode on a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> stopMaintenance(Server... serverRefs) {
        return powerOperationResponse(
            Arrays.asList(serverRefs),
            "Stop Maintenance",
            client.stopMaintenance(ids(serverRefs))
        );
    }

    /**
     * Stop maintenance mode on a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> stopMaintenance(ServerFilter serverFilter) {
        List<Server> serverList = findServers(serverFilter);

        return powerOperationResponse(
            serverList,
            "Stop Maintenance",
            client.stopMaintenance(ids(serverList))
        );
    }

    /**
     * Pause a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> pause(Server... serverRefs) {
        return powerOperationResponse(
            Arrays.asList(serverRefs),
            "Pause",
            client.pause(ids(serverRefs))
        );
    }

    /**
     * Pause a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> pause(ServerFilter serverFilter) {
        List<Server> serverList = findServers(serverFilter);

        return powerOperationResponse(
            serverList,
            "Pause",
            client.pause(ids(serverList))
        );
    }

    /**
     * Reboot a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> reboot(Server... serverRefs) {
        return powerOperationResponse(
            Arrays.asList(serverRefs),
            "Reboot",
            client.reboot(ids(serverRefs))
        );
    }

    /**
     * Reboot a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> reboot(ServerFilter serverFilter) {
        List<Server> serverList = findServers(serverFilter);

        return powerOperationResponse(
            serverList,
            "Reboot",
            client.reboot(ids(serverList))
        );
    }

    /**
     * Reset a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> reset(Server... serverRefs) {
        return powerOperationResponse(
            Arrays.asList(serverRefs),
            "Reset",
            client.reset(ids(serverRefs))
        );
    }

    /**
     * Reset a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> reset(ServerFilter serverFilter) {
        List<Server> serverList = findServers(serverFilter);

        return powerOperationResponse(
            serverList,
            "Reset",
            client.reset(ids(serverList))
        );
    }

    /**
     * Shut down a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> shutDown(Server... serverRefs) {
        return powerOperationResponse(
            Arrays.asList(serverRefs),
            "Shutdown",
            client.shutDown(ids(serverRefs))
        );
    }

    /**
     * Shut down a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> shutDown(ServerFilter serverFilter) {
        List<Server> serverList = findServers(serverFilter);

        return powerOperationResponse(
            serverList,
            "Shutdown",
            client.shutDown(ids(serverList))
        );
    }

    /**
     * Archive a single server or group of servers
     *
     * @param serverRefs server references list
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> archive(Server... serverRefs) {
        return powerOperationResponse(
            Arrays.asList(serverRefs),
            "Archive",
            client.archive(ids(serverRefs))
        );
    }

    /**
     * Archive a single server or group of servers
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for Server list
     */
    public OperationFuture<List<Server>> archive(ServerFilter serverFilter) {
        List<Server> serverList = findServers(serverFilter);

        return powerOperationResponse(
            serverList,
            "Archive",
            client.archive(ids(serverList))
        );
    }

    /**
     * Create snapshot of a single server or group of servers
     *
     * @param expirationDays expiration days (must be between 1 and 10)
     * @param serverRefs     server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<Server>> createSnapshot(Integer expirationDays, Server... serverRefs) {
        return powerOperationResponse(
            Arrays.asList(serverRefs),
            "Create Snapshot",
            client.createSnapshot(
                new CreateSnapshotRequest()
                    .snapshotExpirationDays(expirationDays)
                    .serverIds(ids(serverRefs))
            )
        );
    }

    /**
     * Create snapshot of a single server or group of servers. Default expiration time is 10 days.
     *
     * @param serverRefs     server references list
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<Server>> createSnapshot(Server... serverRefs) {
        return createSnapshot(10, serverRefs);
    }

    /**
     * Create snapshot of a single server or group of servers
     *
     * @param expirationDays expiration days (must be between 1 and 10)
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<Server>> createSnapshot(Integer expirationDays, ServerFilter serverFilter) {
        List<Server> serverList = findServers(serverFilter);

        return powerOperationResponse(
            serverList,
            "Create Snapshot",
            client.createSnapshot(
                new CreateSnapshotRequest()
                    .snapshotExpirationDays(expirationDays)
                    .serverIds(ids(serverList))
            )
        );
    }

    /**
     * Create snapshot of a single server or group of servers. Default expiration time is 10 days.
     *
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for BaseServerResponse list
     */
    public OperationFuture<List<Server>> createSnapshot(ServerFilter serverFilter) {
        return createSnapshot(10, serverFilter);
    }

    /**
     * Delete all snapshots for provided servers
     * @param servers server references
     * @return OperationFuture wrapper for list of ServerRef
     */
    public OperationFuture<List<Server>> deleteSnapshot(Server... servers) {
        List<Server> serverList = Arrays.asList(servers);

        List<JobFuture> futures = serverList.stream()
            .map(serverRef -> findByRef(serverRef))
            .flatMap(metadata -> metadata.getDetails().getSnapshots().stream())
            .map(snapshot ->
                baseServerResponse(
                    client.deleteSnapshot(snapshot.getServerId(),
                        snapshot.getId()))
                    .jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            serverList,
            new ParallelJobsFuture(futures)
        );
    }

    /**
     * Delete all snapshots for server criteria
     * @param serverFilter search servers criteria
     * @return OperationFuture wrapper for list of ServerRef
     */
    public OperationFuture<List<Server>> deleteSnapshot(ServerFilter serverFilter) {
        return deleteSnapshot(getRefsFromFilter(serverFilter));
    }

    /**
     * Restore a given archived server to a specified group
     *
     * @param server server reference
     * @param group  group reference
     * @return OperationFuture wrapper for BaseServerResponse
     */
    public OperationFuture<Link> restore(Server server, Group group) {
        return baseServerResponse(
                restore(server, groupService.findByRef(group).getId())
        );
    }

    private Link restore(Server server, String groupId) {
        return client.restore(
                idByRef(server),
                new RestoreServerRequest()
                        .targetGroupId(groupId)
        );
    }

    /**
     * Restore a group of archived servers to a specified group
     * @param servers servers references
     * @return OperationFuture wrapper for list of ServerRef
     */
    OperationFuture<List<Server>> restore(String groupId, Server... servers) {
        return restore(Arrays.asList(servers), groupId);
    }

    /**
     * Restore a list of archived servers to a specified group
     * @param serverList server List references
     * @return OperationFuture wrapper for list of ServerRef
     */
    OperationFuture<List<Server>> restore(List<Server> serverList, String groupId) {
        List<JobFuture> futures = serverList.stream()
            .map(server ->
                baseServerResponse(
                    client.restore(
                        idByRef(server),
                        new RestoreServerRequest()
                            .targetGroupId(groupId))
                )
                .jobFuture()
            )
            .collect(toList());

        return new OperationFuture<>(
            serverList,
            new ParallelJobsFuture(futures)
        );
    }

    /**
     * Revert a set of servers to snapshot
     * @param servers server references
     * @return OperationFuture wrapper for list of ServerRef
     */
    public OperationFuture<List<Server>> revertToSnapshot(Server... servers) {
        List<Server> serverList = Arrays.asList(servers);

        List<JobFuture> futures = serverList.stream()
            .map(serverRef -> findByRef(serverRef))
            .flatMap(metadata -> metadata.getDetails().getSnapshots().stream())
            .map(snapshot ->
                baseServerResponse(
                    client.revertToSnapshot(snapshot.getServerId(), snapshot.getId())
                )
                .jobFuture()
            )
            .collect(toList());

        return new OperationFuture<>(
            serverList,
            new ParallelJobsFuture(futures)
        );
    }

    /**
     * Revert a set of servers to snapshot
     * @param filter search servers criteria
     * @return OperationFuture wrapper for list of ServerRef
     */
    public OperationFuture<List<Server>> revertToSnapshot(ServerFilter filter) {
        return revertToSnapshot(getRefsFromFilter(filter));
    }

    private List<String> ids(Server... servers) {
        return ids(Arrays.asList(servers));
    }

    public List<String> ids(ServerFilter serverFilter) {
        List<ServerMetadata> serverMetadataList = find(serverFilter);

        return
            serverMetadataList
                .stream()
                .filter(notNull())
                .map(ServerMetadata::getId)
                .map(String::toUpperCase)
                .collect(toList());
    }

    public List<String> ids(List<Server> serverList) {
        return
            serverList
                .stream()
                .filter(notNull())
                .map(this::idByRef)
                .map(String::toUpperCase)
                .collect(toList());
    }

    /**
     * Add public IP to server
     *
     * @param serverRef        server reference
     * @param publicIpConfig publicIp config
     * @return OperationFuture wrapper for ServerRef
     */
    public OperationFuture<Server> addPublicIp(Server serverRef, CreatePublicIpConfig publicIpConfig) {
        checkNotNull(serverRef, "Server reference must be not null");
        checkNotNull(publicIpConfig, "PublicIpConfig must be not null");
        Link response = client.addPublicIp(idByRef(serverRef), publicIpConverter.createPublicIpRequest(publicIpConfig));

        return new OperationFuture<>(
            serverRef,
            response.getId(),
            queueClient
        );
    }

    /**
     * Add public IP to list servers
     *
     * @param servers        servers reference list
     * @param publicIpConfig publicIp config
     * @return OperationFuture wrapper for list of ServerRef
     */
    public OperationFuture<List<Server>> addPublicIp(List<Server> servers, CreatePublicIpConfig publicIpConfig) {
        checkNotNull(servers, "Server list must be not null");
        checkNotNull(publicIpConfig, "PublicIpConfig must be not null");

        List<JobFuture> futures = servers.stream()
            .map(serverRef -> addPublicIp(serverRef, publicIpConfig).jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            servers,
            new ParallelJobsFuture(futures)
        );
    }

    /**
     * Add public IP to list servers
     *
     * @param serverFilter   server search criteria
     * @param publicIpConfig publicIp config
     * @return OperationFuture wrapper for list of ServerRef
     */
    public OperationFuture<List<Server>> addPublicIp(ServerFilter serverFilter,  CreatePublicIpConfig publicIpConfig) {
        return addPublicIp(Arrays.asList(getRefsFromFilter(serverFilter)), publicIpConfig);
    }

    /**
     * Modify ALL existing public IPs on server
     * @param server server reference
     * @param config publicIp config
     * @return OperationFuture wrapper for ServerRef
     */
    public OperationFuture<Server> modifyPublicIp(Server server, ModifyPublicIpConfig config) {
        checkNotNull(config, "PublicIpConfig must be not null");
        List<IpAddress> ipAddresses = findByRef(server).getDetails().getIpAddresses();
        List<String> responseIds = ipAddresses.stream()
            .map(address -> address.getPublicIp())
            .filter(notNull())
            .map(ipAddress ->
                client.modifyPublicIp(idByRef(server),
                    ipAddress,
                    publicIpConverter.createPublicIpRequest(config)))
            .map(Link::getId)
            .collect(toList());

        return new OperationFuture<>(
            server,
            responseIds,
            queueClient
        );
    }

    /**
     * Modify provided public IP on server
     * @param server server reference
     * @param publicIp public ip
     * @param config publicIp config
     * @return OperationFuture wrapper for ServerRef
     */
    public OperationFuture<Server> modifyPublicIp(Server server, String publicIp, ModifyPublicIpConfig config) {
        checkNotNull(config, "PublicIpConfig must be not null");
        checkNotNull(publicIp, "public ip must not be null");

        Link response = client.modifyPublicIp(idByRef(server),
                publicIp,
                publicIpConverter.createPublicIpRequest(config)
        );

        return new OperationFuture<>(
            server,
            response.getId(),
            queueClient
        );
    }

    /**
     * Modify ALL existing public IPs on servers
     * @param servers The list of server references
     * @param config  publicIp config
     * @return OperationFuture wrapper for list of ServerRef
     */
    public OperationFuture<List<Server>> modifyPublicIp(List<Server> servers, ModifyPublicIpConfig config) {
        List<JobFuture> futures = servers.stream()
            .map(serverRef -> modifyPublicIp(serverRef, config).jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            servers,
            new ParallelJobsFuture(futures)
        );
    }

    /**
     * Modify existing public IP on servers
     * @param filter The server filter object
     * @param config  publicIp config
     * @return OperationFuture wrapper for list of ServerRef
     */
    public OperationFuture<List<Server>> modifyPublicIp(ServerFilter filter, ModifyPublicIpConfig config) {
        return modifyPublicIp(Arrays.asList(getRefsFromFilter(filter)), config);
    }

    Server[] getRefsFromFilter(ServerFilter filter) {
        List<Server> serverRefs = filter.getServerIds().stream()
            .map(id -> Server.refById(id))
            .collect(toList());

        return serverRefs.toArray(new Server[serverRefs.size()]);
    }

    /**
     * Get public IP object
     *
     * @param serverRef server reference
     * @param publicIp  existing public IP address
     * @return public IP response object
     */
    public PublicIpMetadata getPublicIp(Server serverRef, String publicIp) {
        return client.getPublicIp(idByRef(serverRef), publicIp);
    }

    /**
     * Get list public IPs for provided server reference {@code server}
     *
     * @param server server reference
     * @return list public IPs
     */
    public List<PublicIpMetadata> findPublicIp(Server server) {
        List<IpAddress> ipAddresses = findByRef(server).getDetails().getIpAddresses();

        return ipAddresses.stream()
            .map(IpAddress::getPublicIp)
            .filter(notNull())
            .map(address -> getPublicIp(server, address))
            .collect(toList());
    }

    /**
     * Remove public IP from server
     *
     * @param serverRef server reference
     * @param ipAddress  existing public IP address
     * @return OperationFuture wrapper for ServerRef
     */
    public OperationFuture<Server> removePublicIp(Server serverRef, String ipAddress) {
        checkNotNull(ipAddress, "ipAddress must be not null");

        Link response = client.removePublicIp(idByRef(serverRef), ipAddress);

        return new OperationFuture<>(
            serverRef,
            response.getId(),
            queueClient
        );
    }

    /**
     * Remove all public IPs from server
     *
     * @param serverRef server reference
     * @return OperationFuture wrapper for ServerRef
     */
    public OperationFuture<Server> removePublicIp(Server serverRef) {
        checkNotNull(serverRef, "Server reference must be not null");
        ServerMetadata serverMetadata = findByRef(serverRef);
        List<JobFuture> jobFutures = serverMetadata.getDetails().getIpAddresses()
            .stream()
            .map(IpAddress::getPublicIp)
            .filter(notNull())
            .map(address -> removePublicIp(serverRef, address).jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            serverRef,
            new ParallelJobsFuture(jobFutures)
        );
    }

    /**
     * Remove all public IPs from servers array
     *
     * @param servers server references
     * @return OperationFuture wrapper for list of ServerRef
     */
    public OperationFuture<List<Server>> removePublicIp(Server... servers) {
        checkNotNull(servers, "Server references must be not null");
        List<Server> serverList = Arrays.asList(servers);

        List<JobFuture> futures = serverList.stream()
            .map(serverRef -> removePublicIp(serverRef).jobFuture())
            .collect(toList());

        return new OperationFuture<>(
            serverList,
            new ParallelJobsFuture(futures)
        );
    }

    /**
     * Remove all public IPs
     *
     * @param serverFilter server search criteria
     * @return OperationFuture wrapper for list of ServerRef
     */
    public OperationFuture<List<Server>> removePublicIp(ServerFilter serverFilter) {
        return removePublicIp(getRefsFromFilter(serverFilter));
    }

    public OperationFuture<List<Server>> powerOperationResponse(
            List<Server> serverList,
            String operation,
            BaseServerListResponse response
    ) {
        if (response.hasErrors()) {
            throw response.summaryException();
        }

        return
            new OperationFuture<>(
                serverList,
                new ParallelJobsFuture(
                    jobInfoList(operation, response),
                    queueClient
                )
            );
    }

    private List<JobInfo> jobInfoList(String operation, BaseServerListResponse apiResponse) {
        return map(apiResponse, response ->
            new ResourceJobInfo(response.findStatusId(), operation, Server.refById(response.getServer()))
        );
    }

    private OperationFuture<Link> baseServerResponse(Link response) {
        return new OperationFuture<>(
            response,
            response.getId(),
            queueClient
        );
    }

    private OperationFuture<Server> baseServerResponse(Server server, Link response) {
        return
            new OperationFuture<>(
                server,
                response.getId(),
                queueClient
        );
    }

    private List<Server> findServers(ServerFilter serverFilter) {
        return find(serverFilter).stream()
            .map(
                metadata -> Server.refById(
                    metadata.getId()
                )
            )
            .collect(toList());
    }

    public SshClient execSsh(Server server) {
        checkNotNull(server);
        ServerMetadata metadata = findByRef(server);

        List<PublicIpMetadata> ipMetadataList = findPublicIp(server);
        if (ipMetadataList.isEmpty()) {
            OperationFuture<Server> addPublicIp = addPublicIp(server, new CreatePublicIpConfig().openPorts(PortConfig.SSH));
            addPublicIp.waitUntilComplete();
        }

        List<IpAddress> ipAddresses = metadata.getDetails().getIpAddresses();
        Optional<String> publicIp = ipAddresses.stream()
                .map(IpAddress::getPublicIp)
                .filter(notNull())
                .findFirst();

        ServerCredentials serverCredentials = client.getServerCredentials(metadata.getId());
        return new SshjClient.Builder()
                .username(serverCredentials.getUserName())
                .password(serverCredentials.getPassword())
                .host(publicIp.get())
                .build();
    }

    public SshClient execSsh(Server... onServers) {
        throw new UnsupportedOperationException();
    }
}
