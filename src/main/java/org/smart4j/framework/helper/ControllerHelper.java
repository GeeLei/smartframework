package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.annotation.Action;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Request;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 控制器助手类，请求方法与请求路径获取处理对象
 * Created by GL on 2017/6/27.
 */
public class ControllerHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerHelper.class);

    
    /**
     * 储存请求与处理器的映射关系
     */
    private static  final Map<Request,Handler> ACTION_MAP=new HashMap<Request, Handler>();
    static {
        LOGGER.debug("ControllerHelper.class is initializing...");
        //获取所有的Controller类
        Set<Class<?>> controllerSet=ClassHelper.getControllerClassSet();

        if (controllerSet!=null&&controllerSet.size()>0){
            //遍历所有的Controller类
            for (Class<?> controllerClass : controllerSet){
                //获取类中声明的所有方法
                Method[] metohds=controllerClass.getDeclaredMethods();
                if (metohds!=null&&metohds.length>0){
                    //遍历所有方法
                    for (Method method : metohds){
                        //找到带有@Action注解的方法
                        if (method.isAnnotationPresent(Action.class)){
                            Action action=method.getAnnotation(Action.class);
                            //获取注解中的URL映射规则
                            String mapping=action.value();
                            LOGGER.debug("action mapping for method "+method.getName() +" in class "
                                    +controllerClass.getName()+" is :"+mapping);
                            //验证URL规则符合(“GET:/requestPath”)格式
                            if(mapping.matches("\\w+:/\\w*")){
                                String[] array=mapping.split(":");
                                if (array!=null&&array.length==2){
                                    //获取请求方法与请求路径
                                    String requestMethod=array[0];
                                    String requestPath=array[1];
                                    Request request=new Request(requestMethod,requestPath);
                                    Handler handler=new Handler(controllerClass,method);
                                    //把请求和处理器映射关系放到容器中
                                    ACTION_MAP.put(request,handler);
                                }
                            }else{
                                LOGGER.error("action mapping "+mapping+" for method "+method.getName() +" in class "
                                        +controllerClass.getName()+" is invalid");
                                throw  new RuntimeException(" invalid action mapping :"+mapping);
                            }
                        }
                    }
                }
            }
        }
        LOGGER.debug("ControllerHelper.class has bean initialized ");
    }
    /**
     * 获取Handler
     */
    public static Handler getHandler(String requestMethod,String requestPath){
        //LOGGER.debug("ACTION_MAP中的所有映射关系为："+ JsonUtil.toJson(ACTION_MAP));
        Request request = new Request(requestMethod,requestPath);
        return  ACTION_MAP.get(request);
    }
}
