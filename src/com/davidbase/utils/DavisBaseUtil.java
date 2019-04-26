package com.davidbase.utils;


import com.davidbase.model.DavidBaseError;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static com.davidbase.model.QueryType.Condition.*;

public class DavisBaseUtil {

    public static final DateTimeFormatter dateTimeFomatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss");
    public static final DateTimeFormatter dateFomatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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

    public static boolean isValidDateTime(String dateStr){
        try {
            LocalDateTime formatDateTime = LocalDateTime.parse(dateStr, dateTimeFomatter);
            return true;
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static LocalDateTime getValidDateTime(String dateStr)  {
        if(isValidDateTime(dateStr)){
            return LocalDateTime.parse(dateStr, dateTimeFomatter);
        }else{
            throw new DavidBaseError("Date time is not valid");
        }
    }

    public static boolean isValidDate(String dateStr){
        try {
            LocalDate formatDate = LocalDate.parse(dateStr, dateFomatter);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    public static LocalDate getValidDate(String dateStr)  {
        if(isValidDate(dateStr)){
            return LocalDate.parse(dateStr, dateFomatter);
        }else{
            throw new DavidBaseError("Date is not valid");
        }
    }

    public static LocalDate getValidDate(Long date)  {
        return Instant.ofEpochSecond(date).atZone(ZoneOffset.UTC).toLocalDate();
    }

    public static LocalDateTime getValidDateTime(Long date)  {
        return Instant.ofEpochSecond(date).atZone(ZoneOffset.UTC).toLocalDateTime();
    }

    public static String getDateTimeInString(LocalDateTime dateTime)  {
        return dateTime.format(dateTimeFomatter);
    }

    public static String getDateInString(LocalDate dateTime)  {
        return dateTime.format(dateFomatter);
    }

    public static boolean conditionCompare(LocalDate validDate, LocalDate validDate1, short condition) {
        switch(condition){
            case EQUALS:return validDate.equals(validDate1);
            case GREATER_THAN:
            case GREATER_THAN_EQUALS:
                return validDate.isAfter(validDate1);
            case LESS_THAN :
            case LESS_THAN_EQUALS: return validDate.isBefore(validDate1);
        }
        return false;
    }

    public static boolean conditionCompare(LocalDate validDate, LocalDateTime validDate1, short condition) {
        switch(condition){
            case EQUALS:return validDate.equals(validDate1.toLocalDate());
            case GREATER_THAN:
            case GREATER_THAN_EQUALS:
                return validDate.isAfter(validDate1.toLocalDate());
            case LESS_THAN :
            case LESS_THAN_EQUALS: return validDate.isBefore(validDate1.toLocalDate());
        }
        return false;
    }

    public static boolean conditionCompare(LocalDateTime validDate, LocalDate validDate1, short condition) {
        switch(condition){
            case EQUALS:return validDate.toLocalDate().equals(validDate1);
            case GREATER_THAN:
            case GREATER_THAN_EQUALS:
                return validDate.toLocalDate().isAfter(validDate1);
            case LESS_THAN :
            case LESS_THAN_EQUALS: return validDate.toLocalDate().isBefore(validDate1);
        }
        return false;
    }

    public static boolean conditionCompare(LocalDateTime validDate, LocalDateTime validDate1, short condition) {
        switch(condition){
            case EQUALS:return validDate.equals(validDate1);
            case GREATER_THAN:
            case GREATER_THAN_EQUALS:
                return validDate.isAfter(validDate1);
            case LESS_THAN :
            case LESS_THAN_EQUALS: return validDate.isBefore(validDate1);
        }
        return false;
    }

    public static void main(String[] args){
        String str ="2016-03-23";
        LocalDate date = getValidDate(str);
        System.out.println(getDateInString(date));
        long dateLong = date.toEpochSecond(LocalTime.MIN,ZoneOffset.UTC);
        System.out.println(dateLong);
        LocalDate dteNw = getValidDate(dateLong);
        System.out.println(getDateInString(dteNw));


        str ="2016-03-23_13:52:23";
        LocalDateTime dateTime = getValidDateTime(str);
        System.out.println(getDateTimeInString(dateTime));
        dateLong = dateTime.toEpochSecond(ZoneOffset.UTC);
        System.out.println(dateLong);
        LocalDateTime dttimeeNw = getValidDateTime(dateLong);
        System.out.println(getDateTimeInString(dttimeeNw));


    }
}
