package com.hgy.ware.vo;

import lombok.Data;

/**
 * 完成采购的采购项
 * @author hgy
 * @Description
 * @created 2024/4/18 22:41
 */
@Data
public class PurchaseItemDoneVo {
    //items: [{itemId:1,status:4,reason:""}]//完成/失败的需求详情
    private Long itemId;
    private Integer status;
    private String reason;
}
