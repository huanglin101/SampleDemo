//package com.demo;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.beans.factory.FactoryBean;
//import org.springframework.http.HttpRequest;
//import org.springframework.http.MediaType;
//import org.springframework.http.client.ClientHttpRequestExecution;
//import org.springframework.http.client.ClientHttpRequestInterceptor;
//import org.springframework.http.client.ClientHttpResponse;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.stereotype.Component;
//import org.springframework.web.client.RestTemplate;
//
//import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
//
//@Component
//public class ResetTemplateFactory implements FactoryBean<RestTemplate> {
//
//	@Override
//	public RestTemplate getObject() throws Exception {
//		RestTemplate template = new RestTemplate();
//		List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
//		FastJsonHttpMessageConverter jsonConverter = new FastJsonHttpMessageConverter();
//		List<MediaType> supportedMediaTypes = new ArrayList<>();
//		supportedMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//		jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
//
//		messageConverters.add(jsonConverter);
//		template.setMessageConverters(messageConverters);
//		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
//		interceptors.add(new ClientHttpRequestInterceptor() {
//
//			@Override
//			public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
//					throws IOException {
//				request.getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
//				return execution.execute(request, body);
//			}
//		});
//		template.setInterceptors(interceptors);
//		return template;
//	}
//
//	@Override
//	public Class<?> getObjectType() {
//		return RestTemplate.class;
//	}
//
//	@Override
//	public boolean isSingleton() {
//		return true;
//	}
//
//}
