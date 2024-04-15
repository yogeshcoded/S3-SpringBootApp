package com.bt.demo.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import software.amazon.awssdk.services.s3.model.BucketAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BucketNotFoundException.class)
    public ResponseEntity<String> bucketNotFoundException(BucketNotFoundException bucketNotFoundException) {
        String message = bucketNotFoundException.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BucketAlreadyExistsException.class)
    public ResponseEntity<String> bucketAlreadyExistsException(BucketAlreadyExistsException bucketAlreadyExistsException) {
        String message = bucketAlreadyExistsException.getMessage();
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<String> objectNotFoundException(ObjectNotFoundException objectNotFoundException) {
        String message = objectNotFoundException.getMessage();
        return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
    }
}
