package com.hgy.product.dao;

import com.hgy.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-29 18:15:56
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
