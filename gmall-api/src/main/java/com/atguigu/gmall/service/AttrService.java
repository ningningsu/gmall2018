package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseAttrValue;
import com.atguigu.gmall.bean.BaseSaleAttr;

import java.util.List;
public interface AttrService {

    //通过catalog3Id查询所有catalog3Id符合要求的BaseAttrInfo数据(包含baseAttrValue数组)查出并返回
    List<BaseAttrInfo> getAttrListByCtg3(String catalog3Id);

    void saveAttr(BaseAttrInfo baseAttrInfo);

    List<BaseAttrInfo> getAttrListByValueId(String join);

    List<BaseSaleAttr> getbaseSaleAttrList();
}
