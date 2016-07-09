package com.taoswork.tallycheck.general.solution.reference;

/**
 * Created by Gao Yuan on 2015/11/15.
 */
public class EntityFetchException extends Exception {
    public EntityFetchException() {
    }

    public EntityFetchException(String message) {
        super(message);
    }

    public EntityFetchException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityFetchException(Throwable cause) {
        super(cause);
    }
}
