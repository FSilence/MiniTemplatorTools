package com.fsilence.templatortools;

import biz.source_code.miniTemplator.MiniTemplator;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * MinitemPlator的工具类
 * 关于MinitemPlator http://www.oschina.net/p/minitemplator/
 * <b>创建时间</b> 2014/12/15 只支持基本数据类型 对象 和 Array 以及 List
 * <b>1.1更新</b> 2014/1/14 如果字段没有在模板中找到 打印log 并继续找后面的, 添加忽略Block找不到的异常并打印Log
 *
 * @author luhao.wei
 * @version 1.1
 */
public class MTAnnotionUtils {
    private static String TAG = "MTUtils";

    /**
     * 追加写入变量
     *
     * @param mt        模板
     * @param targetObj 目标Object
     * @throws Exception
     */
    public static void appendMT(MiniTemplator mt, Object targetObj) throws Exception {
        if (mt == null) {
            throw new NullPointerException("mt can not be null");
        }
        if (targetObj == null) {
            throw new NullPointerException("params can not be null");
        }
        initParams(mt, targetObj);
    }

    private static void initParams(MiniTemplator mt, Object targetObj) throws Exception {
        //从子类向上一直解到根类
        Class<?> targetClass = targetObj.getClass();
        while (targetClass != Object.class) {
            initBaseParams(mt, targetObj, targetClass);
            targetClass = targetClass.getSuperclass();
        }
    }

    private static void initBaseParams(MiniTemplator mt, Object targetObj, Class<?> targetClass) throws Exception {
        if (targetObj == null) return;
        Field[] fields = targetClass.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field field : fields) {
                MTValue mtValue = field.getAnnotation(MTValue.class);
                if (mtValue != null) {
                    field.setAccessible(true);
                    initMTValue(mt, field, mtValue, targetObj);
                }
            }
        }
        MTBlocks mtBlocks = targetClass.getAnnotation(MTBlocks.class);
        if (mtBlocks != null) {
            String[] blocks = mtBlocks.value();
            if (blocks != null && blocks.length > 0) {
                for (String block : blocks) {
                    try {
                        mt.addBlock(block);
                    } catch (MiniTemplator.BlockNotDefinedException e) {
//                        LogUtil.w(TAG, "Block " + block + " not defined");
                    }
                }
            }
        }
    }

    private static void initMTValue(MiniTemplator mt, Field field, MTValue mtValue, Object targetObj) throws Exception {
        MTValue.MTValueType type = mtValue.type();
        if (type == MTValue.MTValueType.VALUE) {
            initBaseValue(mt, field, mtValue, targetObj);
        } else if (type == MTValue.MTValueType.OBJ) {
            initObjValue(mt, field, targetObj);
        }
    }

    /**
     * Object直接toString Array和 List加入“,”分割
     *
     * @throws Exception
     */
    private static void initBaseValue(MiniTemplator mt, Field field, MTValue mtValue, Object targetObj) throws Exception {
        Object obj = field.get(targetObj);
        try {
            if (field.getType().isArray()) {
                mt.setVariable(mtValue.name(), StringUtils.join(",", getIterableOfArray(obj)));
            } else if (field.getType() == List.class) {
                String listStr = obj == null ? "" : StringUtils.join(",", (List) obj);
                mt.setVariable(mtValue.name(), listStr);
            } else {
                mt.setVariable(mtValue.name(), obj == null ? "" : obj.toString());
            }
        } catch (MiniTemplator.VariableNotDefinedException e) {
//            LogUtil.w(TAG, "Variable " + mtValue.name() + " not defined");
            if (!mtValue.ignoreIfNotExist()) {
                throw e;
            }
        }
    }

    private static void initObjValue(MiniTemplator mt, Field field, Object targetObj) throws Exception {
        Object obj = field.get(targetObj);
        if (field.getType().isArray()) {
            for (Object obj1 : getIterableOfArray(obj)) {
                initParams(mt, obj1);
            }
        } else if (field.getType() == List.class) {
            if (obj != null) {
                for (Object obj1 : (List) obj) {
                    initParams(mt, obj1);
                }
            }
        } else {
            initParams(mt, obj);
        }

    }

    /**
     * 将数组转换成Iterable的
     *
     * @param array 要转换的数组对象
     * @return 转换结果
     */
    private static Iterable getIterableOfArray(Object array) {
        List tem = new ArrayList();
        if (array == null) return tem;
        for (int i = 0; i < Array.getLength(array); i++) {
            tem.add(Array.get(array, i));
        }
        return tem;
    }
}
