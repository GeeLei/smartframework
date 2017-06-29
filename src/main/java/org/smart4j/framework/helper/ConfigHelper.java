package org.smart4j.framework.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart4j.framework.ConfigConstant;
import org.smart4j.framework.util.JsonUtil;
import org.smart4j.framework.util.PropsUtil;

import java.util.Properties;

/**
 * 配置文件助手类
 * Created by GL on 2017/6/27.
 */
public class ConfigHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigHelper.class);

    private static final Properties CONFIG_PROPS;

    static {
        CONFIG_PROPS= PropsUtil.loadProps(ConfigConstant.CONFIG_FILE);
        LOGGER.debug("加载配置文件："+ConfigConstant.CONFIG_FILE+" 完成，配置文件中的内容为："+ JsonUtil.toJson(CONFIG_PROPS));
    }

    /**
     * 获取JDBC驱动
     */
    public static String getJdbcDriver(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_DRIVER);
    }

    /**
     * 获取 JDBC URL
     */
    public static String getJdbcUrl(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_URL);
    }

    /**
     * 获取uJDBC 用户名
     */
    public static  String getJdbcUserName(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_USERNAME);

    }

    /**
     * 获取 JDBC 密码
     */
    public static String getJdbcPassword(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.JDBC_PASSWORD);

    }

    /**
     * 获取应用基础包名
     * @return
     */
    public static String getAppBasePackage(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_BASE_PACKAGE);

    }

    /**
     * 获取 jsp 路径
     */
    public static String getAppJspPath(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_JSP_PATH,"/WEB-INF/view/");

    }

    /**
     * 获取静态资源路径
     */
    public static String getAppAssetPath(){
        return PropsUtil.getString(CONFIG_PROPS,ConfigConstant.APP_ASSET_PATH,"/asset/");

    }
}
