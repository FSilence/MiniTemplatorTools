package com.fsilence.templatortools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MiniTemplate中block的注释
 * <b>创建时间</b> 2014/12/15
 *
 * @author luhao.wei
 * @version 1.0
 */
@Target({ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface MTBlocks {
    public String[] value() default {};
}
