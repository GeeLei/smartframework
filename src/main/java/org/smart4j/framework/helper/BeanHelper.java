package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.util.ReflectionUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Bean助手类，封装了Bean容器
 * Created by GL on 2017/6/27.
 */
public class BeanHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanHelper.class);


    /**
     * Bean容器，类型与实例的映射
     */
    private static final Map<Class<?>,Object> BEAN_MAP=new HashMap<Class<?>,Object>();

    /**
     * 初始化阶段加载应用包下的所有Controller类和Service类，并保存在Bean容器中
     */
    static {
        LOGGER.debug("BeanHelper.class is initializing...");
       Set<Class<?>> beanClassSet= ClassHelper.getBeanClassSet();
       for (Class<?> beanClass : beanClassSet){
           Object bean= ReflectionUtil.newInstance(beanClass);
           BEAN_MAP.put(beanClass,bean);
       }
       LOGGER.debug("BeanHelper.class has bean initialized");
    }

    /**
     * 从Bean容器中获取Bean实例
     */
    public static <T> T getBean(Class<T> clazz){
        if (!BEAN_MAP.containsKey(clazz)){
            throw  new RuntimeException("can not get bean by class : "+clazz.getName());
        }
        return (T) BEAN_MAP.get(clazz);
    }

    /**
     * 获取Bean 映射
     */
    public static Map<Class<?>,Object> getBeanMap(){
        return BEAN_MAP;
    }

    /**
     * 添加bean实例
     */
    public static void setBean(Class<?> clazz,Object object) {
        BEAN_MAP.put(clazz,object);
    }
}
