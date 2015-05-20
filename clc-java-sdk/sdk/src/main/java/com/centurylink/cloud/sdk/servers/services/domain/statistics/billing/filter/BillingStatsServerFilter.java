/*
 * (c) 2015 CenturyLink Cloud. All Rights Reserved.
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

package com.centurylink.cloud.sdk.servers.services.domain.statistics.billing.filter;

import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.ServerService;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class BillingStatsServerFilter implements BillingStatsFilter {

    private List<ServerMetadata> serverMetadataList;
    private List<String> serverIdRestrictionsList;

    public BillingStatsServerFilter(ServerFilter serverFilter, ServerService serverService) {
        serverMetadataList = serverService.find(serverFilter);

        serverIdRestrictionsList = serverMetadataList
            .stream()
            .map(ServerMetadata::getId)
            .collect(toList());
    }

    @Override
    public GroupFilter getFilter() {
        return
            new GroupFilter().id(
                serverMetadataList
                    .stream()
                    .map(ServerMetadata::getGroupId)
                    .distinct()
                    .collect(toList())
            );
    }

    public List<String> getServerIdRestrictionsList() {
        return serverIdRestrictionsList;
    }
}
