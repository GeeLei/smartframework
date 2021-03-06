package org.smart4j.framework.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 流操作类
 * Created by GL on 2017/6/28.
 */
public class StreamUtil {
    private static  final Logger LOGGER= LoggerFactory.getLogger(StreamUtil.class);

    /**
     * 从输入流中获取字符串
     * @param is 输入流
     * @return 字符串
     */
    public static  String getString(InputStream is){
        StringBuilder sb=new StringBuilder();
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line=reader.readLine())!=null){
                sb.append(line);
            }
        }catch (IOException e){
            LOGGER.error("get string from input stream failure",e);
            throw  new RuntimeException(e);
        }
        return sb.toString();
    }
}
