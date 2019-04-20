package com.davidbase.model;

public class DavidBaseValidationException extends Exception {

    String error_msg;

    public DavidBaseValidationException(String msg){
        this.error_msg=msg;
    }
    
    public String getErrorMsg(){
        return error_msg;

    }
}
