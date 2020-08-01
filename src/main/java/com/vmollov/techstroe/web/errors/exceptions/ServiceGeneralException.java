package com.vmollov.techstroe.web.errors.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static com.vmollov.techstroe.constants.Errors.*;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = GENERAL_SERVICE_ERROR)
public class ServiceGeneralException extends RuntimeException {

    public ServiceGeneralException(String message) {
        super(message);
    }
}