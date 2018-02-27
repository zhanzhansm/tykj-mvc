package com.tykj.mvc.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tykj.mvc.annotation.Controller;
import com.tykj.mvc.annotation.RequestMapping;
import com.tykj.mvc.annotation.Service;
import com.tykj.mvc.util.MappingInfo;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2018-02-27 17:25
 **/
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String basePackage = "com.tykj.mvc";

    List<Class<?>> clazzes = new ArrayList<Class<?>>();

    Map<String, Object> instanceMap = new HashMap<String, Object>();

    Map<String, Object> controllerMap = new HashMap<String, Object>();

    Map<String, MappingInfo> mappingMap = new HashMap<String, MappingInfo>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        initPackage(basePackage);
        initInstance();
        initAutowired();
        initMappingInfo();
    }

    void initInstance() {

        for (Class<?> clazz : clazzes) {
            try {
                if (clazz.isAnnotationPresent(Controller.class)) {
                    controllerMap.put(clazz.getName(), clazz.newInstance());
                }
                if (clazz.isAnnotationPresent(Service.class)) {
                    instanceMap.put(clazz.getName(), clazz.newInstance());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initPackage(String scanPackage) {

        String path = scanPackage.replace(".", "/");
        URL url = this.getClass().getClassLoader().getResource(path);
        System.out.println(path);
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                initPackage(scanPackage + "." + file.getName());
            } else {
                try {
                    String className = scanPackage + "." + file.getName().replace(".class", "");
                    clazzes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    void initAutowired() {

    }

    void initMappingInfo() {

        String url = "";
        RequestMapping mapping = null;
        Set<Entry<String, Object>> entrySet = controllerMap.entrySet();
        for (Entry<String, Object> entry : entrySet) {
            Class<?> clazz = entry.getValue().getClass();
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                mapping = clazz.getAnnotation(RequestMapping.class);
                url = mapping.value();
            }
            Method[] methods = clazz.getMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    mapping = method.getAnnotation(RequestMapping.class);
                    url = url + mapping.value();
                    mappingMap.put(url, new MappingInfo(url, clazz, method, null));
                }
            }
        }

    }

    public static void main(String[] args) throws Exception {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.initPackage("com.tykj.mvc");
        dispatcherServlet.initInstance();
        System.out.println(dispatcherServlet.instanceMap);
    }
}
