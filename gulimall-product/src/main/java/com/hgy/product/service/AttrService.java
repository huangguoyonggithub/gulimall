package com.hgy.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgy.common.utils.PageUtils;
import com.hgy.product.entity.AttrEntity;
import com.hgy.product.vo.AttrGroupRelationVo;
import com.hgy.product.vo.AttrRespVo;
import com.hgy.product.vo.AttrVo;
import com.hgy.product.vo.BaseAttrs;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-29 18:15:56
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String attrType);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);
}

