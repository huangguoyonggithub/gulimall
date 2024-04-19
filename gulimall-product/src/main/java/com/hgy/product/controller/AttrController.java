package com.hgy.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.hgy.product.entity.ProductAttrValueEntity;
import com.hgy.product.service.ProductAttrValueService;
import com.hgy.product.vo.AttrGroupRelationVo;
import com.hgy.product.vo.AttrRespVo;
import com.hgy.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.hgy.product.entity.AttrEntity;
import com.hgy.product.service.AttrService;
import com.hgy.common.utils.PageUtils;
import com.hgy.common.utils.R;



/**
 * 商品属性
 *
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-29 18:15:56
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    /**
     * 修改商品规格
     * /product/attr/update/{spuId}
     */
    @PostMapping("/update/{spuId}")
    public R updateSpuAttr(@PathVariable("spuId") Long spuId,
                           @RequestBody List<ProductAttrValueEntity> entities){
        productAttrValueService.updateSpuAttr(spuId,entities);
        return R.ok();
    }

    /**
     * 获取spu规格
     * /product/attr/base/listforspu/{spuId}
     */
    @GetMapping("/base/listforspu/{spuId}")
    public R baseAttrListforspu(@PathVariable("spuId") Long spuId){
        List<ProductAttrValueEntity> entities = productAttrValueService.baseAttrListforspu(spuId);
        return R.ok().put("data",entities);
    }

    /**
     * 规格参数的查询和销售属性的查询
     * 销售属性路径：/product/attr/sale/list/{catelogId}
     * 平台属性路径 ：/product/attr/base/list/{catelogId}
     * @return AttrList
     */
    @GetMapping("/{attrType}/list/{catelogId}")
    public R baseAttrList(@RequestParam Map<String, Object> params, //分页参数
                          @PathVariable("catelogId") Long catelogId, //三级分类id的路径变量
                          @PathVariable("attrType") String attrType){
        PageUtils page = attrService.queryBaseAttrPage(params,catelogId,attrType); //分页查询规格参数：params分页条件，catelogId三级分类id
        return R.ok().put("page", page); //接口文档里前端获取数据使用page，使用保存到page中
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 查询attr的信息
     * 路径：/product/attr/info/{attrId}
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
//		AttrEntity attr = attrService.getById(attrId);

        AttrRespVo respVo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", respVo);
    }

    /**
     * 规格参数的新增和销售属性的新增
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }

    /**
     * 规格参数的修改和销售属性的修改
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrVo attr){
		attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
