package com.atguigu.gmall.manage.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.util.FileUploadUtil;
import com.atguigu.gmall.service.AttrService;
import com.atguigu.gmall.service.SpuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by Administrator on 2018/9/6/006.
 */
@Controller
public class SpuController {
    @Reference
    SpuService spuService;

    @RequestMapping("getSpuInfoByCatalog3Id")
    @ResponseBody
    public List<SpuInfo> getSpuInfoByCatalog3Id(String catalog3Id){//获取spu属性列表
        List<SpuInfo> spuInfoByCatalog3Id = spuService.getSpuInfoByCatalog3Id(catalog3Id);
//        for (SpuInfo spuInfo:spuInfoByCatalog3Id) {
//            System.out.println(spuInfo.getSpuName());
//        }
        return spuInfoByCatalog3Id;
    }

    @RequestMapping("saveSpu")
    @ResponseBody
    public String saveSpu(SpuInfo spuInfo){
        SpuInfo spuInfo1 = new SpuInfo();
        spuInfo1.setSpuName(spuInfo.getSpuName());
        spuInfo1.setDescription(spuInfo.getDescription());
        String spuInfoId = spuService.insert(spuInfo1);
        System.out.println(spuInfoId);
        List<SpuSaleAttr> spuSaleAttrList = spuInfo.getSpuSaleAttrList();
        for (SpuSaleAttr spuSaleAttr:spuSaleAttrList) {
            spuService.insert(spuSaleAttr);
//            System.out.println(spuSaleAttr.getSaleAttrName());
        }
        List<SpuImage> spuImageList = spuInfo.getSpuImageList();
        for (SpuImage spuImage:spuImageList) {
            spuService.insert(spuImage);
//            System.out.println(spuImage.getImgName());
        }
        return "success";
    }
    @RequestMapping("fileUpload")
    @ResponseBody
    public String fileUpload(@RequestParam("file") MultipartFile file){
        // 调用fdfs的上传工具
        String imgUrl = FileUploadUtil.uploadImage(file);
        return imgUrl;
    }
    @RequestMapping("spuImageList")
    @ResponseBody
    public List<SpuImage> spuImageList(String spuId){
        List<SpuImage> spuImages =  spuService.spuImageList(spuId);
//        for (SpuImage spuImage : spuImages) {
//            System.out.println(spuImage.getImgName()+" "+spuImage.getImgUrl());
//        }
        return spuImages;
    }

    @RequestMapping(value = "spuSaleAttrList",method = RequestMethod.POST)
    @ResponseBody
    public List<SpuSaleAttr> getAttrListByCtg3(String spuId) {
        System.out.println(spuId);
        List<SpuSaleAttr> spuSaleAttrs = spuService.selectBySpuId(spuId);


        return spuSaleAttrs;
    }
    @RequestMapping("uploadSuccess")
    @ResponseBody
    public String uploadSuccess(SpuInfo spuInfo){
        //spuService.saveSpu(spuInfo);
        System.out.println("uploadSuccess");
        return "success";
    }
    @RequestMapping("upLoadToData")
    @ResponseBody
    public String upLoadToData(SpuInfo spuInfo){
        System.out.println("upLoadToData");
        return "success";
    }
}
