package com.hgy.order.dao;

import com.hgy.order.entity.OrderItemEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单项信息
 * 
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-30 15:33:19
 */
@Mapper
public interface OrderItemDao extends BaseMapper<OrderItemEntity> {
	
}
