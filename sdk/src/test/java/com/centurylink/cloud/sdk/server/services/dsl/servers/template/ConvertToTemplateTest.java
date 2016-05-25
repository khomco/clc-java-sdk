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

package com.centurylink.cloud.sdk.server.services.dsl.servers.template;

import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.dsl.ServerService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.ConvertToTemplateConfig;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.TemplateVisibility;
import com.centurylink.cloud.sdk.server.services.dsl.domain.server.refs.Server;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

@Test(groups = {RECORDED})
public class ConvertToTemplateTest extends AbstractServersSdkTest implements WireMockMixin {

    @Inject
    ServerService serverService;

    Server expectedServer;

    @Test
    public void testConvertSeverToTemplate() {
        Server server = Server.refById("de1altdcln04");

        ConvertToTemplateConfig config = new ConvertToTemplateConfig()
                .description("de1altdcln04-converttotemplate")
                .password("Password123#")
                .visibility(TemplateVisibility.PRIVATE);

        expectedServer = serverService.convertToTemplate(server, config)
            .waitUntilComplete()
            .getResult();

        assertNotNull(expectedServer);
        assertEquals(server, expectedServer);
    }

    @Test
    public void testConvertSeverToTemplateWithoutCredentials() {
        Server server = Server.refById("de1altdcln04");

        ConvertToTemplateConfig config = new ConvertToTemplateConfig()
                .description("de1altdcln04-converttotemplate")
                .visibility(TemplateVisibility.PRIVATE);

        expectedServer = serverService.convertToTemplate(server, config)
                .waitUntilComplete()
                .getResult();

        assertNotNull(expectedServer);
        assertEquals(server, expectedServer);
    }
}
