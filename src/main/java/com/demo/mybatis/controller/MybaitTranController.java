package com.demo.mybatis.controller;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import com.demo.entity.TCustomer;
import com.demo.mapper.TCustomerMapper;

/**
 * mybatis 事务
 * @author hlin
 *
 */
public class MybaitTranController {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private TCustomerMapper customerMapper;
	
	@Autowired
    private PlatformTransactionManager ptm;
	   
	private Logger logger=LoggerFactory.getLogger(this.getClass());
	
	@Transactional
	public void tranTest() {
		
		try {
			TCustomer tCustomer=new TCustomer();
			customerMapper.insert(tCustomer);
		
		} catch (Exception e) {
			logger.error("error",e);
		}
	}
	
	public void sqlSessionTest() {
		SqlSession	session =sqlSessionFactory.openSession(false);//禁止自动提交开启事物
		try {
			TCustomerMapper sessMapper=session.getMapper(TCustomerMapper.class);
			TCustomer tCustomer=new TCustomer();
			sessMapper.insert(tCustomer);
			session.commit();
		} catch (Exception e) {
			session.rollback();
			logger.error("error",e);
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
			logger.error("error",e);
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
					logger.error("error",e);
				}
				
			}
		});
	}
}
