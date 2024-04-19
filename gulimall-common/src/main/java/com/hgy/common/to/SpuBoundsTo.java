package com.hgy.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 保存积分spu信息
 * @author hgy
 * @Description
 * @created 2024/4/17 10:26
 */
@Data
public class SpuBoundsTo {

    private Long spuId;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;
}
