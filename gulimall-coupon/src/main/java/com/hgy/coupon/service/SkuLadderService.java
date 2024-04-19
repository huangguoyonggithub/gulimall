package com.hgy.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgy.common.utils.PageUtils;
import com.hgy.coupon.entity.SkuLadderEntity;

import java.util.Map;

/**
 * 商品阶梯价格
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-30 15:14:29
 */
public interface SkuLadderService extends IService<SkuLadderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

