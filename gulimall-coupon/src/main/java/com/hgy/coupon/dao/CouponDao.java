package com.hgy.coupon.dao;

import com.hgy.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-30 15:14:29
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
