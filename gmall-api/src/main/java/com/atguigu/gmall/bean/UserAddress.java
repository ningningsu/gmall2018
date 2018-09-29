package com.atguigu.gmall.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/9/2/002.
 */
public class UserAddress implements Serializable {
    private String id;
    private String user_address;
    private String user_id;
    private String consignee;
    private String phone_num;
    private String is_default;

    public UserAddress(String id, String user_address,
                       String user_id, String consignee,
                       String phone_num, String is_default) {
        this.id = id;
        this.user_address = user_address;
        this.user_id = user_id;
        this.consignee = consignee;
        this.phone_num = phone_num;
        this.is_default = is_default;
    }

    public UserAddress() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getIs_default() {
        return is_default;
    }

    public void setIs_default(String is_default) {
        this.is_default = is_default;
    }


}
