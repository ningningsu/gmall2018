package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.CartInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/9/18/018.
 */
public interface CartService {
    CartInfo ifCartExits(CartInfo cartInfo);

    void updateCart(CartInfo cartInfoDb);

    void insertCart(CartInfo cartInfo);

    void flushCartCacheByUserId(String userId);

    List<CartInfo> getCartInfosFromCacheByUserId(String userId);

    void updateCartByUserId(CartInfo cartInfo);
    void combine(String userId,List<CartInfo> listCartCookie);
    void updateCartDb(List<CartInfo> listCartCache,String userId);

    void deleteCart(String join, String userId);
}
