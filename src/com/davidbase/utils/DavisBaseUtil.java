package com.davidbase.utils;


import static com.davidbase.model.QueryType.Condition.*;

public class DavisBaseUtil {

    public static boolean conditionCompare(Byte val1, Byte val2, short condition){
        switch(condition){
            case EQUALS:return val1.byteValue()==val2.byteValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }
    
    public static boolean conditionCompare(String val1, String val2, short condition){
        switch(condition){
            case EQUALS:return val1.equalsIgnoreCase(val2);
        }
        return false;
    }
    
    

    public static boolean conditionCompare(Byte val1, Short val2, short condition){
        switch(condition){
            case EQUALS:return val1.byteValue()==val2.shortValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Byte val1, Integer val2, short condition){
        switch(condition){
            case EQUALS:return val1.byteValue()==val2.intValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Byte val1, Long val2, short condition){
        switch(condition){
            case EQUALS:return val1.byteValue()==val2.longValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Short val1, Byte val2, short condition){
        switch(condition){
            case EQUALS:return val1.byteValue()==val2.byteValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Short val1, Short val2, short condition){
        switch(condition){
            case EQUALS:return val1.shortValue()==val2.shortValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Short val1, Integer val2, short condition){
        switch(condition){
            case EQUALS:return val1.shortValue()==val2.intValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Short val1, Long val2, short condition){
        switch(condition){
            case EQUALS:return val1.shortValue()==val2.longValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Integer val1, Byte val2, short condition){
        switch(condition){
            case EQUALS:return val1.intValue()==val2.byteValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Integer val1, Short val2, short condition){
        switch(condition){
            case EQUALS:return val1.intValue()==val2.shortValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Integer val1, Integer val2, short condition){
 
        switch(condition){
            case EQUALS:return val1.intValue()==val2.intValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Integer val1, Long val2, short condition){
        switch(condition){
            case EQUALS:return val1.intValue()==val2.longValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Long val1, Byte val2, short condition){
        switch(condition){
            case EQUALS:return val1.longValue()==val2.byteValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Long val1, Short val2, short condition){
        switch(condition){
            case EQUALS:return val1.longValue()==val2.shortValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Long val1, Integer val2, short condition){
        switch(condition){
            case EQUALS:return val1.longValue()==val2.intValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Long val1, Long val2, short condition){
        switch(condition){
            case EQUALS:return val1.longValue()==val2.longValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Float val1, Float val2, short condition){
        switch(condition){
            case EQUALS:return val1.floatValue()==val2.floatValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Float val1, Double val2, short condition){
        switch(condition){
            case EQUALS:return val1.floatValue()==val2.doubleValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Double val1, Float val2, short condition){
        switch(condition){
            case EQUALS:return val1.doubleValue()==val2.floatValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }

    public static boolean conditionCompare(Double val1, Double val2, short condition){
        switch(condition){
            case EQUALS:return val1.doubleValue()==val2.doubleValue();
            case GREATER_THAN: return val1>val2;
            case LESS_THAN : return val1<val2;
            case GREATER_THAN_EQUALS: return val1>=val2;
            case LESS_THAN_EQUALS: return val1<=val2;
        }
        return false;
    }
}
