package com.davidbase.model.impl;

import com.davidbase.model.DavidBaseError;

public class DavidBaseValidationException extends Exception {

    DavidBaseValidationException(String msg){
        super(msg);
    }
}
