package com.davidbase.model;

public class DavidBaseError extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public DavidBaseError(String error) {
        super(error);
    }
    public DavidBaseError(Exception e){
        super(e);
    }
}