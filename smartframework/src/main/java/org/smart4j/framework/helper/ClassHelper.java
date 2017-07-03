package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Controller;
import org.smart4j.framework.annotation.Service;
import org.smart4j.framework.util.ClassUtil;
import org.smart4j.framework.util.JsonUtil;

import java.util.HashSet;
import java.util.Set;

/**
 * 类操作助手类,封装ClassUtil
 * Created by GL on 2017/6/27.
 */
public class ClassHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClassHelper.class);
    
    
    private static  final Set<Class<?>> CLASS_SET;

    //加载基础包下的所有类
    static {
        LOGGER.debug("ClassHelper.class is initializing...");
        String basePackage=ConfigHelper.getAppBasePackage();
        CLASS_SET= ClassUtil.getClassSet(basePackage);
        LOGGER.debug("ClassHelper.class has been initialized");
    }

    /**
     * 获取应用包下的所有类
     */
    public static Set<Class<?>> getClassSet(){

        return  CLASS_SET;
    }

    /**
     * 获取应用包下面的所有Service类
     */
    public static Set<Class<?>> getServiceClassSet(){
        Set<Class<?>> serviceClassSet=new HashSet<Class<?>>();
        for (Class<?> clazz : CLASS_SET){
            if(clazz.isAnnotationPresent(Service.class)){
                serviceClassSet.add(clazz);
            }
        }
        LOGGER.debug("基础应用包："+ ConfigHelper.getAppBasePackage()
                +" 下所有@Service注解的类数量有 "+serviceClassSet.size()
                +" ,全部类名为："+ JsonUtil.toJson(serviceClassSet));
        return  serviceClassSet;
    }

    /**
     * 获取应用包下面的所有Controller类
     */
    public static Set<Class<?>> getControllerClassSet(){
        Set<Class<?>> controllerClassSet=new HashSet<Class<?>>();
        for (Class<?> clazz : CLASS_SET){
            if(clazz.isAnnotationPresent(Controller.class)){
                controllerClassSet.add(clazz);
            }
        }
        LOGGER.debug("基础应用包："+ ConfigHelper.getAppBasePackage()
                +" 下所有@Controller注解的类数量有 "+controllerClassSet.size()
                +" ,全部类名为："+ JsonUtil.toJson(controllerClassSet));
        return  controllerClassSet;
    }

    /**
     * 获取应用包下面的所有Servcie类和Controller类
     */
    public  static Set<Class<?>> getBeanClassSet(){
        Set<Class<?>> beanClassSet=new HashSet<Class<?>>();
        beanClassSet.addAll(getServiceClassSet());
        beanClassSet.addAll(getControllerClassSet());
        return beanClassSet;
    }
}
