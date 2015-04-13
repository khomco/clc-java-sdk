package com.centurylink.cloud.sdk.servers.services.groups;

import com.centurylink.cloud.sdk.core.client.domain.Link;
import com.centurylink.cloud.sdk.core.datacenters.client.DataCentersClient;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.DataCenterMetadata;
import com.centurylink.cloud.sdk.core.datacenters.client.domain.GetDataCenterListResponse;
import com.centurylink.cloud.sdk.core.datacenters.services.DataCenterService;
import com.centurylink.cloud.sdk.core.datacenters.services.domain.filters.DataCenterFilter;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.group.GroupMetadata;
import com.centurylink.cloud.sdk.servers.services.GroupService;
import com.centurylink.cloud.sdk.servers.services.domain.group.Group;
import com.centurylink.cloud.sdk.servers.services.domain.group.filters.GroupFilter;
import com.google.inject.Inject;
import org.mockito.Mock;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.core.datacenters.services.domain.DataCenters.DE_FRANKFURT;
import static java.util.Arrays.asList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Ilya Drabenia
 */
public class SearchGroupsTest extends AbstractServersSdkTest {

    @Inject
    GroupService groupService;

    @Mock
    ServerClient serverClient;

    @Mock
    DataCenterService dataCenterService;

    @Mock
    DataCentersClient dataCentersClient;

    private void mockDataCentersMetadata() {
        when(dataCentersClient.findAllDataCenters()).thenReturn(new GetDataCenterListResponse(asList(
            new DataCenterMetadata("de1", "Frankfurt") {{
                getLinks().add(new Link() {{
                    setRel("group");
                    setId("rootGroupId");
                }});
            }}))
        );

        when(dataCenterService.find(any(DataCenterFilter.class))).thenReturn(asList(
            new DataCenterMetadata("de1", "Frankfurt") {{
                getLinks().add(new Link() {{
                    setRel("group");
                    setId("rootGroupId");
                }});
            }}
        ));
    }

    private void mockFrankfurtDataCenterRootGroup() {
        when(serverClient.getGroup("rootGroupId")).thenReturn(
            new GroupMetadata() {{
                setId("rootGroupId");
                setName("Root Group");
                getGroups().addAll(asList(
                    new GroupMetadata() {{
                        setId("1");
                        setName("Group1");
                    }},
                    new GroupMetadata() {{
                        setId("2");
                        setName("Archive");
                    }})
                );
            }}
        );
    }

    @Test
    public void testFindByIdRef() {
        mockDataCentersMetadata();
        mockFrankfurtDataCenterRootGroup();

        GroupMetadata group = groupService.findByRef(Group.refById()
            .dataCenter(DE_FRANKFURT)
            .id("1")
        );

        assert group.getId().equals("1");
    }

    @Test
    public void testFindByNameRef() {
        mockDataCentersMetadata();
        mockFrankfurtDataCenterRootGroup();

        GroupMetadata group = groupService.findByRef(Group.refByName()
            .dataCenter(DE_FRANKFURT)
            .name("Archive")
        );

        assert group.getName().equalsIgnoreCase("archive");
    }

    @Test
    public void testFindGroupByName() {
        mockDataCentersMetadata();
        mockFrankfurtDataCenterRootGroup();

        List<GroupMetadata> groups = groupService.find(new GroupFilter()
            .dataCenterIn(DE_FRANKFURT)
            .nameContains("Group1")
        );

        assert groups.get(0).getName().equals("Group1");
    }

    @Test
    public void testFindGroupByNameInAllDataCenters() {
        mockDataCentersMetadata();
        mockFrankfurtDataCenterRootGroup();

        List<GroupMetadata> groups = groupService.find(new GroupFilter()
            .nameContains("Group1")
        );

        assert groups.get(0).getName().equals("Group1");
    }

}
