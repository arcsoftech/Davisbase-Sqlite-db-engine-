package com.davidbase.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.davidbase.model.DavidBaseValidationException;
import com.davidbase.model.QueryType.CreateDatabase;
import com.davidbase.model.QueryType.CreateTable;
import com.davidbase.model.QueryType.DropDatabase;
import com.davidbase.model.QueryType.DropTable;
import com.davidbase.model.QueryType.InsertInto;
import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;
import com.davidbase.model.QueryType.SelectFrom;


import com.davidbase.utils.DavisBaseCatalogHandler;
 
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
    public CreateTable isValidCreateTable(String userCommand, String current_DB) throws DavidBaseValidationException {
        //parse command
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));
        
        //check if the second key word is "table"
        if(commandTokens.get(1).compareToIgnoreCase("table")!=0){
            throw new DavidBaseValidationException("You are not creating table, command is not recognizable");
        }
        //check if the table has been existed. do not know if need to check column?
        DavisBaseCatalogHandler catalog_handler= new DavisBaseCatalogHandler();
        boolean isExist=catalog_handler.tableExists(current_DB, commandTokens.get(2)); //??figure out database name
        
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

    public CreateDatabase isValidDatabase(String userCommand)throws DavidBaseValidationException{
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));
        if(commandTokens.size()>3){
            throw new DavidBaseValidationException("Failed to create Database");
        }

        DavisBaseCatalogHandler catalog_handler= new DavisBaseCatalogHandler();
        boolean isExist=catalog_handler.databaseExists(commandTokens.get(2)); //??figure out database name
        if (isExist!=false){
            throw new DavidBaseValidationException("The database has been already Existed");
        }  
        else{
            CreateDatabase db=new CreateDatabase(commandTokens.get(2));
            return db;
        }
        
    }

    public boolean isValidShowDB(String userCommand)throws DavidBaseValidationException{
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));
        if(commandTokens.size()>3){
            throw new DavidBaseValidationException("Failed to show databases");
        }

        //ShowTable showTable=new ShowTable();
        return true;        
        
    }

    public boolean isValidShowTable(String userCommand)throws DavidBaseValidationException{
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));
        if(commandTokens.size()>3){
            throw new DavidBaseValidationException("Failed to show tables");
        }

        return true;        
        
    }

    public DropDatabase isValidDropDatabase(String userCommand)throws DavidBaseValidationException{
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));
        if(commandTokens.size()>3){
            throw new DavidBaseValidationException("Failed to drop database");
        }
        
        DavisBaseCatalogHandler catalog_handler= new DavisBaseCatalogHandler();
        boolean isExist=catalog_handler.databaseExists(commandTokens.get(2)); 

        if (isExist==false){
            throw new DavidBaseValidationException("The database does not Exist");
        }  
        else{
            DropDatabase dropDB= new DropDatabase(commandTokens.get(2));
            return dropDB;
        }

        
    }

    public DropTable isValidDropTable(String userCommand, String current_DB)throws DavidBaseValidationException{
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));
        if(commandTokens.size()>3){
            throw new DavidBaseValidationException("Failed to drop tables");
        }
        
        DavisBaseCatalogHandler catalog_handler= new DavisBaseCatalogHandler();
        boolean isExist=catalog_handler.tableExists(current_DB, commandTokens.get(2)); 

        if (isExist==false){
            throw new DavidBaseValidationException("The table does not Exist");
        }  
        else{
            DropTable dropTable= new DropTable(current_DB, commandTokens.get(2));
            return dropTable;
        }      
        
    }

    public InsertInto isValidInsertInto(String userCommand,String currentDB)throws DavidBaseValidationException{
             //dummy objects
       // InsertInto cd=new InsertInto();
        //return cd;
        
    }


    public boolean isValidSelectFrom(String userCommand)throws DavidBaseValidationException{
    	 ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));
         int size=commandTokens.size();
         //check if the second key word is "table"
         String userCommandlower=userCommand.toLowerCase();
         if(!userCommandlower.contains("from"))
        	 throw new DavidBaseValidationException("Incorrect SELECT statement");
        	//check if table exists ---- Qi
        return true;
    }



}
