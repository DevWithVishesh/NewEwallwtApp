package com.icsd.exceptionhand;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.icsd.apiresponce.ApiResponse;


import lombok.extern.slf4j.Slf4j;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(IcsdException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse> handleIcsdException(IcsdException ex) {
        ApiResponse apiResponse = new ApiResponse(400, "something went wrong", ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<ApiResponse> handleEntityAlreadyExistsException(EntityAlreadyExistException ex, WebRequest webRequest) {
        log.info("handleEntityAlreadyExistsException fired");
        return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponse> handleResourecNotFoundExecption(ResourceNotFoundException ex, WebRequest webRequest) {
        log.info("handleResourecNotFoundExecption fired");

        return new ResponseEntity<>(new ApiResponse(HttpStatus.NOT_ACCEPTABLE.value(), ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }
}