package com.hgy.order.dao;

import com.hgy.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-30 15:33:19
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
