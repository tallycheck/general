package com.taoswork.tallybook.general.solution.exception;

/**
 * Created by Gao Yuan on 2016/2/23.
 */
public class UnImplementedException extends RuntimeException {
    public UnImplementedException() {
    }

    public UnImplementedException(String message) {
        super(message);
    }

    public UnImplementedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnImplementedException(Throwable cause) {
        super(cause);
    }
}
