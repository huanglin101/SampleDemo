package com.demo.oauth2.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.as.issuer.MD5Generator;
import org.apache.oltu.oauth2.as.request.OAuthTokenRequest;
import org.apache.oltu.oauth2.as.response.OAuthASResponse;
import org.apache.oltu.oauth2.common.OAuth;
import org.apache.oltu.oauth2.common.error.OAuthError;
import org.apache.oltu.oauth2.common.message.OAuthResponse;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class AccessTokenController {

	@RequestMapping("/access")
	public ModelAndView	 access(Model model) {
		return new ModelAndView("accesstoken");
	}
	
	@RequestMapping("/accessToken")
	public HttpEntity token(HttpServletRequest request) {
		try{
			//构建 Oauth请求
			OAuthTokenRequest oauthRequest=new OAuthTokenRequest(request);
			OAuthResponse response;
			//校验客户端id
			if(!oauthRequest.getClientId().equals("123"))
			{
				 response =
                        OAuthASResponse.errorResponse(HttpServletResponse.SC_BAD_REQUEST)
                                .setError(OAuthError.TokenResponse.INVALID_CLIENT)
                                .setErrorDescription("非法的clientId")
                                .buildJSONMessage();
                return new ResponseEntity(response.getBody(), HttpStatus.valueOf(response.getResponseStatus()));
			}
			
			String authCode=oauthRequest.getParam(OAuth.OAUTH_CODE);
			if(oauthRequest.getParam(OAuth.OAUTH_GRANT_TYPE).equals(GrantType.AUTHORIZATION_CODE.toString()))
			{
				
			}
			
			//生成token
			String token="123";
			
			response=OAuthASResponse
					.tokenResponse(HttpServletResponse.SC_OK)
					.setAccessToken(token)
					.setExpiresIn(String.valueOf(""))
					.buildJSONMessage();
			
			return new ResponseEntity<>(response.getBody(),HttpStatus.valueOf(response.getResponseStatus()));
		}
		catch (Exception e) {
			// TODO: handle exception
		}
		return null;
	}
}
