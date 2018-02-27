package com.tykj.mvc;

import com.sun.org.apache.xpath.internal.SourceTree;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.midi.Soundbank;
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
    }

    void initPackage(String basePackage) throws Exception {

        URL url = this.getClass().getClassLoader().getResource("/" + basePackage.replace("\\.", "/"));
        System.out.println(url);
        File dir = new File(url.getFile());

        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                basePackage = basePackage + "." + file.getName();
                initPackage(basePackage);
            } else {
                clazzes.add(Class.forName(basePackage + file.getName()));
            }
        }
    }

    public static void main(String[] args) throws Exception {
        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.initPackage("com.tykj.mvc");
    }
}
