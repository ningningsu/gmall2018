package com.atguigu.gmall.manage.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.gmall.bean.BaseAttrInfo;
import com.atguigu.gmall.bean.BaseAttrValue;
import com.atguigu.gmall.bean.BaseSaleAttr;
import com.atguigu.gmall.manage.mapper.BaseAttrInfoMapper;
import com.atguigu.gmall.manage.mapper.BaseAttrValueMapper;
import com.atguigu.gmall.manage.mapper.BaseSaleAttrMapper;
import com.atguigu.gmall.service.AttrService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AttrServiceImpl implements AttrService {

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;

    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;
    @Autowired
    BaseSaleAttrMapper baseSaleAttrMapper;



    @Override
    public List<BaseAttrInfo> getAttrListByCtg3(String catalog3Id) {

        BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
        baseAttrInfo.setCatalog3Id(catalog3Id);
        List<BaseAttrInfo> select = baseAttrInfoMapper.select(baseAttrInfo);

        if(null!=select&&select.size()>0){
            for (BaseAttrInfo attrInfo : select) {
                String attrId = attrInfo.getId();

                BaseAttrValue baseAttrValue = new BaseAttrValue();
                baseAttrValue.setAttrId(attrId);

                List<BaseAttrValue> select1 = baseAttrValueMapper.select(baseAttrValue);

                attrInfo.setAttrValueList(select1);
            }
        }

        return select;
    }


    @Override
    public void saveAttr(BaseAttrInfo baseAttrInfo) {

        String id = baseAttrInfo.getId();

        if(StringUtils.isBlank(id)){//id为空时是保存
            // 保存属性信息
            baseAttrInfoMapper.insertSelective(baseAttrInfo);
            String attrId = baseAttrInfo.getId();
            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

            for (BaseAttrValue baseAttrValue : attrValueList) {
                baseAttrValue.setAttrId(attrId);

                baseAttrValueMapper.insert(baseAttrValue);
            }
        }else{//id不为空时是更新
            baseAttrInfoMapper.updateByPrimaryKeySelective(baseAttrInfo);

            Example example = new Example(BaseAttrValue.class);
            example.createCriteria().andEqualTo("attrId",baseAttrInfo.getId());
            baseAttrValueMapper.deleteByExample(example);

            String attrId = baseAttrInfo.getId();
            List<BaseAttrValue> attrValueList = baseAttrInfo.getAttrValueList();

            for (BaseAttrValue baseAttrValue : attrValueList) {
                baseAttrValue.setAttrId(attrId);

                baseAttrValueMapper.insert(baseAttrValue);
            }
        }

    }

    @Override
    public List<BaseAttrInfo> getAttrListByValueId(String join) {


        String[] split = join.split(",");

            BaseAttrValue baseAttrValue = new BaseAttrValue();
            BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
            baseAttrInfo.setCatalog3Id("61");
            List<BaseAttrInfo> select1 = baseAttrInfoMapper.select(baseAttrInfo);
            for (BaseAttrInfo attrInfo : select1) {
                String id = attrInfo.getId();
                baseAttrValue.setAttrId(id);
//                Example example = new Example(baseAttrValue.getClass());
//                Example.Criteria criteria = example.createCriteria();
//                criteria.andEqualTo("attrId",id);
//                List<BaseAttrValue> baseAttrValues = baseAttrValueMapper.selectByExample(example);
                List<BaseAttrValue> select = baseAttrValueMapper.select(baseAttrValue);
                attrInfo.setAttrValueList(select);
            }
        return select1;
    }

    @Override
    public List<BaseSaleAttr> getbaseSaleAttrList() {
        List<BaseSaleAttr> baseSaleAttrs = baseSaleAttrMapper.selectAll();
        return baseSaleAttrs;
    }
}
