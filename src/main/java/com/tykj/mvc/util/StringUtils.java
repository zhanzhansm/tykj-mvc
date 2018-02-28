package com.tykj.mvc.util;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2018-02-28 14:32
 **/
public class StringUtils {

    public static String getSuffix(String name){

        return name.substring(name.lastIndexOf("."));
    }
}
