package org.smart4j.framework.helper;

import org.smart4j.framework.util.ClassUtil;

/**
 * 初始化框架，为了集中、按照顺序加载相应的Helper类
 * Created by GL on 2017/6/27.
 */
public class LoaderHelper {
    //相关Helper类初始化
    public static void  init(){
        Class<?>[] classList={ClassHelper.class,
                BeanHelper.class,
                IocHelper.class,
                ControllerHelper.class
        };
        for (Class<?> clazz : classList){
            ClassUtil.loadClass(clazz.getName(),true);
        }
    }
}
