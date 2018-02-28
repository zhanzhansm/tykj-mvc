package com.tykj.mvc.servlet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.Map.Entry;
import java.util.logging.Logger;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tykj.mvc.annotation.Autowired;
import com.tykj.mvc.annotation.Controller;
import com.tykj.mvc.annotation.RequestMapping;
import com.tykj.mvc.annotation.Service;
import com.tykj.mvc.controller.DemoController;
import com.tykj.mvc.util.MappingInfo;
import com.tykj.mvc.util.RequestUtil;
import com.tykj.mvc.util.ResponseUtil;
import com.tykj.mvc.util.StringUtils;

/**
 * @author lukw
 * @email 13507615840@163.com
 * @create 2018-02-27 17:25
 **/
public class DispatcherServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final String basePackage = "com.tykj.mvc";

    private List<Class<?>> clazzes = new ArrayList<Class<?>>();

    private Map<String, Object> instanceMap = new HashMap<String, Object>();

    private Map<String, Object> controllerMap = new HashMap<String, Object>();

    private Map<String, MappingInfo> mappingMap = new HashMap<String, MappingInfo>();

    private Logger logger = Logger.getLogger("dispatcherServlet");

    private List<String> ignoreList = new ArrayList<>();


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String url = request.getRequestURI();
        Map<String, Object> parameter = RequestUtil.initRequestParam(request);
        MappingInfo mappingInfo = mappingMap.get(url);
        if (ignoreList.contains(StringUtils.getSuffix(url))) {
            return;
        }
        String msg = "";
        if (mappingInfo == null) {
            msg = "404";
        } else {
            try {
                msg = mappingInfo.getMethod().invoke(mappingInfo.getClazz(), url).toString();
            } catch (Exception e) {
                e.printStackTrace();
                msg = "500";
            }
        }
        ResponseUtil.writeMsg(response, msg);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doGet(request, response);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        initPackage(basePackage);
        initInstance();
        initAutowired();
        initMappingInfo();
        String ignores = config.getInitParameter("ignores");
        Arrays.asList(ignores.split("\\,")).forEach(ignore -> ignoreList.add(ignore));
    }

    void initInstance() {

        for (Class<?> clazz : clazzes) {
            try {
                if (clazz.isAnnotationPresent(Controller.class)) {
                    controllerMap.put(clazz.getName(), clazz.newInstance());
                }
                if (clazz.isAnnotationPresent(Service.class)) {
                    Object instance = clazz.newInstance();
                    instanceMap.put(clazz.getName(), instance);
                    Class<?>[] interfaces = clazz.getInterfaces();
                    if (interfaces != null) {
                        Arrays.asList(interfaces).forEach(interfaceClazz -> {
                            instanceMap.put(interfaceClazz.getName(), instance);
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void initPackage(String scanPackage) {

        String path = scanPackage.replace(".", "/");
        URL url = this.getClass().getClassLoader().getResource(path);
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

        instanceMap.forEach((key, instance) -> {

            Field[] fields = instance.getClass().getDeclaredFields();
            try {
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Autowired.class)) {
                        logger.info(field.getName());
                        field.set(instance, instanceMap.get(field.getType().getName()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        controllerMap.forEach((key, instance) -> {

            Field[] fields = instance.getClass().getDeclaredFields();
            try {
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Autowired.class)) {
                        field.set(instance, instanceMap.get(field.getType().getName()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    void initMappingInfo() {

        String baseUrl = "";
        RequestMapping mapping;
        Set<Entry<String, Object>> entrySet = controllerMap.entrySet();
        for (Entry<String, Object> entry : entrySet) {
            Class<?> clazz = entry.getValue().getClass();
            if (clazz.isAnnotationPresent(RequestMapping.class)) {
                mapping = clazz.getAnnotation(RequestMapping.class);
                baseUrl = mapping.value();
            }
            Method[] methods = clazz.getMethods();
            for (int i = 0; i < methods.length; i++) {
                Method method = methods[i];
                if (method.isAnnotationPresent(RequestMapping.class)) {
                    mapping = method.getAnnotation(RequestMapping.class);
                    String methodUrl = baseUrl + mapping.value();
                    if (mappingMap.containsKey(methodUrl)) {
                        throw new RuntimeException(methodUrl + ": 相同url已存在");
                    }
                    mappingMap.put(methodUrl, new MappingInfo(methodUrl, controllerMap.get(clazz.getName()), method, null));
                }
            }
        }

    }

    public static void main(String[] args) throws Exception {

        DispatcherServlet dispatcherServlet = new DispatcherServlet();
        dispatcherServlet.initPackage("com.tykj.mvc");
        dispatcherServlet.initInstance();
        dispatcherServlet.initAutowired();
        dispatcherServlet.initMappingInfo();
        System.out.println(dispatcherServlet.controllerMap);
        System.out.println(dispatcherServlet.instanceMap);
        System.out.println(dispatcherServlet.mappingMap);
        DemoController demoController = (DemoController) dispatcherServlet.controllerMap.get("com.tykj.mvc.controller.DemoController");
        System.out.println(demoController.query("DemoController"));
    }
}
