package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.OrderDetail;
import com.atguigu.gmall.bean.OrderInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/9/20/020.
 */
public interface OrderService {
    List<OrderDetail> getOrderDetailListByUserId(String userId);

    abstract void saveOrder(OrderInfo orderInfo);

    abstract String genTradeCode(String userId);

    boolean checkTradeCode(String userId, String tradeCode);

    abstract OrderInfo getOrderInfoByOutTradeNo(String out_trade_no);

    void sendOrderResultQueue(OrderInfo orderInfo);

    void updateOrder(OrderInfo orderInfo);
}
