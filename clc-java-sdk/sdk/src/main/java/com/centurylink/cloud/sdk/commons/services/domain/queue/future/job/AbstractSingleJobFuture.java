package com.centurylink.cloud.sdk.commons.services.domain.queue.future.job;

import com.centurylink.cloud.sdk.commons.services.domain.queue.future.job.waiting.TimeoutInterceptor;
import com.centurylink.cloud.sdk.commons.services.domain.queue.future.job.waiting.WaitingLoop;
import com.centurylink.cloud.sdk.base.services.SdkThreadPool;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * @author Ilya Drabenia
 */
public abstract class AbstractSingleJobFuture implements JobFuture {

    @Override
    public void waitUntilComplete() {
        waitingLoop().get();
    }

    @Override
    public void waitUntilComplete(Duration timeout) {
        waitingLoop()
            .onIterationStarted(
                new TimeoutInterceptor(timeout, operationInfo())
            )
            .get();
    }

    @Override
    public CompletableFuture<Void> waitAsync() {
        CompletableFuture<Void> future = new CompletableFuture<>();

        SdkThreadPool.get().execute(() -> {
            try {
                waitUntilComplete();
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        });

        return future;
    }

    protected abstract String operationInfo();

    public abstract WaitingLoop waitingLoop();
}