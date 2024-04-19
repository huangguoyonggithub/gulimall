package com.hgy.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * 合并采购单
 * @author hgy
 * @Description
 * @created 2024/4/18 10:51
 */
@Data
public class MergeVo {
    /**
     * {
     *   purchaseId: 1, //整单id
     *   items:[1,2,3,4] //合并项集合
     * }
     */
    private Long purchaseId;
    private List<Long> items;
}
