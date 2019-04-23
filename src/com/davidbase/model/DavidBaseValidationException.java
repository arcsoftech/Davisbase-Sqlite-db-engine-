package com.davidbase.model;

public class DavidBaseValidationException extends Exception {

    private static final long serialVersionUID = 1112573555769113630L;
    String error_msg;

    public DavidBaseValidationException(String msg){
        this.error_msg=msg;
    }
    
    public String getErrorMsg(){
        return error_msg;

    }
}
