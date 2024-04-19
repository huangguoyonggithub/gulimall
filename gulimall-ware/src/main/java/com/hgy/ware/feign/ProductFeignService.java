package com.hgy.ware.feign;

import com.hgy.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author hgy
 * @Description
 * @created 2024/4/19 0:01
 */
@FeignClient("gulimall-product")
public interface ProductFeignService {
    /**
     * 远程调用两种请求路径写法
     * 1、让所有请求过网关
     *      1）、@FeignClient("gulimall-gateway")：给gulimall-gateway所在的机器发送请求
     *      2）、/api/product/skuinfo/info/{skuId}
     * 2、直接让后台指定服务处理
     *      1）、@FeignClient("gulimall-product")：给特定服务发送请求
     *      2）、/product/skuinfo/info/{skuId
     * @param skuId
     * @return
     */

    @RequestMapping("/product/skuinfo/info/{skuId}")
    public R info(@PathVariable("skuId") Long skuId);
}
