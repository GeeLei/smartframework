package org.smart4j.framework.bean;

import java.lang.reflect.Method;

/**
 * 封装Action所在类和方法信息
 * Created by GL on 2017/6/27.
 */
public class Handler {
    private Class<?> controllerClass;
    private Method actionMethod;

    public Handler(Class<?> controllerClass, Method actionMethod) {
        this.controllerClass = controllerClass;
        this.actionMethod = actionMethod;
    }

    public Class<?> getControllerClass() {
        return controllerClass;
    }

    public Method getActionMethod() {
        return actionMethod;
    }

}

