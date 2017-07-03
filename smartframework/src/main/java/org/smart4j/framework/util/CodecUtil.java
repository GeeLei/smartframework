package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 编码与解码操作工具类
 * Created by GL on 2017/6/28.
 */
public class CodecUtil {
    private  static  final Logger LOGGER= LoggerFactory.getLogger(CodecUtil.class);

    /**
     * 把URL编码
     */
    public  static  String encodeUrl(String source){
        String target;
        try {
            target= URLEncoder.encode(source,"UTF-8");
        }catch (Exception e){
            LOGGER.error("encode url failure",e);
            throw  new RuntimeException(e);
        }
        return target;
    }

    /**
     * 把URL解码
     */
    public static  String decodeUrl(String source){
        String target;
        try {
            target= URLDecoder.decode(source,"UTF-8");
        }catch (Exception e){
            LOGGER.error("encode url failure",e);
            throw  new RuntimeException(e);
        }
        return target;
    }
}
