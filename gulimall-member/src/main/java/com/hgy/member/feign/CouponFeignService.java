package com.hgy.member.feign;

import com.hgy.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hgy
 * @Description
 * @created 2024/3/30 21:59
 *
 * @FeignClient("gulimall-coupon")  告诉springCloud这是一个远程客户端，它要调用远程服务
 */
@FeignClient("gulimall-coupon")
//@FeignClient(name = "gulimall-coupon" ,url="http://localhost:7000/")  //gulimall-coupon远程服务名，要加上远程服务地址url="http://localhost:7000/
public interface CouponFeignService {

    @RequestMapping("/coupon/coupon/member/list")
    public R membercoupons();
}
