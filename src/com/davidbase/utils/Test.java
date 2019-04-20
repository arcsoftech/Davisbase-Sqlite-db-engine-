package com.davidbase.utils;

import java.util.ArrayList;
import java.util.List;

public class Test {
    public static void main(String[] args){
        Byte b1 = (byte)2;
        Integer i1 = 5;
        List<Object> list = new ArrayList<>();
        list.add(b1);
        list.add(i1);
        for(Object o1:list){
            System.out.println(o1);
        }
        System.out.println((int)list.get(0)>(byte)9);
    }
}
