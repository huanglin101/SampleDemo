package com.demo.oauth2.utils;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;



public class Untils {
	 public Untils() {
	    }

	    private String redirectUri;
	    
	    @Autowired
	    private void setServletContext(ServletContext ctx) {
	        String contextPath = "";//ctx.getContextPath()
	        redirectUri = "http://localhost:8080" + contextPath + "/redirect";
	    }
}
