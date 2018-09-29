package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseCatalog1;
import com.atguigu.gmall.bean.BaseCatalog2;
import com.atguigu.gmall.bean.BaseCatalog3;
import com.atguigu.gmall.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2018/9/4/004.
 */
@Controller
public class CatalogController {
    @Reference
    CatalogService catalogService;
    @RequestMapping("/getCatalog1")
    @ResponseBody
    public  List<BaseCatalog1> getCatalog1() {
        List<BaseCatalog1> catalog1 = catalogService.getCatalog1();
        return catalog1;
    }
    @RequestMapping("/getCatalog2")
    @ResponseBody
    public  List<BaseCatalog2> getCatalog2(String catalog1Id) {

        System.out.println(catalog1Id);
        List<BaseCatalog2> catalog2 = catalogService.getCatalog2(catalog1Id);
        return catalog2;
    }
    @RequestMapping("/getCatalog3")
    @ResponseBody
    public  List<BaseCatalog3> getCatalog3(String catalog2Id) {
        System.out.println("getCatalog3");
        List<BaseCatalog3> catalog3 = catalogService.getCatalog3(catalog2Id);
        return catalog3;
    }

}
