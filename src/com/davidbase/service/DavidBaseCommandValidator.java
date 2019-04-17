package com.davidbase.service;

import com.davidbase.model.impl.CreateTable;
import com.davidbase.model.DavidBaseValidationException;

/**
 * This class validates the user commands to avoid errors while execution.
 */
public class DavidBaseCommandValidator {

    /**
     *
     * @param userCommand
     * @return true if the commands is good for execution.
     */
    public boolean isValid(String userCommand){
        return true;
    }

    /**
     * validate a create table query
     * @param userCommand
     * @return
     */
    public CreateTable isValidCreateTable(String userCommand) throws DavidBaseValidationException {
        // checks for tables and columns etc, and returns a valid CreateTable object else throw exception
        return new CreateTable();
    }
}
