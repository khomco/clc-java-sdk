package com.centurylink.cloud.sdk.servers.services.domain.server.future;

import com.centurylink.cloud.sdk.common.services.client.QueueClient;
import com.centurylink.cloud.sdk.common.services.services.domain.queue.future.job.SingleJobFuture;
import com.centurylink.cloud.sdk.common.services.services.domain.queue.future.job.waiting.SingleWaitingLoop;
import com.centurylink.cloud.sdk.common.services.services.domain.queue.future.job.waiting.WaitingLoop;
import com.centurylink.cloud.sdk.servers.client.ServerClient;

/**
 * @author Ilya Drabenia
 */
public class PauseServerJobFuture extends SingleJobFuture {
    private final String serverId;
    private final ServerClient serverClient;

    public PauseServerJobFuture(String statusId, String serverId, QueueClient queueClient, ServerClient serverClient) {
        super(statusId, queueClient);
        this.serverId = serverId;
        this.serverClient = serverClient;
    }

    @Override
    public WaitingLoop waitingLoop() {
        return
            super
                .waitingLoop()
                .andThen(new SingleWaitingLoop(() ->
                    serverClient
                        .findServerById(serverId)
                        .getDetails()
                        .getPowerState()
                        .equals("paused")
                ));
    }
}