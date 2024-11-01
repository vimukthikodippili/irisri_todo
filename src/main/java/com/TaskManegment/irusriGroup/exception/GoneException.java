package com.TaskManegment.irusriGroup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

public class GoneException extends HttpClientErrorException {
    public GoneException(HttpStatus statusCode, String statusText) {
        super(statusCode, statusText);
    }
}
