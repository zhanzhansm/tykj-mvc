package com.tykj.mvc.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2018-02-27 17:25
 **/
public class DispatcherServlet extends HttpServlet {

    private final String basePackage = "com.tykj.mvc";

    List<Class<?>> clazzes = new ArrayList<Class<?>>();

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
        super.init(config);
        System.out.println("---------------------------------");
        try {
            initPackage(basePackage);
            System.out.println("dddddddddddddddddddddddd");
            for (Class<?> clazz : clazzes) {
                System.out.println(clazz.getSimpleName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initPackage(String basePackage) throws Exception {

        String path = "/" + basePackage.replace(".", "/");
        URL url = this.getClass().getClassLoader().getResource(path);
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                basePackage = basePackage + "." + file.getName();
                initPackage(basePackage);
            }else {
                String className = basePackage + "."+file.getName().replace(".class","");
                clazzes.add(Class.forName(className));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.initPackage("com.tykj.mvc");
    }
}
