package com.hgy.product.service.impl;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hgy.common.utils.PageUtils;
import com.hgy.common.utils.Query;

import com.hgy.product.dao.SkuInfoDao;
import com.hgy.product.entity.SkuInfoEntity;
import com.hgy.product.service.SkuInfoService;
import org.springframework.util.StringUtils;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存发布商品功能-->sku的基本信息，pms_sku_info
     * @param skuInfoEntity
     */
    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    /**
     * Sku的查询和检索
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {

        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();

        /**
         * 查询条件，在浏览器请求载荷里
         * key:
         * catelogId: 0
         * brandId: 0
         * min: 0
         * max: 0
         */

        String key = (String) params.get("key");
        if (StringUtils.hasLength(key)){
            wrapper.and((obj) -> {
                obj.eq("sku_id",key).or().like("sku_name",key);
            });
        }
        String catelogId = (String) params.get("catelogId");
        if (StringUtils.hasLength(catelogId) && !"0".equalsIgnoreCase(catelogId)){ //!"0".equalsIgnoreCase(catelogId)表示catelogId不等于0才工作
            wrapper.and((obj) -> {
                obj.eq("catalog_id",catelogId);
            });
        }
        String brandId = (String) params.get("brandId");
        if (StringUtils.hasLength(brandId) && !"0".equalsIgnoreCase(brandId)){
            wrapper.and((obj) -> {
                obj.eq("brand_id",brandId);
            });
        }
        String min = (String) params.get("min");
        if (StringUtils.hasLength(min)){
            wrapper.and((obj) -> {
                obj.ge("price",min);  //ge大于等于
            });
        }
        String max = (String) params.get("max");
        if (StringUtils.hasLength(max)){
            try {
                BigDecimal bigDecimal = new BigDecimal(max);
                if (bigDecimal.compareTo(new BigDecimal("0")) == 1){
                    wrapper.and((obj) -> {
                        obj.le("price",max);  //le小于等于
                    });
                }
            }catch (Exception e){

            }
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}