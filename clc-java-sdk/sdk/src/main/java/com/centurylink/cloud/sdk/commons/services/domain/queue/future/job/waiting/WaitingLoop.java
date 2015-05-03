package com.centurylink.cloud.sdk.commons.services.domain.queue.future.job.waiting;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Ilya Drabenia
 */
public interface WaitingLoop extends Supplier<Void> {

    WaitingLoop onIterationStarted(Consumer<Void> listener);

    default WaitingLoop andThen(WaitingLoop otherLoop) {
        return new CompositeWaitingLoop(this, otherLoop);
    }

}