package org.smart4j.framework.util;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 属性文件读取工具类
 * Created by GL on 2017/6/26.
 */
public class PropsUtil {

    private static final Logger LOGGER= LoggerFactory.getLogger(PropsUtil.class);
    /**
     * 加载属性文件
     */
    public static Properties loadProps(String fileName){
        Properties props=null;
        InputStream is=null;
        try{
            is=Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(is==null){
                throw  new FileNotFoundException(fileName+"file is not found" );
            }
            props=new Properties();
            props.load(is);
        }catch (IOException e) {
            LOGGER.error("load properties file failure",e);
        } finally{
            if (is==null){
                try {
                    is.close();
                } catch (IOException e){
                    LOGGER.error("close input stream failure",e);
                }
            }
        }
        return props;
    }

    /**
     * 获取字符串属性（默认返回空字符串）
     */
    public static String getString(Properties props,String key){
        return getString(props,key,"");
    }

    /**
     * 获取字符串属性（属性不存在时返回默认值）
             */
    public static String getString(Properties props,String key, String defaultValue){
        String value=defaultValue;
        if (props.containsKey(key))
            value=String.valueOf(props.get(key));
        return value;
    }

    /**
     * 获取数值型属性（默认返回0）
     */
    public static Integer getInt(Properties props,String key){
        return getInt(props,key,0);
    }

    /**
     * 获取数值型属性（属性不存在时返回默认值）
     */
    public static Integer getInt(Properties props,String key, int defaultValue){
        int value=defaultValue;
        if (props.containsKey(key))
            value=Integer.parseInt(props.getProperty(key));
        return value;
    }
    /**
     * 获取布尔型属性（默认返回FALSE）
     */
    public static boolean getBoolean(Properties props,String key){
        return getBoolean(props,key,false);
    }

    /**
     * 获取字符串属性（属性不存在时返回默认值）
     */
    public static boolean getBoolean(Properties props,String key, Boolean defaultValue){
        boolean value=defaultValue;
        if (props.containsKey(key))
            value=Boolean.parseBoolean(props.getProperty(key));
        return value;
    }
}
