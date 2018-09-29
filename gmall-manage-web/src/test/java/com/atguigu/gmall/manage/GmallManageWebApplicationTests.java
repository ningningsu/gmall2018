package com.atguigu.gmall.manage;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseCatalog2;
import com.atguigu.gmall.service.CatalogService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageWebApplicationTests {
	@Reference
	CatalogService catalogService;
	@Test
	public void contextLoads() {
		System.out.println("getCatalog2");
		List<BaseCatalog2> catalog2 = catalogService.getCatalog2("1");
		System.out.println(catalog2);



	}

}
