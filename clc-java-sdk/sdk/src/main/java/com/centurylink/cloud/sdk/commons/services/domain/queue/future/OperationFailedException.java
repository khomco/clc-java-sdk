package com.centurylink.cloud.sdk.commons.services.domain.queue.future;

import com.centurylink.cloud.sdk.base.services.ClcServiceException;

/**
 * @author Ilya Drabenia
 */
public class OperationFailedException extends ClcServiceException {

    public OperationFailedException() {
        super();
    }

    public OperationFailedException(String message) {
        super(message);
    }

    public OperationFailedException(String format, Object... arguments) {
        super(format, arguments);
    }
}