package com.tykj.mvc.util;

import java.lang.reflect.Method;

public class MappingInfo {

	private String url;
	
	private Object clazz;
	
	private Method method;
	
	private Object[] args;
	
	public MappingInfo(String url, Object clazz, Method method,Object[] args) {
		this.url = url;
		this.clazz = clazz;
		this.method = method;
		this.args = args;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Object getClazz() {
		return clazz;
	}

	public void setClazz(Object clazz) {
		this.clazz = clazz;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}
	
}
