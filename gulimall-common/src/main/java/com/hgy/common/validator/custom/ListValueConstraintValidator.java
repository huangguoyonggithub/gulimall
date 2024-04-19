package com.hgy.common.validator.custom;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义校验器
 *
 * @author hgy
 * @Description
 * @created 2024/4/11 23:47
 */
public class ListValueConstraintValidator implements ConstraintValidator<ListValue, Integer> {

    private Set<Integer> set = new HashSet<>();

    //初始化方法，它给把@ListValue注解的vals值拿来
    @Override
    public void initialize(ListValue constraintAnnotation) {
        int[] vals = constraintAnnotation.vals();
        for (int val : vals) {
            set.add(val);
        }
    }


    //是否校验成功
    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }
}
