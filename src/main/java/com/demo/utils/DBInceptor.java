package com.demo.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.apache.ibatis.executor.statement.BaseStatementHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Intercepts({
	@Signature(type = StatementHandler.class, method = "prepare", args = { Connection.class, Integer.class }) })
@Component
public class DBInceptor implements Interceptor{

	private Logger logger=LoggerFactory.getLogger(this.getClass());

	@SuppressWarnings("unchecked")
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		Object target = invocation.getTarget();
		BaseStatementHandler statementHandler = null;
		//statement 的两种实现 BaseStatementHandler，RoutingStatementHandler，一般情况下 mybatis是RoutingStatementHandler实现的
		if (target instanceof RoutingStatementHandler) {
			RoutingStatementHandler routingStatementHandler = (RoutingStatementHandler) target;
			Field field = FieldUtils.getField(RoutingStatementHandler.class, "delegate", true);
			statementHandler = (BaseStatementHandler) FieldUtils.readField(field, routingStatementHandler, true);
		}
		else if (target instanceof BaseStatementHandler) {
			statementHandler = (BaseStatementHandler) target;
		}  
		
		if( statementHandler!=null&&statementHandler.getParameterHandler()!=null){
		
			//获取参数						
			Map<String, Object> map = (Map<String, Object>) statementHandler.getParameterHandler().getParameterObject();
			try {
				String userId=String.valueOf(map.get("userID"));
				BoundSql boundSql = statementHandler.getBoundSql();
				String sql = boundSql.getSql();
				String newSQL = sql.replaceAll("1=1", String.format(" user_id='%s' AND 2=2",userId));				
				Field sqlField = FieldUtils.getField(BoundSql.class, "sql", true);
				FieldUtils.writeField(sqlField, boundSql, newSQL);		
				
			} catch (Exception e) {
				logger.error("拦截sql失败",e);
			}									
		}		
	
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);   	
	}

	@Override
	public void setProperties(Properties properties) {
		
	}
	
	
	private AnnotationInceptor getProperties(BaseStatementHandler statementHandler) throws Exception{		
	
		AnnotationInceptor annotation=null;
		
		Field field = FieldUtils.getField(BaseStatementHandler.class, "mappedStatement", true);
		MappedStatement mappedStatement = (MappedStatement) FieldUtils.readField(field, statementHandler, true);
		String id = mappedStatement.getId();
		
		int lastIndexOf = id.lastIndexOf('.');
		String className = id.substring(0, lastIndexOf);//执行sql的类名
		String methodName = id.substring(lastIndexOf + 1, id.length());//执行sql的方法名
		Method[] declaredMethods = Class.forName(className).getDeclaredMethods();
		for (Method method : declaredMethods) {
			if (method.getName().equals(methodName)) {
				annotation = method.getAnnotation(AnnotationInceptor.class);
				if (annotation != null) {					
					break;
				}
			}
		}
		return annotation;
	}
	
}
