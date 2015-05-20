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

package com.centurylink.cloud.sdk.servers.services.domain.group;

import com.centurylink.cloud.sdk.servers.client.domain.group.*;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ilya.drabenia
 */
public class GroupConverter {

    public CreateGroupRequest createGroupRequest(GroupConfig groupConfig, String parentGroupId) {
        return new CreateGroupRequest()
            .name(groupConfig.getName())
            .description(groupConfig.getDescription())
            .parentGroupId(parentGroupId)
            .customFields(groupConfig.getCustomFields());
    }

    public UpdateGroupRequest createUpdateGroupRequest(GroupConfig groupConfig, String parentGroupId) {
        UpdateGroupRequest req = new UpdateGroupRequest();

        if (groupConfig.getName() != null) {
            req.add(new SimplePatchOperation(PatchOperation.NAME, groupConfig.getName()));
        }
        if (groupConfig.getDescription() != null) {
            req.add(new SimplePatchOperation(PatchOperation.DESCRIPTION, groupConfig.getDescription()));
        }
        if (parentGroupId != null) {
            req.add(new SimplePatchOperation(PatchOperation.GROUP_PARENT_ID, parentGroupId));
        }
        if (groupConfig.getCustomFields() != null) {
            req.add(new CustomFieldPatchOperation(groupConfig.getCustomFields()));
        }
        return req;
    }

    public GroupConfig createGroupConfig(GroupHierarchyConfig hierarchyConfig, String parentGroupId) {
        return new GroupConfig()
            .name(hierarchyConfig.getName())
            .description(hierarchyConfig.getDescription())
            .parentGroup(Group.refById(parentGroupId));
    }

    public BillingStats convertBillingStats(ClientBillingStats clientBillingStats) {
        List<GroupBilling> groupBillingList = new ArrayList<>();

        clientBillingStats.getGroups().forEach(
            (groupId, clientGroupBilling) -> {
                List<ServerBilling> serverBillingList = new ArrayList<>();

                clientGroupBilling.getServers().forEach(
                    (serverId, clientServerBilling) ->
                        serverBillingList.add(
                            convertServerBilling(serverId, clientServerBilling)
                        )
                );

                groupBillingList.add(
                    convertGroupBilling(groupId, clientGroupBilling, serverBillingList)
                );
            }
        );

        return convertBillingStats(clientBillingStats, groupBillingList);
    }

    private BillingStats convertBillingStats(
            ClientBillingStats clientBillingStats,
            List<GroupBilling> groupBillingList
    ) {
        return
            new BillingStats()
                .date(clientBillingStats.getDate())
                .groups(groupBillingList);
    }

    private GroupBilling convertGroupBilling(
            String groupId,
            ClientGroupBilling clientGroupBilling,
            List<ServerBilling> serverBillingList
    ) {
        return
            new GroupBilling()
                .groupId(groupId)
                .name(clientGroupBilling.getName())
                .servers(serverBillingList);
    }

    private ServerBilling convertServerBilling(String serverId, ClientServerBilling clientServerBilling) {
        return
            new ServerBilling()
                .serverId(serverId)
                .templateCost(clientServerBilling.getTemplateCost())
                .archiveCost(clientServerBilling.getArchiveCost())
                .monthlyEstimate(clientServerBilling.getMonthlyEstimate())
                .monthToDate(clientServerBilling.getMonthToDate())
                .currentHour(clientServerBilling.getCurrentHour());
    }

    public MonitoringStatisticRequest createMonitoringStatisticRequest(ServerMonitoringConfig config) {

        MonitoringStatisticRequest request =
            new MonitoringStatisticRequest()
                .type(config.getType().name());

        new ServerMonitoringConfigValidator().getValidator(config).validate();

        return request
            .start(formatDateTime(config.getFrom()))
            .end(formatDateTime(config.getTo()))
            .sampleInterval(buildInterval(config.getInterval()));
    }

    private String formatDateTime(OffsetDateTime dateTime) {
        return dateTime != null
            ? dateTime.atZoneSameInstant(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
            : null;
    }

    private String buildInterval(Duration duration) {
        if (duration == null) {
            return null;
        }
        long days = duration.toDays();
        long hours = duration.minusDays(days).toHours();
        long minutes = duration.minusDays(days).minusHours(hours).toMinutes();
        long seconds = duration.minusDays(days).minusHours(hours).minusMinutes(minutes).getSeconds();

        return String.format("%s:%s:%s:%s",
            formatIntervalValue(days),
            formatIntervalValue(hours),
            formatIntervalValue(minutes),
            formatIntervalValue(seconds)
        );
    }

    private String formatIntervalValue(long value) {
        return value < 10 ? ("0" + value) : ("" + value);
    }


}
