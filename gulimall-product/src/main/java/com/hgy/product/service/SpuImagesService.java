package com.hgy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgy.common.utils.PageUtils;
import com.hgy.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-29 18:15:55
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveImages(Long id, List<String> images);
}

