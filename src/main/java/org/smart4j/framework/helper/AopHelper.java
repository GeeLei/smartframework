package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Aspect;
import org.smart4j.framework.proxy.AbstractAspectProxy;
import org.smart4j.framework.proxy.Proxy;
import org.smart4j.framework.proxy.ProxyManager;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 切面配置助手类
 * Created by GL on 2017/7/4.
 */
public class AopHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);

    static {
        try {
            LOGGER.debug("AopHelper.class is initializing...");
            Map<Class<?>,Set<Class<?>>> proxyMap=createProxyMap();
            Map<Class<?> , List<Proxy>> targetMap=createTargetMap(proxyMap);
            for (Map.Entry<Class<?>,List<Proxy>> targetEntry : targetMap.entrySet()){
                Class<?> targetClass=targetEntry.getKey();
                List<Proxy> proxyList=targetEntry.getValue();
                Object proxy= ProxyManager.createProxy(targetClass,proxyList);
                //Ioc容器中的代理类的对象替换成代理对象，然后注入到其他对象中
                BeanHelper.setBean(targetClass,proxy);
                LOGGER.debug(targetClass.getName()+" 设置的代理对象实例名为： "+proxy.getClass().getName());
            }
        }catch (Exception e){
            LOGGER.error("aop failure",e);
        }
        LOGGER.debug("AopHelper.class has been initialized ");
    }
    

    /**
     * 创建@Aspect注解应用的目标类集合，例如@Aspect(Controller.class)获取所有Controller类作为代理目标类
     */
    private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws  Exception {
        Set<Class<?>> targetClassSet=new HashSet<Class<?>>();
        Class<? extends Annotation> annotation=aspect.value();
        if(annotation!=null&&!annotation.equals(Aspect.class)){
            targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));
        }
        return targetClassSet;
    }

    /**
     * 获取代理类(切面类)和目标类集合之间的映射关系，一个代理类可能映射多个目标类
     */
    private static Map<Class<?>,Set<Class<?>>> createProxyMap() throws  Exception{
        Map<Class<?>,Set<Class<?>>> proxyMap=new HashMap<Class<?>, Set<Class<?>>>();
        Set<Class<?>> proxyClassSet=ClassHelper.getClassBySuperClass(AbstractAspectProxy.class);
        for (Class<?> proxyClass : proxyClassSet){
            if (proxyClass.isAnnotationPresent(Aspect.class)){
                Aspect aspect=proxyClass.getAnnotation(Aspect.class);
                //获取@Aspect注解值中的目标类集合
                Set<Class<?>> targetClassSet=createTargetClassSet(aspect);
                proxyMap.put(proxyClass,targetClassSet);
            }
        }
        return proxyMap;
    }

    /**
     * 创建目标类与代理类实例列表之间的映射关系，一个目标类可能被多个代理类实例进行增强
     */
    private static Map<Class<?> , List<Proxy>> createTargetMap(Map<Class<?>,Set<Class<?>>> proxyMap) throws Exception{
        //目标类与代理对象集合的映射关系
        Map<Class<?>,List<Proxy>> targetMap=new HashMap<Class<?>, List<Proxy>>();
        for (Map.Entry<Class<?>,Set<Class<?>>> proxyEntry : proxyMap.entrySet()){
            Class<?> proxyClass=proxyEntry.getKey();
            Set<Class<?>> targetClassSet=proxyEntry.getValue();
            for (Class<?> targetClass : targetClassSet){
                //代理类的实例
                Proxy proxy=(Proxy) proxyClass.newInstance();
                if (targetMap.containsKey(targetClass)){
                    targetMap.get(targetClassSet).add(proxy);
                }else {
                    List<Proxy> proxyList=new ArrayList<Proxy>();
                    proxyList.add(proxy);
                    targetMap.put(targetClass,proxyList);
                }
            }
        }
        return  targetMap;
    }
}
