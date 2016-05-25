package com.centurylink.cloud.sdk.server.services.dsl.servers.operations;

import com.centurylink.cloud.sdk.server.services.dsl.domain.server.ScriptPackageConfig;
import com.centurylink.cloud.sdk.tests.recorded.WireMockFileSource;
import com.centurylink.cloud.sdk.tests.recorded.WireMockMixin;
import org.testng.annotations.Test;

import static com.centurylink.cloud.sdk.tests.TestGroups.RECORDED;

/**
 * Created by khomco on 5/23/16.
 */
@Test(groups = RECORDED)
public class ExecutePackageTest extends AbstractServerOperationTest implements WireMockMixin {

    @Test
    @WireMockFileSource("/package/execute")
    public void testExecutePackage() {
        ScriptPackageConfig scriptPackageConfig = new ScriptPackageConfig()
                .packageId("a103ec29-b590-4374-8b89-378965837abd")
                .parameter("key", "value");

        serverService.executePackage(scriptPackageConfig, server)
                .waitUntilComplete();
    }
}
