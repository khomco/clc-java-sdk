package com.centurylink.cloud.sdk.server.services.dsl.servers.operations;

import com.centurylink.cloud.sdk.server.services.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
 * @author Aliaksandr Krasitski
 */
@Test(groups = RECORDED)
public class ServerCreateSnapshotTest extends AbstractServerOperationTest implements WireMockMixin {

    @Test
    @WireMockFileSource("/snapshot/create")
    public void testCreateSnapshotServer() {
        serverService.createSnapshot(server)
            .waitUntilComplete();

        ServerMetadata serverMetadata = serverService.findByRef(server);
        assert serverMetadata.getDetails().getSnapshots().size() == 1;
    }
}
