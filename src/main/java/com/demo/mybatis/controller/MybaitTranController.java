package com.demo.mybatis.controller;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.demo.entity.TCustomer;
import com.demo.mapper.CustomerMapper;

/**
 * mybatis 事务
 * @author hlin
 *
 */
public class MybaitTranController {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private CustomerMapper customerMapper;
	
	@Autowired
    private PlatformTransactionManager ptm;
	
	@Autowired 
	private DataSource dataSource;
	   
	
	@Transactional
	public void tranTest() {
		try {
			TCustomer tCustomer=new TCustomer();
			customerMapper.insert(tCustomer);
		
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public void sqlSessionTest() {
		SqlSession	session =sqlSessionFactory.openSession(false);//禁止自动提交开启事物
		try {
			CustomerMapper sessMapper=session.getMapper(CustomerMapper.class);
			TCustomer tCustomer=new TCustomer();
			sessMapper.insert(tCustomer);
			session.commit();
		} catch (Exception e) {
			session.rollback();
		}
		
		session.close();
	}
	
	public void platTranTest() {
		DefaultTransactionDefinition defTran=new DefaultTransactionDefinition();
		defTran.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		TransactionStatus status=ptm.getTransaction(defTran);
		try {
			TCustomer tCustomer=new TCustomer();
			customerMapper.insert(tCustomer);
			ptm.commit(status);	
		} catch (Exception e) {
			ptm.rollback(status);
		}
	}
	
	public void tranTemplateTest() {
		TCustomer tCustomer=new TCustomer();
	
		TransactionTemplate template=new TransactionTemplate();
		template.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		template.execute(new TransactionCallbackWithoutResult() {
			
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus status) {
				try {
					customerMapper.insert(tCustomer);	
				} catch (Exception e) {
					status.setRollbackOnly();
				}
				
			}
		});
	}
}
