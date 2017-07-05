package org.smart4j.framework.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * 抽象切面代理类，提供模板方法，切面具体实现类要继承本类，并重写相应的扩展方法
 * Created by GL on 2017/7/4.
 */
public abstract class AbstractAspectProxy implements Proxy{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractAspectProxy.class);

    public final Object doProxy(ProxyChain chain) throws Throwable {
        Object result=null;
        Class<?> targetClass=chain.getTargetClass();
        Method targetMethod=chain.getTargetMethod();
        Object[] methodParams=chain.getMethodParams();

        begin();
        try {
            if (intercept(targetClass,targetMethod,methodParams)){
                before(targetClass,targetMethod,methodParams);
                result=chain.doProxyChain();
                after(targetClass,targetMethod,methodParams,result);
            }else {
                result=chain.doProxyChain();
            }
        }catch (Exception e){
            LOGGER.error("proxy failure ",e);
            error(targetClass,targetMethod,methodParams,e);
            throw  new RuntimeException(e);
        }finally {
            end();
        }

        return result;
    }

    /**
     * doProxy实现了代理链的执行框架，并抽象出了一系列的“钩子方法”，这些方法可以在具体实现类中有选择地进行实现
     */

    public void begin(){

    }

    public boolean intercept(Class<?> targetClass,Method targetMethod,Object[] methodParams) throws  Throwable{
        return true;
    }

    public void before(Class<?> targetClass,Method targetMethod,Object[] methodParams) throws  Throwable{

    }

    public void after(Class<?> targetClass,Method targetMethod,Object[] methodParams,Object result) throws  Throwable{

    }

    public void error(Class<?> targetClass,Method targetMethod,Object[] methodParams,Throwable exception) throws  Throwable{

    }

    public void end(){

    }
}
