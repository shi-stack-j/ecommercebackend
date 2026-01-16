package com.shivam.aiecommercebackend.exception;

public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String msg){
        super(msg);
    }
}
