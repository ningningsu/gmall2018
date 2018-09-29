package com.atguigu.gmall.manage.mapper;

import com.atguigu.gmall.bean.BaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BaseAttrInfoMapper extends Mapper<BaseAttrInfo> {
    List<BaseAttrInfo> selectAttrInfoList(String attrValueIds);
    public List<BaseAttrInfo> selectAttrInfoListByIds(@Param("attrValueIds") String attrValueIds);

    List<BaseAttrInfo> selectAttrListByValueId(String join);
}
