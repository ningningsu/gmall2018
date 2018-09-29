package com.atguigu.gmall.cart.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.bean.CartInfo;
import com.atguigu.gmall.cart.mapper.CartInfoMapper;
import com.atguigu.gmall.service.CartService;

import com.atguigu.gmall.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartInfoMapper cartInfoMapper;

    @Autowired
    RedisUtil redisUtil;

    @Override
    public CartInfo ifCartExits(CartInfo cartInfo) {//数据库中是否存在该购物车内商品

        Example e = new Example(CartInfo.class);

        e.createCriteria().andEqualTo("userId", cartInfo.getUserId()).andEqualTo("skuId", cartInfo.getSkuId());

//        CartInfo cartInfoReturn = cartInfoMapper.selectOneByExample(e);
        CartInfo cartInfo1 = cartInfoMapper.selectOne(cartInfo);

        return cartInfo1;
    }

    @Override
    public void updateCart(CartInfo cartInfoDb) {//将购物车商品插入到数据库并将数据库数据同步到缓存

        cartInfoMapper.updateByPrimaryKeySelective(cartInfoDb);
        flushCartCacheByUserId(cartInfoDb.getUserId());

    }

    @Override
    public void insertCart(CartInfo cartInfo) {//向数据库购物车中插入商品
        cartInfoMapper.insertSelective(cartInfo);
    }

    @Override
    public void flushCartCacheByUserId(String userId) {//将数据库购物车同步到缓存
//        //更新之前需要清除缓存数据
        Jedis jedis = redisUtil.getJedis();
        jedis.del("cart:" + userId + ":list");//删除缓存
        // 查询userId对应的购物车集合
        CartInfo cartInfo = new CartInfo();
        cartInfo.setUserId(userId);
        List<CartInfo> cartInfos = cartInfoMapper.select(cartInfo);
//        Example example = new Example(CartInfo.class);
//        example.createCriteria().andEqualTo("userId",userId);
//        List<CartInfo> cartInfos = cartInfoMapper.selectByExample(example);
        Map<String, String> map = new HashMap<String, String>();
        System.out.println("将数据库购物车同步到缓存");
        if (null != cartInfos && cartInfos.size() > 0) {
            // 将购物车集合转化为map
            for (CartInfo info : cartInfos) {
                map.put(info.getId(), JSON.toJSONString(info));
            }
            // 将购物车的hashMap放入redis
            jedis.hmset("cart:" + userId + ":list", map);
            jedis.close();
        }
    }

    @Override
    public List<CartInfo> getCartInfosFromCacheByUserId(String userId) {//将缓存中的购物车信息查出并返回
        List<CartInfo> cartInfos = new ArrayList<>(); // 声明一个处理后的购物车集合对象
        Jedis jedis = redisUtil.getJedis();
        List<String> hvals = jedis.hvals("cart:" + userId + ":list"); // 将缓存中的购物车的hashMap返回
        if (null != hvals && hvals.size() > 0) {
            for (String hval : hvals) {
                CartInfo cartInfo = JSON.parseObject(hval, CartInfo.class);
                cartInfos.add(cartInfo);
            }
        }
        jedis.close();
        return cartInfos;
    }

    @Override
    public void updateCartByUserId(CartInfo cartInfo) {
        Example example = new Example(CartInfo.class);
        example.createCriteria().andEqualTo("skuId", cartInfo.getSkuId())
                .andEqualTo("userId", cartInfo.getUserId());
        cartInfoMapper.updateByExampleSelective(cartInfo, example);
        System.out.println("将选定货物插入到数据库购物车");
    }

    @Override
    public void combine(String userId, List<CartInfo> cartInfos) {//将客户端购物车cookies合并到数据库端的该用户的购物车
        List<CartInfo> cartInfosCombine = new ArrayList<>();
        //如果客户端购物车有购物车sku，则客户端购物车信息添加到缓存。
        if (null != cartInfos && cartInfos.size() > 0) {
            flushCartCacheByUserId(userId);//将数据库购物车更新到缓存
            List<CartInfo> cartInfosFromCache = getCartInfosFromCacheByUserId(userId);//从缓存中取出当前数据库的数据
            if (null != cartInfosFromCache && cartInfosFromCache.size() > 0) {
                for (CartInfo cartInfo : cartInfos) {
                    boolean b = if_new_cart(cartInfosFromCache, cartInfo);
                    if (b){
                        cartInfosCombine.add(cartInfo);
                    }else {
                        for (CartInfo info : cartInfosFromCache) {
                            if (info.getSkuId().equals(cartInfo.getSkuId())){
                                int i = info.getSkuNum() + cartInfo.getSkuNum();
                                info.setSkuNum(i);
                            }
                        }
                    }
                }
                cartInfosFromCache.addAll(cartInfosCombine);
            }else {
                cartInfosFromCache = cartInfos;//如果缓存为空，将客户端数据复制给缓存
            }

            updateCartDb(cartInfosFromCache, userId);//导入数据库并更新到缓存同步；
        }else {
            //如果客户端购物车为空，则只同步缓存和数据库
//            flushCartCacheByUserId(userId);//将数据库购物车更新到缓存
        }
        System.out.println("stopIn combine");
    }

    @Override
    public void updateCartDb(List<CartInfo> cartInfosCombine, String userId) {//传入要更新的购物车数据,然后将数据从缓存更新到数据库
        Example example = new Example(CartInfo.class);
        example.createCriteria().andEqualTo("userId", userId);
//        List<CartInfo> cartInfos = new ArrayList<>();
        Jedis jedis = redisUtil.getJedis();//将合并后的数据放入缓存
        cartInfoMapper.deleteByExample(example);//清空用户以前的数据库信息//此处无法删除数据？
        Map<String, String> hashMap = new HashMap<String, String>();
        for (CartInfo cartInfo : cartInfosCombine) {
            cartInfo.setUserId(userId);
            cartInfo.setId(null);
            cartInfo.setCartPrice(cartInfo.getSkuPrice().multiply(new BigDecimal(cartInfo.getSkuNum())));
            int i = cartInfoMapper.insertSelective(cartInfo);//将合并后的数据插入到数据库
            System.out.println(i);
            hashMap.put(cartInfo.getSkuId(), JSON.toJSONString(cartInfo));
        }
        jedis.del("cart:" + userId + ":list");
        jedis.hmset("cart:" + userId + ":list", hashMap);//将数据库的购物车列表更新到缓存《redis》
        jedis.close();
    }

    @Override
    public void deleteCart(String join, String userId) {

        // 删除购物车已经下单的数据
        cartInfoMapper.deleteCartsById(join);

        // 同步购物车缓存
        flushCartCacheByUserId(userId);
    }

    private boolean if_new_cart(List<CartInfo> listCartDb, CartInfo cartCookie) {//要保证闯进来的参数不能为空，将加入数据库的商品是否已经存在
        for (CartInfo cartInfo : listCartDb) {
            if (cartInfo.getSkuId().equals(cartCookie.getSkuId())) {
                return false;
            }
        }
        return true;
    }

}




















