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

package com.centurylink.cloud.sdk.loadbalancer.services.dsl.group;

import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.loadbalancer.services.AbstractLoadBalancerSdkTest;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.LoadBalancerService;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerConfig;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerMetadata;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.LoadBalancerStatus;
import com.centurylink.cloud.sdk.loadbalancer.services.dsl.domain.refs.group.LoadBalancer;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
@WireMockFileSource("create")
public class CreateLoadBalancerTest extends AbstractLoadBalancerSdkTest implements WireMockMixin {

    @Inject
    LoadBalancerService loadBalancerService;

    LoadBalancer loadBalancer;

    @Test
    public void testCreate() {
        String name = "Balancer2";
        String description = "Balancer2 description";

        loadBalancer = loadBalancerService
            .create(
                new LoadBalancerConfig()
                    .name(name)
                    .description(description)
                    .status(LoadBalancerStatus.ENABLED)
                    .dataCenter(DataCenter.US_EAST_STERLING)
            )
            .waitUntilComplete()
            .getResult();

        LoadBalancerMetadata metadata = loadBalancerService.findByRef(loadBalancer);

        assertNotNull(metadata);
        assertEquals(metadata.getName(), name);
        assertEquals(metadata.getDescription(), description);
        assertEquals(metadata.getStatus(), LoadBalancerStatus.ENABLED.getCode());
        assertEquals(metadata.getDataCenterId(), DataCenter.US_EAST_STERLING.getId());
    }

    @AfterMethod
    public void deleteBalancer() {
        loadBalancerService.delete(loadBalancer.asFilter());
    }
}
