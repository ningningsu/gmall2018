package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.SpuImage;
import com.atguigu.gmall.bean.SpuInfo;
import com.atguigu.gmall.bean.SpuSaleAttr;
import java.util.List;

/**
 * Created by Administrator on 2018/9/10/010.
 */
public interface SpuService {
    void insert(SpuImage spuImage);//向SpuImage表中插入一条数据

    List<SpuImage> spuImageList(String spuId);//通过spuid查询所有spuid符合要求的spuImage数据查出并返回

    List<SpuInfo> getSpuInfoByCatalog3Id(String catalog3Id);//通过catalog3Id查询所有catalog3Id符合要求的spuImage数据查出并返回

    String insert(SpuInfo spuInfo);//向SpuInfo表中插入一条数据并返回该条数据的主键（）

    void insert(SpuSaleAttr spuSaleAttr);//向SpuSaleAttr表中插入一条数据

    List<SpuSaleAttr> selectBySpuId(String spuId);//通过spuid查询所有spuid符合要求的SpuSaleAttr数据查出并返回
}
