package com.taoswork.tallybook.general.solution.exception;

/**
 * Created by Gao Yuan on 2016/2/22.
 */
public class UnexpectedException extends RuntimeException {
    public UnexpectedException() {
    }

    public UnexpectedException(String message) {
        super(message);
    }

    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnexpectedException(Throwable cause) {
        super(cause);
    }
}
