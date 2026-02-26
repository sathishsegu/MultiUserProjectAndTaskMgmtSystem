package com.flm.mgmtsystem.exception;

public class UnAuthorizedActionException extends RuntimeException {

    public UnAuthorizedActionException(String message) {
        super(message);
    }
}
