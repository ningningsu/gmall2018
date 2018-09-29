package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseAttrValue;
import com.atguigu.gmall.bean.BaseSaleAttr;
import com.atguigu.gmall.service.AttrService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2018/9/5/005.
 */
@Controller
public class AttrController {

    @Reference
    AttrService attrService;

    @RequestMapping("/getAttrListByCtg3")
    @ResponseBody
    public List<BaseAttrInfo> getAttrListByCtg3(String catalog3Id) {
        System.out.println("getAttrListByCtg3");
        List<BaseAttrInfo> baseAttrInfos =attrService.getAttrListByCtg3(catalog3Id);
//        for (BaseAttrInfo base:baseAttrInfos) {
//            System.out.println(base.getAttrName());
//        }
        return baseAttrInfos;
    }
@RequestMapping("baseSaleAttrList")
@ResponseBody
public List<BaseSaleAttr> baseSaleAttrList(){
    List<BaseSaleAttr> baseSaleAttrs = attrService.getbaseSaleAttrList();
//    for (BaseSaleAttr baseSaleAttr:baseSaleAttrs) {
//        System.out.println(baseSaleAttr.getName());
//    }
    return baseSaleAttrs;
}
    @RequestMapping("/saveAttr")
    @ResponseBody
    public  String saveAttr(BaseAttrInfo baseAttrInfo) {
        System.out.println("getCatalog3");
        String catalog3Id = baseAttrInfo.getCatalog3Id();
        String attrName = baseAttrInfo.getAttrName();
        BaseAttrInfo baseAttrInfo1 = new BaseAttrInfo();
        baseAttrInfo1.setCatalog3Id(catalog3Id);
        baseAttrInfo1.setAttrName(attrName);
        attrService.saveAttr(baseAttrInfo1);
        return "出来";
    }
}
