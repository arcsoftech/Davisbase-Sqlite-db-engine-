package com.davidbase.model;

public class DavidBaseError extends RuntimeException{

    public DavidBaseError(String error){
        super(error);
    }
    public DavidBaseError(Exception e){
        super(e);
    }
}