package com.hgy.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hgy.product.entity.CategoryEntity;
import com.hgy.product.service.CategoryService;
import com.hgy.common.utils.PageUtils;
import com.hgy.common.utils.R;



/**
 * 商品三级分类
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-29 18:15:56
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 查出所有分类以及子分类，以树形解构组装起来
     */
    @RequestMapping("/list/tree")
    public R list(){
       List<CategoryEntity> entities = categoryService.listWithTree();

        return R.ok().put("data", entities);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    /**
     * 批量修改菜单数据（拖拽时调用）
     * @param category 要修改的菜单数组
     * @return
     */
    @RequestMapping("/update/sort")
    public R updateSort(@RequestBody CategoryEntity[] category){
        categoryService.updateBatchById(Arrays.asList(category));
        return R.ok();
    }

    /**
     * 级联修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateCascade(category);

        return R.ok();
    }

    /**
     * 删除
     * @RequestBody：获取请求体，必须发送POST请求
     * springMVC自动将请求体中的数据（json）转化为对应的对象
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] catIds){

        //1、检查当前删除菜单，是否被别的地方引用
//		categoryService.removeByIds(Arrays.asList(catIds));
        //批量删除菜单
        categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
