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
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.policy.services.client.PolicyClient;
import com.centurylink.cloud.sdk.policy.services.dsl.domain.PolicyConverter;

/**
 * @author Aliaksandr Krasitski
 */
public class PolicyService {

    private final AntiAffinityService antiAffinityService;
    private final AlertService alertService;

    @Inject
    public PolicyService(
        PolicyClient client,
        DataCenterService dataCenterService,
        PolicyConverter converter
    ) {
        this.antiAffinityService = new AntiAffinityService(client, dataCenterService);
        this.alertService = new AlertService(client, converter);
    }

    /**
     * Gets anti-affinity policies service
     * @return anti-affinity service
     */
    public AntiAffinityService antiAffinity() {
        return antiAffinityService;
    }

    /**
     * Gets alert policies service
     * @return alert service
     */
    public AlertService alert() {
        return alertService;
    }


}