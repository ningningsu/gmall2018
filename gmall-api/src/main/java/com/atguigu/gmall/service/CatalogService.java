package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.BaseCatalog1;
import com.atguigu.gmall.bean.BaseCatalog2;
import com.atguigu.gmall.bean.BaseCatalog3;

import java.util.List;

/**
 * Created by Administrator on 2018/9/4/004.
 */
public interface CatalogService {
    List<BaseCatalog1> getCatalog1();//获取BaseCatalog1的数据列表

    List<BaseCatalog2> getCatalog2(String catalog1Id);//通过catalog1Id获取BaseCatalog2的数据列表

    List<BaseCatalog3> getCatalog3(String catalog2Id);//通过catalog2Id获取BaseCatalog3的数据列表
}
