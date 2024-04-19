package com.hgy.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgy.common.to.SkuReductionTo;
import com.hgy.common.utils.PageUtils;
import com.hgy.coupon.entity.SkuFullReductionEntity;

import java.util.Map;

/**
 * 商品满减信息
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-30 15:14:29
 */
public interface SkuFullReductionService extends IService<SkuFullReductionEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSkuReduction(SkuReductionTo skuReductionTo);
}

