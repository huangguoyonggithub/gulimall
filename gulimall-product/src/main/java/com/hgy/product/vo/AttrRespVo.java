package com.hgy.product.vo;

import lombok.Data;

/** 规格参数响应数据，页面显示的
 * @author hgy
 * @Description
 * @created 2024/4/14 16:13
 */
@Data
public class AttrRespVo extends AttrVo{
    /**
     * "catelogName": "手机/数码/手机", //所属分类名字
     * "groupName": "主体", //所属分组名字
     */

    private String catelogName;

    private String groupName;

    private Long[] catelogPath;
}
