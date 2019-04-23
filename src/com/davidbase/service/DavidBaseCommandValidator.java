package com.davidbase.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import com.davidbase.utils.DataType;

import com.davidbase.model.PageComponent.InternalColumn;
import com.davidbase.model.DavidBaseValidationException;
import com.davidbase.model.QueryType.Condition;
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
        List<InternalColumn> columns_list=new ArrayList<InternalColumn>();
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
            boolean is_pri=false;
            //get primary key
            for(int j=0;j<temp.size();j++){
                if(temp.get(j).toLowerCase().contains("primary")==true){
                    pri=temp.get(0);
                    is_pri=true;
                }
            }
            columns_list.add(new InternalColumn(temp.get(0), DataType.getTypeFromText(temp.get(1).toUpperCase().trim()),is_pri,false));


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

    public InsertInto isValidInsertInto(String userCommand, String currentDB)throws DavidBaseValidationException{
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));
        // DavisBaseCatalogHandler catalog_handler= new DavisBaseCatalogHandler();
        // boolean isExist=catalog_handler.tableExists(currentDB, commandTokens.get(2));
        // if (isExist==false){
        //     throw new DavidBaseValidationException("The table does not Exist");
        // } 

        int first_open_bracket_index = userCommand.toLowerCase().indexOf("(");
        int last_close_bracket_index = userCommand.toLowerCase().lastIndexOf(")");

        String string_inside_brackets=userCommand.substring(first_open_bracket_index + 1, last_close_bracket_index).trim();
        ArrayList<String> columns_substrings = new ArrayList<String>(Arrays.asList(string_inside_brackets.split("values")));
        //System.out.println(columns_substrings.get(0));
        //System.out.println(columns_substrings.get(1));

        List<String> columns=new ArrayList<String>();
        List<String> values=new ArrayList<String>();

        String columns_string=columns_substrings.get(0).replaceAll("[)]","");
        String values_string=columns_substrings.get(1).replaceAll("[(]","");
        columns_string=columns_string.trim();
        values_string=values_string.trim();

        ArrayList<String> columns_list = new ArrayList<String>(Arrays.asList(columns_string.split(",")));
        ArrayList<String> values_list = new ArrayList<String>(Arrays.asList(values_string.split(",")));
        
        for(int i=0; i<columns_list.size();i++){
            columns.add(columns_list.get(i).trim());
        }

        for(int i=0; i<values_list.size();i++){
            values.add(values_list.get(i).trim());
        }

        InsertInto queryObject=new InsertInto(DavidBaseManager.getCurrentDB(),commandTokens.get(2),columns, values);
        return queryObject;
        // for(int i=0; i<columns.size();i++){
        //     System.out.println(columns.get(i));
        // }

        // for(int i=0; i<values.size();i++){
        //     System.out.println(values.get(i));
        // }
        





        
    }


    public boolean isValidSelectFrom(String userCommand)throws DavidBaseValidationException{
    	 ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));
        int size=commandTokens.size();
         //check if the second key word is "table"
         String userCommandlower=userCommand.toLowerCase();
         if(!userCommandlower.contains("from"))
        	 throw new DavidBaseValidationException("Incorrect SELECT statement");
        
        
        //check if table exists ---- Qi
        DavisBaseCatalogHandler catalog_handler= new DavisBaseCatalogHandler();
        boolean isExist=catalog_handler.databaseExists(commandTokens.get(2)); 

        if (isExist!=false){
            throw new DavidBaseValidationException("The table does not Exist");
        }  
        // else{
        //     DropDatabase dropDB= new DropDatabase(commandTokens.get(2));
        //     return dropDB;
        // }
        
        // int where_index = userCommand.toLowerCase().indexOf("where");
        // int semicolon_index=userCommand.toLowerCase().indexOf(";");
        // String string_where_clause=userCommand.substring(where_index + 1, semicolon_index-1).trim();
        // ArrayList<String> columns = new ArrayList<String>(Arrays.asList(string_where_clause.split(" ")));

        // for(int i=0; i<columns.size();i++){
        //     System.out.println(columns.get(i));
        // }
        int from_index = userCommand.toLowerCase().indexOf("from");
        //String attribute = userCommand.substring("select".length(), from_index).trim();
        String rest = userCommand.substring(from_index + "from".length());

        int where_index = rest.toLowerCase().indexOf("where");
        if(where_index == -1) {
            String tableName = rest.trim();
        }

        String tableName = rest.substring(0, where_index).trim();
        String condition_string = rest.substring(where_index + "where".length()).trim();

        //parse condition
        Condition condition=parse_condition(condition_string, tableName);


        String str="=";
        short cnd=0;
        switch(str){
            case "=": cnd = Condition.EQUALS;break;
            case ">" : cnd = Condition.GREATER_THAN;break;
            case "<" : cnd = Condition.LESS_THAN;break;
            case ">=" : cnd = Condition.GREATER_THAN_EQUALS;break;
            case "<=" : cnd = Condition.LESS_THAN_EQUALS;break;
        }



        return true;
    }

    public Condition parse_condition(String condition_String, String tableName) throws DavidBaseValidationException{
        short cnd=-1;
        String op="";
        
        if(condition_String.contains("<=")){
            cnd=Condition.LESS_THAN_EQUALS;
            op="<=";
        }

        else if(condition_String.contains(">=")){
            cnd= Condition.GREATER_THAN_EQUALS;
            op=">=";

        }

        else if(condition_String.contains(">")){
            cnd= Condition.GREATER_THAN;
            op=">";

        }

        else if(condition_String.contains("<")){
            cnd= Condition.LESS_THAN;
            op="<";

        }

        else if(condition_String.contains("=")){
            cnd= Condition.EQUALS;
            op="=";

        }
        else {
            cnd=-1;
        }

        if (cnd==-1){
            throw new DavidBaseValidationException("No Operator");
        }

        String[] strings;
        String column;
        String value;
        //DataType dataType;  Need to get data type of the value 
        Condition condition;
        strings = condition_String.split(op);
        if(strings.length != 2) {
            throw new DavidBaseValidationException("Unrecongnized Condition");           
        }

        column = strings[0].trim();
        value=strings[2].trim();
        //dataType=fetchAllTableColumnDataTypes(tableName);
        condition = Condition.CreateCondition(0,column, null, (Object)value);

        switch (cnd){
            case 0:
                condition = parse(conditionString, operator, "=");
                break;
            case 1:
                condition = parse(conditionString, operator, "<");
                break;
            case 2:
                condition = parse(conditionString, operator, ">");
                break;          
            case 3:
                condition = parse(conditionString, operator, "<=");
                break;
            case 4:
                condition = getConditionInternal(conditionString, operator, ">=");
                break;
            
        }




        return null;

        
    }



}
