package com.demo.oauth2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/test")
public class TestController {

	@RequestMapping(value = "/testone", method = RequestMethod.GET)
	public String test1() {
		return "index";
	}
	
	@RequestMapping(value = "/testtwo", method = RequestMethod.POST)
	public String test2() {
		return "test2";
	}
}
