package com.demo.oauth2.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.security.SecurityUtil;
import org.apache.oltu.oauth2.as.request.OAuthAuthzRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.demo.oauth2.entity.Client;
import com.demo.oauth2.entity.User;
import com.demo.oauth2.service.OAuthService;
import com.demo.oauth2.service.UserService;




@Controller
public class AuthorizeController {

	@Autowired
	private OAuthService oAuthService;
	
	@Autowired
	private UserService userService;
	
	
	@RequestMapping("/authorize")
	public Object authorize(Model model,HttpServletRequest request) throws OAuthSystemException, OAuthProblemException {
		 //构建OAuth 授权请求
        OAuthAuthzRequest oauthRequest = new OAuthAuthzRequest(request);
        //验证客户端信息id,key,还可以是回传地址的校验
        
        //检查传入的客户端id是否正确
        if (!oAuthService.checkClientId(oauthRequest.getClientId())) {
            OAuthResponse response =
                    OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                            .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                            .setErrorDescription("clientId 不合法")
                            .buildJSONMessage();
            return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
        }
        
		//如果用户没有登录则需要跳转到登录界面
		if(!login(request))
		{
			model.addAttribute("client",new Client());
			return "oauth2Login";
		}
		return null;
	}
	
	 private boolean login(HttpServletRequest request) {
	        if("get".equalsIgnoreCase(request.getMethod())) {
	            request.setAttribute("error","非法的请求");
	            return false;
	        }
	        String username = request.getParameter("username");
	        String password = request.getParameter("password");

	        if(StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
	            request.setAttribute("error","登录失败:用户名或密码不能为空");
	            return false;
	        }
	        try {
	            // 写登录逻辑
	            User user = userService.findByUsername(username);
	            if(user!=null){
	                if(!userService.checkUser(username,password,user.getSalt(),user.getPassword())){
	                    request.setAttribute("error","登录失败:密码不正确");
	                    return false;
	                }else{
	                    return true;
	                }
	            }else{
	                request.setAttribute("error","登录失败:用户名不正确");
	                return false;
	            }
	        } catch (Exception e) {
	            request.setAttribute("error", "登录失败:" + e.getClass().getName());
	            return false;
	        }
	    }
}
