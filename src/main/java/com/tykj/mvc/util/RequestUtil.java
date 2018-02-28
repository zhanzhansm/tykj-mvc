package com.tykj.mvc.util;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2018-02-28 11:10
 **/
public class RequestUtil {

    public static Map<String,Object> initRequestParam(HttpServletRequest request){

        Map<String,Object> params = new HashMap<>();
        Enumeration<String> enumeration =  request.getParameterNames();
        while (enumeration.hasMoreElements()){
            String key = enumeration.nextElement();
            params.put(key,request.getParameter(key));
        }
        return params;
    }
}
