package com.hgy.product.dao;

import com.hgy.product.entity.BrandEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 品牌
 * 
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-29 18:15:56
 */
@Mapper
public interface BrandDao extends BaseMapper<BrandEntity> {
	
}
