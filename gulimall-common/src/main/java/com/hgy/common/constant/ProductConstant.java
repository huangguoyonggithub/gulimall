package com.hgy.common.constant;

/**
 * 商品系统有关的常量枚举
 * @author hgy
 * @Description
 * @created 2024/4/14 23:03
 */
public class ProductConstant {

    public enum AttrEnum {
        ATTR_TYPE_BASE(1, "基本属性"), ATTR_TYPE_SALE(0, "销售属性");

        private int code;
        private String msg;

        AttrEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
