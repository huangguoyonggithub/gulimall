package com.hgy.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgy.common.utils.PageUtils;
import com.hgy.common.utils.Query;

import com.hgy.product.dao.ProductAttrValueDao;
import com.hgy.product.entity.ProductAttrValueEntity;
import com.hgy.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveProductAttrValue(List<ProductAttrValueEntity> collect) {
        if (collect == null || collect.size() == 0){

        }else {
            this.saveBatch(collect);
        }
    }

    /**
     * 获取spu规格
     * @param spuId
     * @return
     */
    @Override
    public List<ProductAttrValueEntity> baseAttrListforspu(Long spuId) {
        List<ProductAttrValueEntity> entities = this.baseMapper.selectList(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id", spuId));

        return entities;
    }

    /**
     * 修改商品规格
     * @param spuId
     * @param entities
     */
    @Transactional
    @Override
    public void updateSpuAttr(Long spuId, List<ProductAttrValueEntity> entities) {
        //1、删除这个spuId之前对应的所有属性
        this.baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));

        //2、插入新的属性数据
        List<ProductAttrValueEntity> collect = entities.stream().map((item) -> {
            item.setSpuId(spuId);
            return item;
        }).collect(Collectors.toList());
        this.saveBatch(collect);
    }

}