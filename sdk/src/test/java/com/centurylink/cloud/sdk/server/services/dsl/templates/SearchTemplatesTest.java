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

package com.centurylink.cloud.sdk.server.services.dsl.templates;

import com.centurylink.cloud.sdk.base.services.client.DataCentersClient;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter;
import com.centurylink.cloud.sdk.core.injector.Inject;
import com.centurylink.cloud.sdk.core.services.filter.Filter;
import com.centurylink.cloud.sdk.server.services.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.server.services.dsl.TemplateService;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.TemplateFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.CpuArchitecture;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.OsFilter;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.filters.os.OsType;
import com.centurylink.cloud.sdk.server.services.dsl.domain.template.refs.Template;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.base.services.client.domain.datacenters.deployment.capabilities.TemplateMetadata.MANAGED_OS_VALUE;
import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.base.services.dsl.domain.datacenters.refs.DataCenter.US_EAST_STERLING;
import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public class SearchTemplatesTest extends AbstractServersSdkTest {

    @Inject
    TemplateService templateService;

    @Inject @Mock
    DataCentersClient dataCentersClient;

    @BeforeMethod
    public void setUp() throws Exception {
        Mockito
            .doReturn(fromJson("data_centers_list.json", GetDataCenterListResponse.class))
            .when(dataCentersClient).findAllDataCenters();

        Mockito
            .doReturn(
                fromJson("va1_deployment_capabilities.json", DatacenterDeploymentCapabilitiesMetadata.class)
            )
            .when(dataCentersClient).getDeploymentCapabilities("va1");

        Mockito
            .doReturn(
                fromJson("de1_deployment_capabilities.json", DatacenterDeploymentCapabilitiesMetadata.class)
            )
            .when(dataCentersClient).getDeploymentCapabilities("de1");
    }

    @Test
    public void testFindTemplateByOsRef() throws Exception {
        TemplateMetadata metadata = templateService.findByRef(Template.refByOs()
            .dataCenter(US_EAST_STERLING)
            .type(OsType.RHEL)
            .version("6")
            .architecture(CpuArchitecture.x86_64)
        );

        assertEquals(metadata.getName(), "RHEL-6-64-TEMPLATE");
    }

    @Test
    public void testFindTemplateByNameRef() {
        TemplateMetadata metadata = templateService.findByRef(Template.refByName()
            .dataCenter(US_EAST_STERLING)
            .name("CENTOS-6-64-TEMPLATE")
        );

        assertEquals(metadata.getName(), "CENTOS-6-64-TEMPLATE");
    }

    @Test
    public void testFindTemplateByDescriptionRef() {
        TemplateMetadata metadata = templateService.findByRef(Template.refByDescription()
            .dataCenter(US_EAST_STERLING)
            .description("pxe boot")
        );

        assertEquals(metadata.getName(), "PXE-TEMPLATE");
    }

    @Test
    public void testFindAllCentOsTemplates() {
        List<TemplateMetadata> results = templateService.find(new TemplateFilter()
            .dataCenters("va1") // US_EAST_STERLING
            .dataCenters(DataCenter.refByName("Sterling"))
            .osTypes(new OsFilter()
                    .type(OsType.CENTOS)
            )
        );

        assertEquals(results.size(), 2);
        assertEquals(
            map(results, TemplateMetadata::getName),
            asList("CENTOS-5-64-TEMPLATE", "CENTOS-6-64-TEMPLATE")
        );
    }

    @Test
    public void testFindAllTemplatesWithManagedOsCapabilities() {
        List<TemplateMetadata> results = templateService.find(new TemplateFilter()
            .dataCentersWhere(d -> "va1".equals(d.getId()))
            .where(t -> t.getCapabilities().contains(MANAGED_OS_VALUE))
        );

        assertEquals(results.size(), 8);
    }

    @Test
    public void testOrOperation() {
        List<TemplateMetadata> results = templateService.find(Filter.or(
            new TemplateFilter()
                .dataCenterNameContains("sterling")
                .osTypes(new OsFilter()
                    .type(OsType.CENTOS)
                ),

            new TemplateFilter()
                .dataCenters(DE_FRANKFURT)
                .nameContains("debian")
        ));

        assertEquals(results.size(), 4);
        assertEquals(
            map(results, TemplateMetadata::getName),
            asList(
                "CENTOS-5-64-TEMPLATE", "CENTOS-6-64-TEMPLATE",
                "DEBIAN-6-64-TEMPLATE", "DEBIAN-7-64-TEMPLATE"
            )
        );
    }

    @Test
    public void testAndOperation() {
        List<TemplateMetadata> results = templateService.find(Filter.and(
            new TemplateFilter()
                .dataCenters(US_EAST_STERLING)
                .where(t -> t.getName().contains("6-64")),

            Filter.or(
                new TemplateFilter()
                    .dataCenters(US_EAST_STERLING)
                    .osTypes(new OsFilter()
                        .type(OsType.CENTOS)
                    ),

                new TemplateFilter()
                    .dataCenters(US_EAST_STERLING)
                    .osTypes(new OsFilter()
                        .type(OsType.DEBIAN)
                    )
            )
        ));

        assertEquals(results.size(), 2);
        assertEquals(
            map(results, TemplateMetadata::getName),
            asList("CENTOS-6-64-TEMPLATE", "DEBIAN-6-64-TEMPLATE")
        );
    }

}
