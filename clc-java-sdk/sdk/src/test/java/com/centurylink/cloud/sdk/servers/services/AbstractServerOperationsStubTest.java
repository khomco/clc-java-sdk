package com.centurylink.cloud.sdk.servers.services;

import com.centurylink.cloud.sdk.core.commons.client.QueueClient;
import com.centurylink.cloud.sdk.servers.AbstractServersSdkTest;
import com.centurylink.cloud.sdk.servers.client.ServerClient;
import com.centurylink.cloud.sdk.servers.client.domain.server.Details;
import com.centurylink.cloud.sdk.servers.client.domain.server.metadata.ServerMetadata;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.GroupRef;
import com.centurylink.cloud.sdk.servers.services.domain.server.filters.ServerFilter;
import com.centurylink.cloud.sdk.servers.services.domain.server.refs.ServerRef;

import com.google.inject.Inject;

public abstract class AbstractServerOperationsStubTest extends AbstractServersSdkTest {

    protected ServerRef server1;
    protected ServerRef server2;

    protected ServerFilter serverFilter;

    @Inject
    ServerService serverService;

    protected ServerMetadata loadServerMetadata(ServerRef server) {
        ServerMetadata metadata = serverService.findByRef(server);
        assertNotNull(metadata);

        return metadata;
    }

    protected Details loadServerDetails(ServerRef server) {
        ServerMetadata metadata = loadServerMetadata(server);
        assertNotNull(metadata.getDetails());

        return metadata.getDetails();
    }

    protected void assertThatServerHasStatus(ServerRef server, String status) {
        assertEquals(loadServerMetadata(server).getStatus(), status);
    }

    protected void assertThatServerPowerStateHasStatus(ServerRef server, String status) {
        assertEquals(loadServerDetails(server).getPowerState(), status);
    }

    protected void assertThatMaintenanceFlagIs(ServerRef server, Boolean expectedResult) {
        assertEquals(loadServerDetails(server).getInMaintenanceMode(), expectedResult);
    }

    protected abstract void powerOnServer();

    protected abstract void powerOffServer();

    protected abstract void pauseServer();

    protected abstract void shutDownServer();

    protected abstract void stopServerMaintenance();

    protected abstract void startServerMaintenance();

    protected abstract void archiveServer();

    protected abstract void createServerSnapshot();

    protected abstract void restoreServer(GroupRef group, ServerRef server);

    protected abstract void resetServer();

    protected abstract void rebootServer();

    public void testPowerOff() {
        testShutDown();
        powerOffServer();

        assertThatServerPowerStateHasStatus(server1, "stopped");
        assertThatServerPowerStateHasStatus(server2, "stopped");
    }

    public void testPause() {
        pauseServer();

        assertThatServerPowerStateHasStatus(server1, "paused");
        assertThatServerPowerStateHasStatus(server2, "paused");
        powerOnServer();
    }

    public void testShutDown() {
        testPause();
        shutDownServer();

        assertThatServerPowerStateHasStatus(server1, "stopped");
        assertThatServerPowerStateHasStatus(server2, "stopped");
        powerOnServer();
    }

    public void testPowerOn() {
        testPowerOff();
        powerOnServer();

        assertThatServerPowerStateHasStatus(server1, "started");
        assertThatServerPowerStateHasStatus(server2, "started");
    }

    public void testStartMaintenance() {
        testPowerOn();
        startServerMaintenance();

        assertThatMaintenanceFlagIs(server1, true);
        assertThatMaintenanceFlagIs(server2, true);
    }

    public void testStopMaintenance() {
        testStartMaintenance();
        stopServerMaintenance();

        assertThatMaintenanceFlagIs(server1, false);
        assertThatMaintenanceFlagIs(server2, false);
    }

    public void testCreateSnapshot() {
        testStopMaintenance();

        assertEquals(loadServerDetails(server1).getSnapshots().size(), 0);
        assertEquals(loadServerDetails(server2).getSnapshots().size(), 0);

        createServerSnapshot();

        assertEquals(loadServerDetails(server1).getSnapshots().size(), 1);
        assertEquals(loadServerDetails(server2).getSnapshots().size(), 1);
    }

    public void testRebootServer() {
        testCreateSnapshot();

        rebootServer();

        assertThatServerPowerStateHasStatus(server1, "started");
        assertThatServerPowerStateHasStatus(server2, "started");
    }

    public void testReset() {
        testRebootServer();

        resetServer();

        assertThatServerPowerStateHasStatus(server1, "started");
        assertThatServerPowerStateHasStatus(server2, "started");
    }


    public void testArchive() {
        testReset();

        assertThatServerHasStatus(server1, "active");
        assertThatServerHasStatus(server2, "active");

        archiveServer();

        assertThatServerHasStatus(server1, "archived");
        assertThatServerHasStatus(server2, "archived");
    }

    public abstract void runChainTests();
}
