package com.davidbase.service;

import com.davidbase.model.impl.CreateTable;
import com.davidbase.model.DavidBaseValidationException;
import com.davidbase.model.DavidBaseCatalogHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.*;
//import java.util.DavidBaseCatalogHandler;
import com.davidbase.utils.DavidBaseCatalogHandler;
 
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
        //parse command
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));
        
        
        //check if the table has been existed. do not know if need to check column?
        DavidBaseCatalogHandler catalog_handler= new DavidBaseCatalogHandler();
        boolean isExist=catalog_handler.tableExists("databaseName", commandTokens.get(2)); //??figure out database name
        
        // checks for tables and columns etc, and returns a valid CreateTable object else throw exception
        //boolean isExist=false;
        if (isExist!=false){
            throw new DavidBaseValidationException("error");
        }
        else{
            CreateTable ctable = new CreateTable();
            ctable.setTableName(commandTokens.get(2));
            return ctable;
        }
        
    }
}
