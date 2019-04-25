package com.davidbase.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import com.davidbase.utils.DataType;

import com.davidbase.model.PageComponent.InternalColumn;
import com.davidbase.model.DavidBaseValidationException;
import com.davidbase.model.QueryType.Condition;
import com.davidbase.model.QueryType.CreateDatabase;
import com.davidbase.model.QueryType.CreateTable;
import com.davidbase.model.QueryType.DeleteFrom;
import com.davidbase.model.QueryType.DropDatabase;
import com.davidbase.model.QueryType.DropTable;
import com.davidbase.model.QueryType.InsertInto;
import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;
import com.davidbase.model.QueryType.SelectFrom;
import com.davidbase.model.QueryType.UpdateTable;
import com.davidbase.utils.DavisBaseCatalogHandler;

//import org.graalvm.compiler.nodes.InliningLog.UpdateScope;

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

        if(!userCommand.endsWith(")")){
            throw new DavidBaseValidationException("Missing )");
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
                    if(DataType.getTypeFromText(temp.get(1).toUpperCase().trim())==DataType.INT){
                        pri=temp.get(0);
                        is_pri=true;
                    }else{
                        throw new DavidBaseValidationException("Primary Key must be INT data type ");
                    }
                    
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
        boolean isExist=true;

        if (isExist==false){
            throw new DavidBaseValidationException("The table does not Exist");
        }
        else{
            DropTable dropTable= new DropTable(current_DB, commandTokens.get(2));
            return dropTable;
        }

    }

    public InsertInto isValidInsertInto(String userCommand, String currentDB)throws DavidBaseValidationException{
        //String userCommand_copy=userCommand;
        if(userCommand.contains("values")==false){
            throw new DavidBaseValidationException("Missing keyword Values");
        }
        
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));
        
        DavisBaseCatalogHandler catalog_handler= new DavisBaseCatalogHandler();
        boolean isExist=catalog_handler.tableExists(currentDB, commandTokens.get(2));
        if (isExist==false){
             throw new DavidBaseValidationException("The table does not Exist");
        } 

        int first_open_bracket_index = userCommand.indexOf("(");
        int last_close_bracket_index = userCommand.lastIndexOf(")");

        String string_inside_brackets=userCommand.substring(first_open_bracket_index + 1, last_close_bracket_index).trim();
        ArrayList<String> columns_substrings = new ArrayList<String>(Arrays.asList(string_inside_brackets.split("values")));
        //System.out.println(columns_substrings.get(0));
        //System.out.println(columns_substrings.get(1));

        List<String> columns=new ArrayList<String>();
        List values=new ArrayList();

        String columns_string=columns_substrings.get(0).replaceAll("[)]","");
        String values_string=columns_substrings.get(1).replaceAll("[(]","");
        columns_string=columns_string.trim();
        values_string=values_string.trim();

        System.out.println(columns_string);
        System.out.println(values_string);

        ArrayList<String> columns_list = new ArrayList<String>(Arrays.asList(columns_string.split(",")));
        ArrayList<String> values_list = new ArrayList<String>(Arrays.asList(values_string.split(",")));
        
        if(columns_list.size()!=values_list.size()){
            throw new DavidBaseValidationException("columns and corrosponding values do not match");
        }

        for(int i=0; i<columns_list.size();i++){
            columns.add(columns_list.get(i).trim());
        }

        for(int i=0; i<values_list.size();i++){
            if(values_list.get(i).contains("\"")){
            String str= values_list.get(i).trim().replace("\"", "");
                values.add(str);
            }else{
                values.add(Integer.parseInt(values_list.get(i).trim()));
            }
        }


        
        // for(int i=0; i<columns_list.size();i++){
        //     System.out.println(columns_list.get(i).trim());
        // }

        // for(int i=0; i<values_list.size();i++){
        //     System.out.println(values_list.get(i).trim());
        // }

        InsertInto queryObject=new InsertInto(DavidBaseManager.getCurrentDB(),commandTokens.get(2),columns, values);
        return queryObject;
        
    }


    public DeleteFrom isValidDeleteFrom(String userCommand) throws DavidBaseValidationException{
        String[] userParts = userCommand.toLowerCase().split(" ");
        String[] actualParts = "Delete From".toLowerCase().split(" ");

        for(int i=0;i<actualParts.length;i++){
            if(!actualParts[i].equals(userParts[i])){
                throw new DavidBaseValidationException("Unrecongnized query");

            }
        }


        String tableName = "";
            String condition_String = "";
            int index = userCommand.toLowerCase().indexOf("where");
             if(index == -1) {
                 tableName = userCommand.substring("Delete From".length()).trim();
                 throw new DavidBaseValidationException("Tell me which record you want to delete");

             }

            if(tableName.equals("")) {
                tableName = userCommand.substring("Delete From".length(), index).trim();
            }
            System.out.println(tableName);
            condition_String = userCommand.substring(index + "where".length()).trim();

            List column_condition=parse_condition(condition_String, tableName);
            String column=(String)column_condition.get(0);
            //System.out.println(column);
            Condition condition=(Condition)column_condition.get(1);
            //System.out.println(condition.getValue());
            List<Condition> condition_list=new ArrayList<Condition>();
            condition_list.add(condition);
            DeleteFrom delete_object= new DeleteFrom("", tableName);
            delete_object.setConditions(condition_list);
            //System.out.println(delete_object.conditions.get(0).getValue());
        return delete_object;
    }

    public UpdateTable isValidUpdateTable(String userCommand)throws DavidBaseValidationException{
        // String conditions = "";
        // int setIndex = userCommand.toLowerCase().indexOf("set");
        // if(setIndex == -1) {
        //     throw new DavidBaseValidationException("Where is the set key word");
        // }

        // String tableName = userCommand.substring(QueryHandler.UPDATE_COMMAND.length(), setIndex).trim();
        // String clauses = userCommand.substring(setIndex + "set".length());
        // int whereIndex = userCommand.toLowerCase().indexOf("where");
        // if(whereIndex == -1){
        //     IQuery query = QueryHandler.UpdateQuery(tableName, clauses, conditions);
        //     QueryHandler.ExecuteQuery(query);
        //     return;
        // }

        
        
        return null;
    }




    public SelectFrom isValidSelectFrom(String userCommand) throws DavidBaseValidationException{
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));
        int size=commandTokens.size();
        //check if the second key word is "table"
        String userCommandlower=userCommand.toLowerCase();
        if(!userCommandlower.contains("from"))
            throw new DavidBaseValidationException("Incorrect SELECT statement");


        //check if table exists ---- Qi
        DavisBaseCatalogHandler catalog_handler= new DavisBaseCatalogHandler();
        
        //System.out.print(commandTokens.get(2));
        
        
        boolean isExist=catalog_handler.tableExists("abc", commandTokens.get(2).trim());
        
        
        System.out.print(isExist);

        if (isExist==false){
            throw new DavidBaseValidationException("The table does not Exist");
        }
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
        List column_condition=parse_condition(condition_string, tableName);
        String column=(String)column_condition.get(0);
        Condition condition=(Condition)column_condition.get(1);

        SelectFrom select_object=new SelectFrom();
        select_object.setColumns(column);
        select_object.setCondition(condition);
        select_object.setTableName(tableName);

        System.out.print(select_object);
        return select_object;
    }

    public List parse_condition(String condition_String, String tableName) throws DavidBaseValidationException{
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
        value=strings[1].trim();
        // DavisBaseCatalogHandler d=new DavisBaseCatalogHandler();
        // HashMap<String, String> dataTypes= d.fetchAllTableColumnDataTypes("catalog","davisbase_columns");
        // Iterator iterator = dataTypes.keySet().iterator();
        // while (iterator.hasNext()){
        //     String key = (String)iterator.next();
        //     System.out.println(key+"="+dataTypes.get(key));
        // }
        // System.out.println(dataTypes.get(column));
        //DataType type= DataType.getTypeFromText(dataTypes.get(column));

        condition = Condition.CreateCondition((byte)0,cnd, DataType.getTypeFromText("INT"), (Object)value);

        List column_condition=new ArrayList();
        column_condition.add(column);
        column_condition.add(condition);



        return column_condition;


    }
}
