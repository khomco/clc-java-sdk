package com.centurylink.cloud.sdk.servers.services.templates;

import com.centurylink.cloud.sdk.core.commons.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities.DatacenterDeploymentCapabilitiesMetadata;
import com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities.TemplateMetadata;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.services.TemplateService;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.filters.TemplateFilter;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;
import com.google.inject.Inject;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.core.commons.client.domain.datacenters.deployment.capabilities.TemplateMetadata.MANAGED_OS_VALUE;
import static com.centurylink.cloud.sdk.core.commons.services.domain.datacenters.refs.DataCenter.US_EAST_STERLING;
import static com.centurylink.cloud.sdk.core.services.function.Streams.map;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.RHEL;
import static java.util.Arrays.asList;

/**
 * @author Ilya Drabenia
 */
public class SearchTemplatesTest extends AbstractServersSdkTest {

    @Inject
    TemplateService templateService;

    @Inject @Spy
    DataCentersClient dataCentersClient;

    @BeforeMethod
    public void setUp() throws Exception {
        mockDataCentersList();
        mockVa1();
    }

    private void mockDataCentersList() {
        Mockito
            .doReturn(fromJson("data_centers_list.json", GetDataCenterListResponse.class))
            .when(dataCentersClient).findAllDataCenters();
    }

    private void mockVa1() {
        Mockito
            .doReturn(
                fromJson("va1_deployment_capabilities.json", DatacenterDeploymentCapabilitiesMetadata.class)
            )
            .when(dataCentersClient).getDeploymentCapabilities("va1");
    }

    @Test
    public void testFindTemplateByOsRef() throws Exception {
        TemplateMetadata metadata = templateService.findByRef(Template.refByOs()
            .dataCenter(US_EAST_STERLING)
            .type(RHEL)
            .version("6")
            .architecture(x86_64)
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
            .dataCenters(US_EAST_STERLING)
            .osTypes(new OsFilter()
                .type(CENTOS)
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
            .dataCenters(US_EAST_STERLING)
            .where(t -> t.getCapabilities().contains(MANAGED_OS_VALUE))
        );

        assertEquals(results.size(), 8);
    }

}
