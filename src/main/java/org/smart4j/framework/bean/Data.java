package org.smart4j.framework.bean;

/**
 * 返回数据对象,通过HttpServletResponse对象直接输出到浏览器
 * Created by GL on 2017/6/28.
 */
public class Data {
    private Object model;

    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }
}
