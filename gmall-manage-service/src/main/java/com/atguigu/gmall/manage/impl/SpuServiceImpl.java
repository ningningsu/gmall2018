package com.atguigu.gmall.manage.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.SpuImage;
import com.atguigu.gmall.bean.SpuInfo;
import com.atguigu.gmall.bean.SpuSaleAttr;
import com.atguigu.gmall.bean.SpuSaleAttrValue;
import com.atguigu.gmall.manage.mapper.*;
import com.atguigu.gmall.service.SpuService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Administrator on 2018/9/8/008.
 */
@Service
public class SpuServiceImpl implements SpuService {
    @Autowired
    SpuImageMapper spuImageMapper;
    @Autowired
    SpuInfoMapper spuInfoMapper;
    @Autowired
    SpuSaleAttrMapper spuSaleAttrMapper;
    @Autowired
    SpuSaleAttrValueMapper spuSaleAttrValueMapper;

    @Override
    public void insert(SpuImage spuImage) {
        spuImageMapper.insertSelective(spuImage);
    }

    @Override
    public List<SpuImage> spuImageList(String spuId) {
        SpuImage spuImage = new SpuImage();
        spuImage.setSpuId(spuId);
        List<SpuImage> select = spuImageMapper.select(spuImage);
        return select;
    }


    @Override
    public void insert(SpuSaleAttr spuSaleAttr) {
        spuSaleAttrMapper.insertSelective(spuSaleAttr);
    }

    @Override
    public List<SpuSaleAttr> selectBySpuId(String spuId) {
        SpuSaleAttr spuSaleAttr = new SpuSaleAttr();
        spuSaleAttr.setSpuId(spuId);
        List<SpuSaleAttr> select = spuSaleAttrMapper.select(spuSaleAttr);
        if (null!=select&&select.size()>0){
            for (SpuSaleAttr saleAttr : select) {
                String saleAttrId = saleAttr.getSaleAttrId();
                SpuSaleAttrValue spuSaleAttrValue = new SpuSaleAttrValue();
                spuSaleAttrValue.setSpuId(spuId);
                spuSaleAttrValue.setSaleAttrId(saleAttrId);
                List<SpuSaleAttrValue> select1 = spuSaleAttrValueMapper.select(spuSaleAttrValue);
                saleAttr.setSpuSaleAttrValueList(select1);

            }
        }

        return select;
    }

    @Override
    public List<SpuInfo> getSpuInfoByCatalog3Id(String catalog3Id) {
        SpuInfo spuInfo = new SpuInfo();
        spuInfo.setCatalog3Id(catalog3Id);
        return spuInfoMapper.select(spuInfo);
    }

    @Override
    public String insert(SpuInfo spuInfo1) {
        spuInfoMapper.insertSelective(spuInfo1);
        return spuInfo1.getId();
    }


}
