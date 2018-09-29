package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.PaymentInfo;

import java.util.Map;

/**
 * Created by Administrator on 2018/9/24/024.
 */
public interface PaymentService {
    void savePayment(PaymentInfo paymentInfo);

    void updatePayment(PaymentInfo paymentInfo);

    void sendPaymentSuccessQueue(String out_trade_no, String trackingNo);

    void updatePaymentSuccess(PaymentInfo paymentInfo);

    boolean checkStatus(String outTradeNo);

    void sendDelayPaymentResult(String outTradeNo, int i);

    Map<String, String> checkAlipayPayment(String outTradeNo);
}
