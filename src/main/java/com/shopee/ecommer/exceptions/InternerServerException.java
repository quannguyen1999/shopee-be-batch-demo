package com.shopee.ecommer.exceptions;

public class InternerServerException extends RuntimeException {

    public InternerServerException(String exception) {
        super(exception);
    }
}