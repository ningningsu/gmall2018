package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SkuSaleAttrValue;
import com.atguigu.gmall.bean.SpuSaleAttr;

import java.util.List;

/**
 * Created by Administrator on 2018/9/6/006.
 */
public interface SkuService {

    List<SkuInfo> getSkuInfoByCatalog3Id(String catalog3Id);//通过catalog3Id获取SkuInfo的数据列表

    SkuInfo getSkuById(String skuId);

    List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(String spuId, String skuId);

    List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId);

    List<SkuInfo> getAllSkuInfo();
}
