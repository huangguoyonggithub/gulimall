package com.hgy.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hgy.coupon.entity.CouponEntity;
import com.hgy.coupon.service.CouponService;
import com.hgy.common.utils.PageUtils;
import com.hgy.common.utils.R;
import org.springframework.web.context.annotation.RequestScope;


/**
 * 优惠券信息
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-30 15:14:29
 */
@RefreshScope  //动态刷新配置
@RestController
@RequestMapping("coupon/coupon")
public class CouponController {
    @Autowired
    private CouponService couponService;

    @Value("${coupon.user.name}")  //获取配置文件配置的值
    private String name;
    @Value("${coupon.user.age}")
    private String age;
    @RequestMapping("/test")
    public R test(){
        return R.ok().put("username",name).put("age",age);
    }

    //自定义的家的远程调用方法，用于测试远程调用
    @RequestMapping("/member/list")
    public R membercoupons(){
        CouponEntity couponEntity = new CouponEntity();
        couponEntity.setCouponName("满100减20");

        return R.ok().put("coupons",Arrays.asList(couponEntity));
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = couponService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		CouponEntity coupon = couponService.getById(id);

        return R.ok().put("coupon", coupon);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CouponEntity coupon){
		couponService.save(coupon);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CouponEntity coupon){
		couponService.updateById(coupon);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		couponService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
