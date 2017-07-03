package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类，封装Java反射相关API，可以通过类来实例化对象
 * Created by GL on 2017/6/27.
 */
public class ReflectionUtil {
    private static  final Logger LOGGER= LoggerFactory.getLogger(ReflectionUtil.class);

    /**
     * 创建类的实例
     */
    public static Object newInstance(Class<?> clazz){
        Object instance;
        try {
            instance=clazz.newInstance();
        }catch (Exception e){
            LOGGER.error("new instance faliure for class :"+clazz.getName());
            throw  new RuntimeException(e);
        }
        return  instance;
    }

    /**
     * 调用方法
     */
    public static Object invokeMethod(Object obj, Method method, Object... params){
        Object result;
        try {
            method.setAccessible(true);
            result=method.invoke(obj,params);
        }catch (Exception e){
            LOGGER.error("invoke method failure for method :"+method.getName());
            throw  new RuntimeException(e);
        }
        return  result;
    }

    /**
     * 设置成员变量的值,用于注入依赖
     */
    public  static void setField(Object obj, Field field, Object value){
        try {
            field.setAccessible(true);
            field.set(obj,value);
        }catch (Exception e){
            LOGGER.error("set field failure",e);
            throw  new RuntimeException(e);
        }
    }
}
