package com.davidbase.service;

import static com.davidbase.utils.DavisBaseConstants.*;
import com.davidbase.model.QueryType.CreateTable;
import com.davidbase.utils.DavisBaseCatalogHandler;
import com.davidbase.model.DavidBaseValidationException;
import com.davidbase.model.QueryType.CreateDatabase;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class Test{

    static Scanner scanner = new Scanner(System.in).useDelimiter(";");

    static DavidBaseCommandValidator commandValidator = new DavidBaseCommandValidator();
    static DavidBaseCommandExecutor commandExecutor = new DavidBaseCommandExecutor();

    public static boolean isExit = false;

    public static void main(String[] args){
        //DavidBaseCommandValidator dbValidator= new DavidBaseCommandValidator();
        //boolean result= dbValidator.isValid("123");
        //System.out.println(result);

        /* Variable to collect user input from the prompt */
        String userCommand = "";

        while(!isExit) {
            System.out.print(prompt);
            /* toLowerCase() renders command case insensitive */
            userCommand = scanner.next().replace("\n", " ").replace("\r", "").trim().toLowerCase();
            // userCommand = userCommand.replace("\n", "").replace("\r", "");
            parseUserCommand(userCommand);
            System.out.println("Exiting...");
        }
        System.out.println("Exiting...");

    }

    public static void parseUserCommand (String userCommand) {

        /* commandTokens is an array of Strings that contains one token per array element
         * The first token can be used to determine the type of command
         * The other tokens can be used to pass relevant parameters to each command-specific
         * method inside each case statement */
        // String[] commandTokens = userCommand.split(" ");
        ArrayList<String> commandTokens = new ArrayList<String>(Arrays.asList(userCommand.split(" ")));


        /*
         *  This switch handles a very small list of hardcoded commands of known syntax.
         *  You will want to rewrite this method to interpret more complex commands.
         */
        switch (commandTokens.get(0)) {
            case "select":
                System.out.println("CASE: SELECT");
                //parseQuery(userCommand);
                break;
            case "show":
                if (commandTokens.get(1).compareToIgnoreCase("database")==0){
                    parseShowDatabase(userCommand);
                }
                else{
                    parseShowTable(userCommand);
                }
                break;
            case "drop":
                System.out.println("CASE: DROP");
                //dropTable(userCommand);
                break;
            case "create":
                System.out.println("CASE: CREATE");
                if (commandTokens.get(1).compareToIgnoreCase("table")==0){
                    parseCreateTable(userCommand);
                }
                else{
                    parseCreateDatabase(userCommand);
                }
                break;
            case "update":
                System.out.println("CASE: UPDATE");
                //parseUpdate(userCommand);
                break;
            case "help":
                //help();
                break;
            case "version":
                //displayVersion();
                break;
            case "exit":
                isExit = true;
                break;
            case "quit":
                isExit = true;
            default:
                System.out.println("I didn't understand the command: \"" + userCommand + "\"");
                break;
        }
    }

    private static void parseCreateTable(String createTableString) {
        System.out.println("createTable");
        try {
            //CreateTable queryObject = commandValidator.isValidCreateTable(createTableString);
            //List<String> columns=queryObject.getColumns();
            // for(int i=0; i<columns.size();i++){
            //     System.out.println(columns.get(i));
            // }
            //QueryResult result = commandExecutor.executeQuery(queryObject);
            //System.out.println("Rows affected: " + result.getRowsAffected());
        }catch(DavidBaseValidationException e) {
            System.out.println(e.getErrorMsg());
            //throw new DavidBaseError("Create table command in not valid.");
        //}catch(Exception e) {
            //throw new DavidBaseError("Error while executing command.");
        }
    }

    private static void parseCreateDatabase(String createDataBaseString) {
        try {
            //CreateDatabase queryObject = commandValidator.isValidDatabase(createDataBaseString);
            //System.out.println(queryObject.databaseName);
        }catch(DavidBaseValidationException e) {
            System.out.println(e.getErrorMsg());
        }
    }

    private static void parseShowDatabase(String showDB) {
        try {
            //boolean isTrue = commandValidator.isValidShowDB(showDB);
            //System.out.println(queryObject.databaseName);
        }catch(DavidBaseValidationException e) {
            System.out.println(e.getErrorMsg());
        }
    }

    private static void parseShowTable(String showTable) {
        try {
            //boolean isTrue = commandValidator.isValidShowTable(showTable);
            //System.out.println(queryObject.databaseName);
        }catch(DavidBaseValidationException e) {
            System.out.println(e.getErrorMsg());
        }
    }


}