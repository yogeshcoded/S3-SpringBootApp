package com.bt.demo.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BucketNotFoundException extends Exception {
    public BucketNotFoundException(String message) {
        super(message);
    }

    public BucketNotFoundException() {
    }
}
