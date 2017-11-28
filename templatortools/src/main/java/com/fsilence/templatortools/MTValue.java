package com.fsilence.templatortools;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MiniTemplate 模板中value的注释
 * <b>创建时间</b> 2014/12/15
 * <b>1.1更新时间</b> 2015/01/14 添加ignoreIfNotExist true 表示忽略 {@link biz.source_code.miniTemplator.MiniTemplator.VariableNotDefinedException}
 *                              false 标识抛出异常
 * @version 1.1
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface MTValue {
    public enum MTValueType{
        /**
         * VALUE 基本数据类型直接toString处理 LIST 或者 Array 就TextUtils.join(,) 必须带name
         * OBJ 处理内部 List Array循环处理
         */
        VALUE, OBJ
    }

    /**
     * 对应在模板中的字段名称
     * @return 模板中的字段名称
     */
    public String name() default "";

    /**
     * 如果模板中没有这个字段是否忽视
     * @return default true 如果是false的话，会抛出异常，true的话忽略它
     */
    public boolean ignoreIfNotExist() default true;

    /**
     * Field的类型
     * @see MTValueType
     * @return Field类型
     */
    public MTValueType type() default MTValueType.VALUE;
}
