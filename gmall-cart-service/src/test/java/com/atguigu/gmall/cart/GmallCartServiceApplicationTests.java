package com.atguigu.gmall.cart;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.CartInfo;
import com.atguigu.gmall.cart.mapper.CartInfoMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallCartServiceApplicationTests {


	@Test
	public void contextLoads() {
		List<CartInfo> allCartInfos = new ArrayList<>();
		String a=JSON.toJSONString(allCartInfos);//测试tojsonString方法
        System.out.println(a+"可以为空");
    }
	@Test
	public void contextLoads2() {
        CartInfo cartInfo = new CartInfo();
        cartInfo.setUserId("2");

//        System.out.println(i);
    }

}
