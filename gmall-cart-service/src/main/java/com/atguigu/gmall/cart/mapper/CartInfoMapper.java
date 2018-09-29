package com.atguigu.gmall.cart.mapper;

import com.atguigu.gmall.bean.CartInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by Administrator on 2018/9/18/018.
 */
public interface CartInfoMapper extends Mapper<CartInfo> {
    void deleteCartsById(@Param("join") String join);
//    CartInfo selectOneByExample(Example e);
//
//    int updateByPrimaryKeySelective(CartInfo cartInfoDb);
//
//    int insertSelective(CartInfo cartInfo);

//    List<CartInfo> select(CartInfo cartInfo);
}
