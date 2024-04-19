package com.hgy.product.feign;

import com.hgy.common.to.SkuReductionTo;
import com.hgy.common.to.SpuBoundsTo;
import com.hgy.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 远程调用gulimall-coupon的功能
 * @author hgy
 * @Description
 * @created 2024/4/17 10:18
 */
@FeignClient("gulimall-coupon") //调用哪个远程服务
public interface CouponFeignService {

    /**
     * 1、CouponFeignService。saveSpuBounds(spuBoundsTo);
     *      1)、@RequestBody将这个对象spuBoundsTo转化为json
     *      2）、找到gulimall-coupon服务（服务中心中找），给/coupon/spubounds/save发送请求。
     *           将上一步的json放在请求体位置，发送数据
     *      3）、对方服务收到请求。请求体里有json数据
     *           @RequestBody SpuBoundsEntity spuBounds，将请求体中的json数据转化为SpuBoundsEntity
     * 总结：只要json数据模型是兼容的(数据中有一对一关系，字段一样)，双方服务无需使用同一个to
     *
     */

    /**
     * 远程调用保存spu积分信息
     * @param spuBoundsTo
     * @return
     */
    @PostMapping("/coupon/spubounds/save") //指定调用哪个方法(要完整路径)
    R saveSpuBounds(@RequestBody SpuBoundsTo spuBoundsTo);


    /**
     * 远程调用保存惠、满减信息
     * @param skuReductionTo
     * @return
     */
    @PostMapping("/coupon/skufullreduction/saveinfo")
    R saveSkuReduction(@RequestBody SkuReductionTo skuReductionTo);
}
