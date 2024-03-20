package com.shopee.ecommer.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

//trả về status sẽ là Not_Found
//phải extends RuntimeException thì mới throw được
@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String exception) {
        super(exception);
    }
}