package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.List;

/**
 * Created by Administrator on 2018/9/6/006.
 */
@Controller
public class SkuController {
    @Reference
    SkuService skuInfoService;
    @RequestMapping("getSkuInfoByCatalog3Id")
    @ResponseBody
    public List<SkuInfo> getSkuInfoByCatalog3Id(String catalog3Id){
        List<SkuInfo> skuInfoByCatalog3Id = skuInfoService.getSkuInfoByCatalog3Id(catalog3Id);
        return skuInfoByCatalog3Id;
    }

    @RequestMapping("getSkuInfoById")
    @ResponseBody
    public SkuInfo getSkuInfoById(String skuId){
        SkuInfo skuById = skuInfoService.getSkuById("2");
        return skuById;
    }



}

