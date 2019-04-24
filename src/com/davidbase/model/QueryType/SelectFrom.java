package com.davidbase.model.QueryType;

import com.davidbase.model.DavidBaseError;
import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;
import com.davidbase.utils.DavisBaseFileHandler;

import java.util.List;

/**
 * This class represents a Select Query
 * supports a single table.
 */
public class SelectFrom implements QueryBase {

    private String tableName;
    private String column;
    private Condition condition;

    @Override
    public QueryResult Execute() {
        try {
            new DavisBaseFileHandler().readFromFile(tableName);
        }catch(Exception e){
            e.printStackTrace();
            throw new DavidBaseError("Error while creating new table");
        }
        return null;
    }

    public void setColumn(String column){
        this.column=column;
    }

    public void setCondition(Condition condition){
        this.condition=condition;
    }

    public void setTableName(String tableName){
        this.tableName=tableName;
    }

    public String getColumn(){
        return column;
    }

    public Condition getCondition(){
        return condition;
    }
    
    public String getTableName(){
        return tableName;
    }
}
