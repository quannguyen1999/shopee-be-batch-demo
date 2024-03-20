package com.shopee.ecommer.exceptions;

import com.shopee.ecommer.constants.MessageErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedRequestException extends RuntimeException {

    public UnauthorizedRequestException(MessageErrors exception) {
        super(exception.toString());
    }

}