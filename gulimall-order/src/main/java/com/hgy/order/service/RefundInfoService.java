package com.hgy.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hgy.common.utils.PageUtils;
import com.hgy.order.entity.RefundInfoEntity;

import java.util.Map;

/**
 * 退款信息
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-30 15:33:19
 */
public interface RefundInfoService extends IService<RefundInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

