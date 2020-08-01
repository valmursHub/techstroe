package com.vmollov.techstroe.web.errors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.vmollov.techstroe.constants.Errors.*;

@ResponseStatus(code = HttpStatus.FORBIDDEN, reason = UNAUTHORIZED)
public class UnauthorizedActionException extends RuntimeException {

    public UnauthorizedActionException(String message) {
        super(message);
    }
}
