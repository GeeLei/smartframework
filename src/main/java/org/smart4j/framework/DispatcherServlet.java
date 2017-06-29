package org.smart4j.framework;

import org.smart4j.framework.bean.Data;
import org.smart4j.framework.bean.Handler;
import org.smart4j.framework.bean.Param;
import org.smart4j.framework.bean.View;
import org.smart4j.framework.helper.BeanHelper;
import org.smart4j.framework.helper.ConfigHelper;
import org.smart4j.framework.helper.ControllerHelper;
import org.smart4j.framework.helper.LoaderHelper;
import org.smart4j.framework.util.JsonUtil;
import org.smart4j.framework.util.ReflectionUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by GL on 2017/6/28.
 */
@WebServlet(urlPatterns = "/*",loadOnStartup = 0)
public class DispatcherServlet extends HttpServlet {

    private static final org.slf4j.Logger LOGGER= org.slf4j.LoggerFactory.getLogger(DispatcherServlet.class);

    private static final String TAG = "DispatcherServlet";

    @Override
    public void init(ServletConfig config) throws ServletException {
        LOGGER.info(TAG+"-->init method is running...");
        //初始化相关Helper类
        LoaderHelper.init();
        //获取ServletContext对象
        ServletContext servletContext=config.getServletContext();
        //注册处理JSP的Servlet
        ServletRegistration jspServlet=servletContext.getServletRegistration("jsp");
        jspServlet.addMapping(ConfigHelper.getAppJspPath()+"*");
        //注册处理静态资源的默认Servlet
        ServletRegistration defaultServlet=servletContext.getServletRegistration("default");
        defaultServlet.addMapping(ConfigHelper.getAppAssetPath()+"*");
        LOGGER.info(TAG+"-->init method finished successfully");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       LOGGER.info(TAG+"-->service method is running...");
        //获取请求方法与请求路径
        String requestMethod=req.getMethod();
        String requestPath=req.getPathInfo();
        //获取Handler处理器
        Handler handler= ControllerHelper.getHandler(requestMethod,requestPath);
        if (handler!=null){
            Class<?> controllerClass=handler.getControllerClass();
            Object controllerBean= BeanHelper.getBean(controllerClass);
            //封装请求信息
            Param param=new Param(req);
            //调用Action方法
            Method method=handler.getActionMethod();
            Object result= ReflectionUtil.invokeMethod(controllerBean,method,param);
            //处理Action方法的返回值
            if (result instanceof View){
                //返回JSP页面
                View view=(View)result;
                String viewPath=view.getPath();
                if (viewPath!=null){
                    LOGGER.debug("view path is " +viewPath);
                    //绝对路径重定向
                    if (viewPath.startsWith("/")){
                        resp.sendRedirect(req.getServletContext()+viewPath);
                    }else{
                        //相对路径转发
                        Map<String,Object> model=view.getModel();
                        for (Map.Entry<String,Object> entry : model.entrySet()){
                            req.setAttribute(entry.getKey(),entry.getValue());
                        }
                        req.getRequestDispatcher(ConfigHelper.getAppJspPath()+viewPath).forward(req,resp);
                    }
                }else {
                    LOGGER.error("view path is null");
                }
            }else if (result instanceof Data){
                Data data=(Data)result;
                Object model=data.getModel();
                if (model!=null){
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer=resp.getWriter();
                    String json=JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }
        }else{
            LOGGER.error("handler has not found for request method : "
                    +requestMethod+" and request path : "+requestPath);
            throw  new RuntimeException("handler has not found for request method : "
                    +requestMethod+" and request path : "+requestPath);
        }
        LOGGER.info(TAG+"-->service method is finished");
    }
}
