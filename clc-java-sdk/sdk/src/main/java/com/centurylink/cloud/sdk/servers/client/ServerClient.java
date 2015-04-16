package com.centurylink.cloud.sdk.servers.client;

import com.centurylink.cloud.sdk.core.auth.services.BearerAuthentication;
import com.centurylink.cloud.sdk.core.client.BaseSdkClient;
import com.centurylink.cloud.sdk.core.client.InvocationFuture;
import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.BaseServerListResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.BaseServerResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.CreateServerRequest;
import com.centurylink.cloud.sdk.servers.client.domain.server.PublicIpAddressResponse;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.client.domain.server.template.CreateTemplateRequest;
import com.centurylink.cloud.sdk.servers.services.domain.server.PublicIpAddressRequest;
import com.google.common.util.concurrent.SettableFuture;
import com.google.inject.Inject;

import javax.ws.rs.client.InvocationCallback;
import java.util.List;

import static javax.ws.rs.client.Entity.entity;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON_TYPE;

/**
 * @author ilya.drabenia
 */
public class ServerClient extends BaseSdkClient {

    @Inject
    public ServerClient(BearerAuthentication authFilter) {
        super(authFilter);
    }

    /**
     * Creates a new server. Calls to this operation must include a token acquired
     * from the authentication endpoint. See the Login API for information on acquiring
     * this token.
     */
    public BaseServerResponse create(CreateServerRequest request) {
        return
            client("/servers/{accountAlias}")
                .request().post(
                    entity(request, APPLICATION_JSON_TYPE)
                )
                .readEntity(BaseServerResponse.class);
    }

    public SettableFuture<BaseServerResponse> createAsync(CreateServerRequest request) {
        final InvocationFuture<BaseServerResponse> future = new InvocationFuture<BaseServerResponse>();

        client("/servers/{accountAlias}")
            .request()
            .async()
            .post(entity(request, APPLICATION_JSON_TYPE), new InvocationCallback<BaseServerResponse>() {
                @Override
                public void completed(BaseServerResponse createServerResponse) {
                    future.toListenableFuture().set(createServerResponse);
                }

                @Override
                public void failed(Throwable throwable) {
                    future.toListenableFuture().setException(throwable);
                }
            });

        return future.toListenableFuture();
    }

    public BaseServerResponse delete(String serverId) {
        return
            client("/servers/{accountAlias}/{serverId}")
                .resolveTemplate("serverId", serverId)
                .request()
                .delete(BaseServerResponse.class);
    }

    public ServerMetadata findServerByUuid(String uuid) {
        return
            client("/servers/{accountAlias}/{serverId}?uuid=true")
                .resolveTemplate("serverId", uuid)
                .request()
                .get(ServerMetadata.class);
    }

    public SettableFuture<ServerMetadata> findServerByUuidAsync(String uuid) {
        final InvocationFuture<ServerMetadata> future = new InvocationFuture<>();

        client("/servers/{accountAlias}/{serverId}?uuid=true")
            .resolveTemplate("serverId", uuid)
            .request()
            .buildGet()
            .submit(new InvocationCallback<ServerMetadata>() {
                @Override
                public void completed(ServerMetadata serverMetadata) {
                    future.toListenableFuture().set(serverMetadata);
                }

                @Override
                public void failed(Throwable throwable) {
                    future.toListenableFuture().setException(throwable);
                }
            });

        return future.toListenableFuture();
    }

    public ServerMetadata findServerById(String id) {
        return
            client("/servers/{accountAlias}/{serverId}")
                .resolveTemplate("serverId", id)
                .request()
                .get(ServerMetadata.class);
    }

    public GroupMetadata getGroup(String rootGroupId) {
        return
            client("/groups/{accountAlias}/{rootGroupId}")
                .resolveTemplate("rootGroupId", rootGroupId)
                .request().get(GroupMetadata.class);
    }

    public BaseServerResponse convertToTemplate(CreateTemplateRequest request) {
        return
            client("/servers/{accountAlias}/{serverId}/convertToTemplate")
                .resolveTemplate("serverId", request.getServerId())
                .request()
                .post(entity(request, APPLICATION_JSON))
                .readEntity(BaseServerResponse.class);
    }

    public BaseServerListResponse powerOn(List<String> serverIdList) {
        return sendPowerOperationRequest("powerOn", serverIdList);
    }

    public BaseServerListResponse powerOff(List<String> serverIdList) {
        return sendPowerOperationRequest("powerOff", serverIdList);
    }

    public BaseServerListResponse startMaintenance(List<String> serverIdList) {
        return sendPowerOperationRequest("startMaintenance", serverIdList);
    }

    public BaseServerListResponse stopMaintenance(List<String> serverIdList) {
        return sendPowerOperationRequest("stopMaintenance", serverIdList);
    }

    public BaseServerListResponse pause(List<String> serverIdList) {
        return sendPowerOperationRequest("pause", serverIdList);
    }

    public BaseServerListResponse reboot(List<String> serverIdList) {
        return sendPowerOperationRequest("reboot", serverIdList);
    }

    public BaseServerListResponse reset(List<String> serverIdList) {
        return sendPowerOperationRequest("reset", serverIdList);
    }

    public BaseServerListResponse shutDown(List<String> serverIdList) {
        return sendPowerOperationRequest("shutDown", serverIdList);
    }

    public BaseServerListResponse archive(List<String> serverIdList) {
        return sendPowerOperationRequest("archive", serverIdList);
    }

    private BaseServerListResponse sendPowerOperationRequest(String operationName, List<String> serverIdList) {
        return
            client("/operations/{accountAlias}/servers/" + operationName)
                .request()
                .post(entity(serverIdList, APPLICATION_JSON))
                .readEntity(BaseServerListResponse.class);
    }

    public Link addPublicIp(String serverId, PublicIpAddressRequest publicIpAddressRequest) {
        return
            client("/servers/{accountAlias}/{serverId}/publicIPAddresses")
                .resolveTemplate("serverId", serverId)
                .request()
                .post(entity(publicIpAddressRequest, APPLICATION_JSON_TYPE))
                .readEntity(Link.class);
    }

    public PublicIpAddressResponse getPublicIp(String serverId, String publicIp) {
        return
                client("/servers/{accountAlias}/{serverId}/publicIPAddresses/{publicIp}")
                        .resolveTemplate("serverId", serverId)
                        .resolveTemplate("publicIp", publicIp)
                        .request()
                        .get(PublicIpAddressResponse.class);
    }
}