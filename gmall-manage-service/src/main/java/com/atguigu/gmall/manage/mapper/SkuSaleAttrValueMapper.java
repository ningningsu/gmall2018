package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.SkuSaleAttrValue;
import com.atguigu.gmall.bean.SpuSaleAttr;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2018/9/10/010.
 */
public interface SkuSaleAttrValueMapper extends Mapper<SkuSaleAttrValue> {

    List<SpuSaleAttr> selectSpuSaleAttrListCheckBySku(@Param("spuId") Integer spuId, @Param("skuId") Integer skuId);

    List<SkuSaleAttrValue> selectSkuSaleAttrValueListBySpu(long l);
}
