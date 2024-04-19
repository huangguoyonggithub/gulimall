package com.hgy.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author hgy
 * @Description
 * @created 2024/4/17 11:26
 */
@Data
public class SkuReductionTo {
    private Long skuId;

    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;

    private List<MemberPrice> memberPrice;
}
