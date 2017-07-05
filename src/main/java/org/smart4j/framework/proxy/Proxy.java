package org.smart4j.framework.proxy;

/**
 * 代理接口
 * Created by GL on 2017/7/4.
 */
public interface Proxy {
    /**
     * 执行链式代理
     */
    Object doProxy(ProxyChain chain) throws  Throwable;
}
