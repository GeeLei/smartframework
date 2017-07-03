package org.smart4j.framework.bean;

import org.smart4j.framework.util.CodecUtil;
import org.smart4j.framework.util.StreamUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数对象，从HttpServletRequest对象中获取所有请求参数，并封装到此对象中
 * Created by GL on 2017/6/27.</>
 */
public class Param {
    private Map<String,Object> paramMap;

    public Param(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public Param(HttpServletRequest request) throws IOException {
        this.paramMap=new HashMap<String, Object>();
        Enumeration<String> paramNames=request.getAttributeNames();
        while (paramNames.hasMoreElements()){
            String paramName=paramNames.nextElement();
            String paramValue=request.getParameter(paramName);
            paramMap.put(paramName,paramValue);
        }
        String body= CodecUtil.decodeUrl(StreamUtil.getString(request.getInputStream()));
        if (body!=null){
            String[] params=body.split("&");
            if(params!=null&&params.length>0){
                for (String param : params){
                    String[] array=param.split("=");
                    if (array!=null&&array.length==2){
                        String paramName=array[0];
                        String paramValue=array[1];
                        paramMap.put(paramName,paramValue);
                    }
                }
            }
        }
    }

    /**
     * 获取所有字段信息
     * @return
     */
   public Map<String,Object> getMap(){
        return paramMap;
  }


}
