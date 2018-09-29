package com.atguigu.gmall.manage.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.*;
import com.atguigu.gmall.manage.mapper.SkuAttrValueMapper;
import com.atguigu.gmall.manage.mapper.SkuImageMapper;
import com.atguigu.gmall.manage.mapper.SkuInfoMapper;
import com.atguigu.gmall.manage.mapper.SkuSaleAttrValueMapper;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.utils.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import java.util.List;

/**
 * Created by Administrator on 2018/9/6/006.
 */
@Service
public class SkuServiceImpl implements SkuService {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    SkuInfoMapper skuInfoMapper;
    @Autowired
    SkuImageMapper skuImageMapper;
    @Autowired
    SkuSaleAttrValueMapper skuSaleAttrValueMapper;
    @Autowired
    SkuAttrValueMapper skuAttrValueMapper;


    @Override
    public List<SkuInfo> getSkuInfoByCatalog3Id(String catalog3Id) {
        // 查询sku信息
        SkuInfo skuInfoParam = new SkuInfo();
        skuInfoParam.setCatalog3Id(catalog3Id+"");
        List<SkuInfo> skuInfos = skuInfoMapper.select(skuInfoParam);

        for (SkuInfo skuInfo : skuInfos) {
            String skuId = skuInfo.getId();
            // 查询图片集合
            SkuImage skuImageParam = new SkuImage();
            skuImageParam.setSkuId(skuId);
            List<SkuImage> skuImages = skuImageMapper.select(skuImageParam);
            skuInfo.setSkuImageList(skuImages);

            // 平台属性集合
            SkuAttrValue skuAttrValue = new SkuAttrValue();
            skuAttrValue.setSkuId(skuId);
            List<SkuAttrValue> skuAttrValues = skuAttrValueMapper.select(skuAttrValue);

            skuInfo.setSkuAttrValueList(skuAttrValues);
        }



        return skuInfos;
    }



    public SkuInfo getSkuByIdFromDB(String skuId) {
        System.out.println(skuId);
        // 查询sku信息
        SkuInfo skuInfoParam = new SkuInfo();
        skuInfoParam.setId(skuId);
        System.out.println("selectOne");
        SkuInfo skuInfo = skuInfoMapper.selectOne(skuInfoParam);
        // 查询图片集合
        SkuImage skuImageParam = new SkuImage();
        skuImageParam.setSkuId(skuId);
        System.out.println("skuImage");
        List<SkuImage> skuImages = skuImageMapper.select(skuImageParam);
//        for (SkuImage skuImage : skuImages) {
//            System.out.println("skuImage");
//            System.out.println(skuImage.getImgName());
//        }
        skuInfo.setSkuImageList(skuImages);

        return skuInfo;
    }

    @Override
    public SkuInfo getSkuById(String skuId) {
        String custom = Thread.currentThread().getName();
        System.err.println(custom + "线程进入sku查询方法");
        SkuInfo skuInfo = null;
        String skuKey = "sku:" + skuId + ":info";
        Jedis jedis = redisUtil.getJedis();
        String s = jedis.get(skuKey);// 缓存redis查询
        if (StringUtils.isNotBlank(s) && s.equals("empty")) {
            System.err.println(custom + "线程进发现数据库中没有数据，返回");
            return null;
        }
        if (StringUtils.isNotBlank(s) && !"empty".equals(s)) {
            System.err.println(custom + "线程能够从redis中获取数据");
            skuInfo = JSON.parseObject(s, SkuInfo.class);
        } else {
            System.err.println(custom + "线程没有从redis中取出数据库，申请访问数据库的分布式锁");
            // db查询(限制db的访问量)
            String OK = jedis.set("sku:" + skuId + ":lock", "1", "nx", "px", 10000);
            if (StringUtils.isNotBlank(OK)) {
                System.err.println(custom + "线程得到分布式锁，开始访问数据库");
                skuInfo = getSkuByIdFromDB(skuId);
                if (null != skuInfo) {
                    System.err.println(custom + "线程成功访问数据库，删除分布式锁");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    jedis.del("sku:" + skuId + ":lock");
                }
            } else {
                System.err.println(custom + "线程需要访问数据库，但是未得到分布式锁，开始自旋");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 自旋
                jedis.close();
                return getSkuById(skuId);
            }
            if (null == skuInfo) {
                System.err.println(custom + "线程访问数据库后，发现数据库为空，将空值同步redis");
                jedis.set(skuKey, "empty");
            }
            // 同步redis
            System.err.println(custom + "线程将数据库中获取数据同步redis");
            if (null != skuInfo && !"empty".equals(s)) {
                jedis.set(skuKey, JSON.toJSONString(skuInfo));
            }
        }
        System.err.println(custom + "线程结束访问返回");
        jedis.close();
        return skuInfo;
    }

    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySkuFormDB(String spuId, String skuId) {
        List<SpuSaleAttr> spuSaleAttrs = skuSaleAttrValueMapper.selectSpuSaleAttrListCheckBySku(Integer.parseInt(spuId),
                Integer.parseInt(skuId));
        return spuSaleAttrs;
    }
    @Override
    public List<SpuSaleAttr> getSpuSaleAttrListCheckBySku(String spuId, String skuId) {
    String custom = Thread.currentThread().getName();
    System.err.println(custom + "线程进入sku查询方法");
    List<SpuSaleAttr> spuSaleAttrs = null;
    String skuKey = "getSpuSaleAttrListCheckBySku:" + spuId + ":" + skuId + ":info";
    Jedis jedis = redisUtil.getJedis();
    String s = jedis.get(skuKey);// 缓存redis查询
    if (StringUtils.isNotBlank(s) && s.equals("empty")) {
        System.err.println(custom + "线程进发现数据库中没有数据，返回");
        return null;
    }
    if (StringUtils.isNotBlank(s) && !"empty".equals(s)) {
        System.err.println(custom + "线程能够从redis中获取数据");
        spuSaleAttrs = JSON.parseObject(s, List.class);
    } else {
        System.err.println(custom + "线程没有从redis中取出数据库，申请访问数据库的分布式锁");
        // db查询(限制db的访问量)
        String OK = jedis.set("getSpuSaleAttrListCheckBySku:"+ spuId + ":" + skuId + ":lock", "1", "nx", "px", 10000);
        if (StringUtils.isNotBlank(OK)) {
            System.err.println(custom + "线程得到分布式锁，开始访问数据库");
            spuSaleAttrs = getSpuSaleAttrListCheckBySkuFormDB(spuId, skuId);
            if (null != spuSaleAttrs) {
                System.err.println(custom + "线程成功访问数据库，删除分布式锁");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                jedis.del("getSpuSaleAttrListCheckBySku:"+ spuId + ":" + skuId + ":lock");
            }
        } else {
            System.err.println(custom + "线程需要访问数据库，但是未得到分布式锁，开始自旋");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // 自旋
            jedis.close();
            return getSpuSaleAttrListCheckBySku(spuId,  skuId);
        }
        if (null == spuSaleAttrs) {
            System.err.println(custom + "线程访问数据库后，发现数据库为空，将空值同步redis");
            jedis.set(skuKey, "empty");
        }
        // 同步redis
        System.err.println(custom + "线程将数据库中获取数据同步redis");
        if (null != spuSaleAttrs && !"empty".equals(s)) {
            jedis.set(skuKey, JSON.toJSONString(spuSaleAttrs));
        }
    }
    System.err.println(custom + "线程结束访问返回");
    jedis.close();
    return spuSaleAttrs;
}

    @Override
    public List<SkuSaleAttrValue> getSkuSaleAttrValueListBySpu(String spuId) {
        List<SkuSaleAttrValue> skuSaleAttrValueList = skuSaleAttrValueMapper.selectSkuSaleAttrValueListBySpu(Long.parseLong(spuId));
        return skuSaleAttrValueList;
    }

    @Override
    public List<SkuInfo> getAllSkuInfo() {
        List<SkuInfo> skuInfos = skuInfoMapper.selectAll();
        return skuInfos;
    }
}
