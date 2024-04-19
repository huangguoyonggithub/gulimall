package com.hgy.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgy.common.utils.PageUtils;
import com.hgy.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-30 15:33:19
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

