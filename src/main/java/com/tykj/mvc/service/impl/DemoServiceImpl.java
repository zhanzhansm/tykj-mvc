package com.tykj.mvc.service.impl;

import com.tykj.mvc.annotation.Service;
import com.tykj.mvc.service.IDemoService;

@Service
public class DemoServiceImpl implements IDemoService{

	@Override
	public String query(String params) {
		return "DemoServiceImpl : " + params;
	}
}
