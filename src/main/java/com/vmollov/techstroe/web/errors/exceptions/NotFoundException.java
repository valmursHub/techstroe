package com.vmollov.techstroe.web.errors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.vmollov.techstroe.constants.Errors.*;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
