package com.nhansen.bookproject.database;

public class UserReadWriteException extends RuntimeException {

    public UserReadWriteException (String message) {
        super(message);
    }
}
