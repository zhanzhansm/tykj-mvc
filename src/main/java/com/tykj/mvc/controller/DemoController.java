package com.tykj.mvc.controller;

import com.tykj.mvc.annotation.Autowired;
import com.tykj.mvc.annotation.Controller;
import com.tykj.mvc.annotation.RequestMapping;
import com.tykj.mvc.service.IDemoService;

@Controller
@RequestMapping("/mvc")
public class DemoController {

	@Autowired
	private IDemoService demoService;
	
	@RequestMapping("/query")
	public void query(String name) {
		
		demoService.query(name);
	}
}
