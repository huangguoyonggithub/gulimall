package com.hgy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgy.common.utils.PageUtils;
import com.hgy.product.entity.SkuImagesEntity;

import java.util.Map;

/**
 * sku图片
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-29 18:15:55
 */
public interface SkuImagesService extends IService<SkuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

