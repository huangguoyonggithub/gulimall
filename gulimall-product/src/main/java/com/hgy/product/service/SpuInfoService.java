package com.hgy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgy.common.utils.PageUtils;
import com.hgy.product.entity.SpuInfoDescEntity;
import com.hgy.product.entity.SpuInfoEntity;
import com.hgy.product.vo.SpuSaveVo;

import java.util.Map;

/**
 * spu信息
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-29 18:15:55
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfo(SpuSaveVo vo);

    void saveBaceSpuInfo(SpuInfoEntity spuInfoEntity);

    PageUtils queryPageByConition(Map<String, Object> params);
}

