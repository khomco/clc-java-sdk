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

package com.centurylink.cloud.sdk.server.services.dsl.domain.server;

import com.centurylink.cloud.sdk.server.services.dsl.domain.group.refs.Group;
import com.centurylink.cloud.sdk.server.services.dsl.domain.network.refs.Network;

/**
 * @author Krishna Buchupally
 */
public class ConvertTemplateToServerConfig {

    private Group group;
    private Network network;
    private String password;

    public Group getGroup() {
        return group;
    }

    public ConvertTemplateToServerConfig group(Group group) {
        this.group = group;
        return this;
    }

    public Network getNetwork() {
        return network;
    }

    public ConvertTemplateToServerConfig network(Network network) {
        this.network = network;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public ConvertTemplateToServerConfig password(String password) {
        this.password = password;
        return this;
    }
}
