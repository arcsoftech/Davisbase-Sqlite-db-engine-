package com.davidbase.service;

import com.davidbase.model.QueryType.CreateTable;
import com.davidbase.model.DavidBaseValidationException;
import com.davidbase.utils.DavidBaseCatalogHandler;
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
        
        //check if the second key word is "table"
        if(commandTokens.get(1).compareToIgnoreCase("table")!=0){
            throw new DavidBaseValidationException("You are not creating table, command is not recognizable");
        }
        //check if the table has been existed. do not know if need to check column?
        DavidBaseCatalogHandler catalog_handler= new DavidBaseCatalogHandler();
        boolean isExist=catalog_handler.tableExists("databaseName", commandTokens.get(2)); //??figure out database name
        
        //parse and put all columns to a list
        List<String> columns_list=new ArrayList<String>();
        //table's primary key
        String pri=null;

        String column_indexes = userCommand.toLowerCase();
        int open_bracket_index = column_indexes.indexOf("(");
        int close_bracket_index = column_indexes.indexOf(")");

        String string_inside_brackets=userCommand.substring(open_bracket_index + 1, close_bracket_index).trim();
        ArrayList<String> columns_substrings = new ArrayList<String>(Arrays.asList(string_inside_brackets.split(",")));
        for(int i=0;i<columns_substrings.size();i++){
            String columns_substrings_trim=columns_substrings.get(i).trim();
            ArrayList<String> temp = new ArrayList<String>(Arrays.asList(columns_substrings_trim.split(" ")));
            
            //get primary key
             for(int j=0;j<temp.size();j++){
                if(temp.get(j).toLowerCase().contains("primary")==true){
                    pri=temp.get(0);
                }
             }
            
            //put all columns to a arraylist
            columns_list.add(temp.get(0));
        }

        // checks for tables and columns etc, and returns a valid CreateTable object else throw exception
        //boolean isExist=false;
        if (isExist!=false){
            throw new DavidBaseValidationException("The table has been already Existed");
        }  
        else{
            CreateTable ctable = new CreateTable();
            ctable.setTableName(commandTokens.get(2));
            ctable.setColumns(columns_list);
            ctable.setPrimaryKey(pri);
            //  for(int i=0; i<columns.size();i++){
            //      System.out.println(columns.get(i));
            //  }
            return ctable;
        }
        
    }
}
