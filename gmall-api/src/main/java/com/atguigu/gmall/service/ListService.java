package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.SkuLsInfo;
import com.atguigu.gmall.bean.SkuLsParam;
import com.atguigu.gmall.bean.SkuLsResult;

import java.util.List;

/**
 * Created by Administrator on 2018/9/13/013.
 */
public interface ListService {
    void init();
    List<SkuLsInfo> search(SkuLsParam skuLsParam);
}
