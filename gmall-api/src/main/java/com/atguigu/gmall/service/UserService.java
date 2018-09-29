package com.atguigu.gmall.service;

import com.atguigu.gmall.bean.UserAddress;
import com.atguigu.gmall.bean.UserInfo;

import java.util.List;

/**
 * Created by Administrator on 2018/9/1/001.
 */
public interface UserService {
    List<UserInfo> getUserList();//查询所有UserInfo
    UserInfo getUserInfoById(String id);//通过id查询UserInfo
    List<UserInfo> getUserInfoListByExample(UserInfo userInfo);//通过条件查询userInfo
    int deleteUserInfoById(String id);//通过id删除userinfo
    int inselectUserInfoByExample(UserInfo userInfo);//插入userinfo，允许信息不全
    int updateUserInfoByPrimaryKeySelect(UserInfo userInfo);//修改userinfo信息
    List<UserAddress> getUserAddressList();//查询所有Useraddress
    UserAddress getUserAddressById(String id);//通过id查询userddress
    List<UserAddress> getUserAddressListByExample(UserAddress userAddress);//通过条件查询useraddress
    int deleteAddressById(String id);//通过id删除useraddress
    int inselectUserAddress(UserAddress userAddress);//插入useraddress，允许信息不全
    int updateUserAddressByPrimaryKeySelect(UserAddress userAddress);//修改useraddress信息

    UserInfo login(UserInfo userInfo);

    UserAddress getAddressListById(String addressId);

    List<UserAddress> getAddressListByUserId(String userId);

    List<UserAddress> getUserAddressListByUserId(String s);
}
