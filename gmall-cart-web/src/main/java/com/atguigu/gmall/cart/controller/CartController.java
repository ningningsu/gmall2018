package com.atguigu.gmall.cart.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.annotation.LoginRequire;
import com.atguigu.gmall.bean.CartInfo;
import com.atguigu.gmall.bean.SkuInfo;
import com.atguigu.gmall.service.CartService;
import com.atguigu.gmall.service.SkuService;
import com.atguigu.gmall.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.alibaba.fastjson.JSON.*;

/**
 * Created by Administrator on 2018/9/15/015.
 */
@Controller
public class CartController {
    @Reference
    SkuService skuService;
    @Reference
    CartService cartService;

    public String test() {
        return "index";
    }



    @RequestMapping("checkCart")
    public String cartInner(CartInfo cartInfo, HttpServletRequest request, HttpServletResponse response, ModelMap map) {
        List<CartInfo> cartInfos = new ArrayList<>();
        String skuId = cartInfo.getSkuId();
        String userId = (String) request.getAttribute("userId");//2
        //修改购物车的勾选状态
        if (StringUtils.isNotBlank(userId)) {
            //修改db
            cartInfo.setUserId(userId);
            cartService.updateCartByUserId(cartInfo);
            cartService.getCartInfosFromCacheByUserId(userId);

        } else {
            String listCartCookie = CookieUtil.getCookieValue(request, "listCartCookie", true);
            //覆盖浏览器中的cookies
            cartInfos = JSON.parseArray(listCartCookie, CartInfo.class);
            for (CartInfo info : cartInfos) {
                String skuId1 = info.getSkuId();
                if (skuId1.equals(skuId)) {
                    info.setIsChecked(cartInfo.getIsChecked());

                }
            }
        }
        map.put("cartList", cartInfos);
        BigDecimal totalPrice = getTotalPrice(cartInfos);
        map.put("totalPrice", totalPrice);
        return "cartInner";
    }

    @RequestMapping(value = "addToCart1", method = RequestMethod.POST)
    public String addToCart1(HttpServletRequest request) {
        String skuId = request.getParameter("skuId");
        String num = request.getParameter("num");
        System.out.println(skuId + " " + num);
//        @RequestParam("num") String num,@RequestParam("skuId") String skuId
        return "success";
    }

    @LoginRequire(needSuccess = false)
    @RequestMapping(value = "cartList")
    public String cartList(HttpServletRequest request, ModelMap map) {
        String userId = (String) request.getAttribute("userId");//2
        List<CartInfo> cartInfos = new ArrayList<>();//声明一个处理后的购物车集合对象
        if (StringUtils.isBlank(userId)) {
            //用户没登陆
            String cookieValue = CookieUtil.getCookieValue(request, "listCartCookie", true);//从客户端取出的cookies
            if (StringUtils.isNotBlank(cookieValue)) {
                cartInfos = parseArray(cookieValue, CartInfo.class);//将cookie封装为集合
            }

        } else {
            //用户已登录从redis中获取数据
            cartInfos = cartService.getCartInfosFromCacheByUserId(userId);
        }
        map.put("cartList", cartInfos);
        BigDecimal totalPrice = getTotalPrice(cartInfos);
        map.put("totalPrice", totalPrice);

        return "cartList";
    }

    private BigDecimal getTotalPrice(List<CartInfo> cartInfos) {
        BigDecimal TotalPrice =new BigDecimal(0);
        for (CartInfo cartInfo : cartInfos) {
            Integer skuNum = cartInfo.getSkuNum();
            BigDecimal skuPrice = cartInfo.getSkuPrice();
            cartInfo.setCartPrice(new BigDecimal(skuNum).multiply(skuPrice));
            TotalPrice=TotalPrice.add(new BigDecimal(skuNum).multiply(skuPrice));//循环计算购物车总价
        }
        return TotalPrice;
    }

    @RequestMapping(value = "addToCart", method = RequestMethod.POST)
    public String addToCart(HttpServletRequest request, HttpServletResponse response, @RequestParam Map<String, String> map) {
        List<CartInfo> cartInfos = new ArrayList<>();//声明一个处理后的购物车集合对象
        String skuId = map.get("skuId");
        SkuInfo skuById = skuService.getSkuById(skuId);
        System.out.println(skuById.getId() + " " + skuById.getSpuId() + " " + skuById.getSkuDefaultImg() + " " + skuById.getSkuName());
        String num = map.get("num");
        Integer skuNum = Integer.parseInt(num);
        SkuInfo skuInfo = skuService.getSkuById(skuId);
        System.out.println(skuId + " " + num);
        request.setAttribute("skuInfo", skuById);
        //判断用户是否登录
        String userId = (String) request.getAttribute("userId");//2
        //封装购物车对象
        CartInfo cartInfo = new CartInfo();
        cartInfo.setSkuId(skuById.getId());
        cartInfo.setSkuPrice(skuById.getPrice());
        cartInfo.setImgUrl(skuById.getSkuDefaultImg());
        cartInfo.setUserId(userId);
        cartInfo.setSkuName(skuById.getSkuName());
        cartInfo.setSkuNum(skuNum);
        cartInfo.setIsChecked("1");

        if (StringUtils.isBlank(userId)) {//用户为空
            //登录页面e
            String cookieValue = CookieUtil.getCookieValue(request, "listCartCookie", true);//从客户端取出的cookies
            if (StringUtils.isBlank(cookieValue)) {


                cartInfos.add(cartInfo);
            } else {
                cartInfos = parseArray(cookieValue, CartInfo.class);//将cookie封装为集合
                boolean b = if_new_cart(cartInfos, cartInfo);
                if (b) {
                    cartInfos.add(cartInfo); //如果是新的购物车需要新增
                } else {
                    //如果购物车中原有此购物车则需要更新此购物车
                    for (CartInfo info : cartInfos) {
                        if (info.getSkuId().equals(cartInfo.getSkuId())) {
                            info.setSkuNum(info.getSkuNum() + cartInfo.getSkuNum());
                            info.setCartPrice(info.getSkuPrice().multiply(new BigDecimal(info.getSkuNum())));
                        }
                    }

                }
            }
            CookieUtil.setCookie(request, response, "listCartCookie", toJSONString(cartInfos), 1000 * 60 * 60 * 24, true);
        } else {
            // db有用户id
            cartInfo.setUserId(userId);
            // db
            CartInfo cartInfoDb = cartService.ifCartExits(cartInfo);

            if (null != cartInfoDb) {
                // 更新
                cartInfoDb.setSkuNum(cartInfoDb.getSkuNum() + cartInfo.getSkuNum());
                cartInfoDb.setCartPrice(cartInfoDb.getSkuPrice().multiply(new BigDecimal(cartInfoDb.getSkuNum())));
                cartService.updateCart(cartInfoDb);
            } else {
                // 添加
                cartService.insertCart(cartInfo);
            }

            // 同步缓存
            cartService.flushCartCacheByUserId(userId);
        }
        return "success";
    }

    private boolean if_new_cart(List<CartInfo> cartInfos, CartInfo cartInfo) {
        boolean b = true;
        for (CartInfo info : cartInfos) {
            if (info.getSkuId().equals(cartInfo.getSkuId())) {
                b = false;
            }
        }
        return b;
    }
}


















