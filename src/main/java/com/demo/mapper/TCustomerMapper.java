package com.demo.mapper;

import com.demo.entity.TCustomer;
import com.demo.entity.TCustomerCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TCustomerMapper {
    int countByExample(TCustomerCriteria example);

    int deleteByExample(TCustomerCriteria example);

    int deleteByPrimaryKey(String id);

    int insert(TCustomer record);

    int insertSelective(TCustomer record);

    List<TCustomer> selectByExample(TCustomerCriteria example);

    TCustomer selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TCustomer record, @Param("example") TCustomerCriteria example);

    int updateByExample(@Param("record") TCustomer record, @Param("example") TCustomerCriteria example);

    int updateByPrimaryKeySelective(TCustomer record);

    int updateByPrimaryKey(TCustomer record);
}