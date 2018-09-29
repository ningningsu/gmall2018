package com.atguigu.gmall.item.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.bean.SkuSaleAttrValue;
import com.atguigu.gmall.bean.SpuSaleAttr;
import com.atguigu.gmall.service.SkuService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.ServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Created by Administrator on 2018/9/10/010.
 */
@Controller
public class ItemController {
    @Reference
    SkuService skuService;
    @RequestMapping("/test")
    public  String index(ServletRequest request){
            request.setAttribute("hello","你好");
            return "index";
    }
    @RequestMapping("/{skuId}.html")
    public String getSkuInfo(@PathVariable String skuId, ModelMap model){
        SkuInfo skuInfo = skuService.getSkuById(skuId);
        getSkuSaleAttrValueListBySpu(skuInfo,model);
        if(skuInfo==null){
            return null;
        }
        model.put("skuInfo",skuInfo);
        String spuId = skuInfo.getSpuId();
        List<SpuSaleAttr> spuSaleAttrs = skuService.getSpuSaleAttrListCheckBySku(spuId,skuId);// 查询销售属性列表
        model.put("spuSaleAttrListCheckBySku",spuSaleAttrs);
        return "item";
    }

    public void getSkuSaleAttrValueListBySpu(SkuInfo skuInfo,ModelMap model){
        System.out.println("getSkuSaleAttrValueListBySpu"+skuInfo.getSpuId());
        List<SkuSaleAttrValue> skuSaleAttrValueListBySpu = skuService.getSkuSaleAttrValueListBySpu(skuInfo.getSpuId());
        //把列表变换成 valueid1|valueid2|valueid3 ：skuId  的 哈希表 用于在页面中定位查询
        String valueIdsKey="";
        Map<String,String> valuesSkuMap=new HashMap<>();
        for (int i = 0; i < skuSaleAttrValueListBySpu.size(); i++) {
            SkuSaleAttrValue skuSaleAttrValue = skuSaleAttrValueListBySpu.get(i);
            if(valueIdsKey.length()!=0){
                valueIdsKey= valueIdsKey+"|";
            }
            valueIdsKey=valueIdsKey+skuSaleAttrValue.getSaleAttrValueId();
            if((i+1)== skuSaleAttrValueListBySpu.size()||!skuSaleAttrValue.getSkuId().equals(skuSaleAttrValueListBySpu.get(i+1).getSkuId())  ){
                valuesSkuMap.put(valueIdsKey,skuSaleAttrValue.getSkuId());
                valueIdsKey="";
            }
        }
        String valuesSkuJson = JSON.toJSONString(valuesSkuMap);//把map变成json串
        model.addAttribute("valueIdSkuJson",valuesSkuJson);
    }
}
