package com.shivam.aiecommercebackend.exception;

public class InSufficientStockException extends RuntimeException{
    public InSufficientStockException(String msg){
        super(msg);
    }
}
