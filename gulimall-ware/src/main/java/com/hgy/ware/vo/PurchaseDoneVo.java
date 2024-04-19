package com.hgy.ware.vo;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 完成采购数据
 * @author hgy
 * @Description
 * @created 2024/4/18 22:39
 */
@Data
public class PurchaseDoneVo {

    @NotNull
    private Long id; //采购单id

    private List<PurchaseItemDoneVo> items; //采购项
}
