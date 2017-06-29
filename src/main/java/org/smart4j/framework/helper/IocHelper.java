package org.smart4j.framework.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Inject;
import org.smart4j.framework.util.ReflectionUtil;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 依赖注入助手类
 * Created by GL on 2017/6/27.
 */
public class IocHelper {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(IocHelper.class);
    
    
    static{
        LOGGER.debug("IocHelper.class is initializing...");
        //获取Bean类与Bean实例之间的映射关系
        Map<Class<?>,Object> beanMap=BeanHelper.getBeanMap();
        if (beanMap!=null&&beanMap.size()>0){
            //遍历BeanMap
            for (Map.Entry<Class<?>,Object> beanEntry : beanMap.entrySet()){
                //获取Bean类和Bean实例
                Class<?> beanClass=beanEntry.getKey();
                Object beanInstance=beanEntry.getValue();
                //根据反射获取Bean类中定义的所有成员变量
                Field[] beanFields=beanClass.getDeclaredFields();
                if (!ArrayUtils.isEmpty(beanFields)){
                    //遍历成员变量
                    for (Field beanField : beanFields){
                        //找到带有@Inject注解的成员变量
                        if(beanField.isAnnotationPresent(Inject.class)){
                            //根据成员变量的类型，在BeanMap中获取Bean实例
                            Class<?> beanFieldClass=beanField.getType();
                            Object beanFieldInstance=beanMap.get(beanFieldClass);
                            //通过反射为成员变量注入实例
                            if (beanFieldInstance!=null){
                                LOGGER.debug(beanInstance.getClass().getName()+"成员变量"
                                        +beanField.getName()+" 注入实例 "
                                        +beanFieldInstance.getClass().getName());
                                ReflectionUtil.setField(beanInstance,beanField,beanFieldInstance);
                            }
                        }

                    }
                }
            }
        }
        LOGGER.debug("IocHelper.class has bean initialized");
    }
}
