package com.shivam.aiecommercebackend.exception;

public class AlreadyReviewedException extends RuntimeException{
    public AlreadyReviewedException(String msg){
        super(msg);
    }
}
