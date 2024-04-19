package com.hgy.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.annotations.JsonAdapter;
import lombok.Data;

/**
 * 商品三级分类
 * 
 * @author hgy
 * @email hgy@gmail.com
 * @date 2024-03-29 18:15:56
 */
@Data
@TableName("pms_category")
public class CategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 分类id
	 */
	@TableId
	private Long catId;
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 父分类id
	 */
	private Long parentCid;
	/**
	 * 层级
	 */
	private Integer catLevel;
	/**
	 * 是否显示[0-不显示，1显示]
	 * Mybatis-plus逻辑删除的字段
	 */
	@TableLogic(value = "1",delval = "0")
	private Integer showStatus;
	/**
	 * 排序
	 */
	private Integer sort;
	/**
	 * 图标地址
	 */
	private String icon;
	/**
	 * 计量单位
	 */
	private String productUnit;
	/**
	 * 商品数量
	 */
	private Integer productCount;

	/**
	 * 所有一级分类的子分类
	 */
	@JsonInclude(JsonInclude.Include.NON_EMPTY)  //表示这个字段不是空或者null的时候返回数据才携带
	@TableField(exist = false) //表示在数据表里不存在
	private List<CategoryEntity> children;

}
