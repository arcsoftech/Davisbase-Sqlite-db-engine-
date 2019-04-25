package com.davidbase.model.QueryType;

import com.davidbase.utils.DataType;
import com.davidbase.utils.DavisBaseConstants;
import com.davidbase.model.QueryType.QueryBase;
import com.davidbase.model.QueryType.QueryResult;
import com.davidbase.utils.DavisBaseFileHandler;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import static com.davidbase.utils.DavisBaseConstants.*;

/**
 * Class represents a Drop Table query
 */
public class DropTable implements QueryBase {

    public String databaseName;
    public String tableName;

    public DropTable(String databaseName, String tableName) {
        this.databaseName = databaseName;
        this.tableName = tableName;
    }

    @Override
    public QueryResult execute() {

        DeleteFrom deleteQuery = new DeleteFrom(databaseName,SYSTEM_TABLES_TABLENAME);

        List<Condition> delConds = new ArrayList<Condition>();
        delConds.add(new Condition(1, Condition.EQUALS, DataType.TEXT,tableName));

        deleteQuery.setConditions(delConds);
        int result = deleteQuery.execute().getRowsAffected();
        if(result>0){
            File file = new File(DavisBaseFileHandler.getDatabasePath(DEFAULT_DATA_DIRNAME) + "/" + tableName + FILE_EXT);
            file.delete();
        }
        return new QueryResult(result);
    }
}
