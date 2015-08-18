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

package com.centurylink.cloud.sdk.policy.services.dsl;

import com.centurylink.cloud.sdk.base.services.dsl.DataCenterService;
import com.centurylink.cloud.sdk.policy.services.client.PolicyClient;
import com.google.inject.Inject;

/**
 * @author Aliaksandr Krasitski
 */
public class PolicyService {

    private final AntiAffinityService antiAffinityService;

    @Inject
    public PolicyService(
        PolicyClient client,
        DataCenterService dataCenterService
    ) {
        this.antiAffinityService = new AntiAffinityService(client, dataCenterService);
    }

    public AntiAffinityService antiAffinity() {
        return antiAffinityService;
    }



}
