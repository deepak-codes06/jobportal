package com.deepak.jobportal.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

        // For handling the user not found exception
        @ExceptionHandler(UserNotFoundException.class)
        public ResponseEntity<Map<String , String>> handleUserNotFound(UserNotFoundException ex){
            return build(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        // For handling the job not found exception
        @ExceptionHandler(JobNotFoundException.class)
        public ResponseEntity<Map<String,String>> handleJobNotFound(JobNotFoundException ex){
            return build(HttpStatus.NOT_FOUND, ex.getMessage());
        }

        // For handling the exception if user is already applied the job but trying to apply again
        @ExceptionHandler(AlreadyAppliedException.class)
        public ResponseEntity<Map<String,String>> handleAlreadyApplied(AlreadyAppliedException ex){
            return build(HttpStatus.BAD_REQUEST, ex.getMessage());
        }

        // for handling any other kind of exception
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Map<String, String>> handleOther(Exception ex){
            return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }


        private ResponseEntity<Map<String, String>> build(HttpStatus status, String message){
            Map<String, String> body = new HashMap<>();
            body.put("message", message);
            return new ResponseEntity<>(body, status);
        }

}
