package org.smart4j.framework.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类操作工具类
 * Created by GL on 2017/6/27.
 */
public class ClassUtil {
    private static final Logger LOGGER= LoggerFactory.getLogger(ClassUtil.class);

    /**
     *  获取类加载器,<br/>
     * 获取当前线程中的ClassLoader
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();
    }


    /**
     * 加载类
     * @param className 类名
     * @param isInitialized 是否执行类的静态代码块
     * @return 加载的类对象
     */
    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> clazz;
        try {
            clazz=Class.forName(className,isInitialized,getClassLoader());
        }catch (ClassNotFoundException e){
            LOGGER.error("load class failure : "+className+" not found .",e);
            throw  new RuntimeException(e);
        }
        return clazz;
    }

    /**
     * 获取指定包名下的所有类<br/></>
     * 将包名转为路径名后，做为参数传给CloassLoader.getResources()，以得到该路径下所有资源的URL;
     *通过URL.getProtocol()方法，判断资源是在本地(file:)或是第三方jar包(jar:)内;
     *在本地的类直接文件遍历即可;
     * 第三方jar则通过URL.openConnection()得到JarURLConnection，再通过JarURLConnection.getJarFile()获得JarFile,最后遍历该JarFile的item即可。
     */
    public  static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls=getClassLoader().getResources(packageName.replace(".","/"));
            while (urls.hasMoreElements()){

                URL url=urls.nextElement();
                if (url!=null){
                    String  protocol=url.getProtocol();
                    //本地文件读取
                    if ("file".equals(protocol)){
                        String packagePath=url.getPath();
                        addClass(classSet,packagePath,packageName);
                    }else if ("jar".equals(protocol))
                        addClassInJar(classSet, url);
                }
            }
        }catch(IOException e){
            LOGGER.error("access package failure : "+ packageName,e);
            throw  new RuntimeException(e);
        }
        return  classSet;
    }

    //本地class文件以及文件夹读取
    private static void  addClass(Set<Class<?>> classSet,String packagePath,String packageName){
        //过滤出.class文件和类文件夹
        File[] files=new File(packagePath).listFiles(new FileFilter() {
            public boolean accept(File file) {
                return (file.isFile()&&file.getName().endsWith(".class"))||file.isDirectory();
            }
        });
        for (File file : files){
            String fileName=file.getName();
            if (file.isFile()){
                String className=fileName.substring(0,fileName.lastIndexOf("."));
                if (StringUtils.isNotEmpty(packageName)){
                    className=packageName+"."+className;//类的全限定名
                }
                doAddClass(classSet,className);
            }else{
                //递归处理文件夹
                String subPackagePath=fileName;
                if (packagePath!=null){
                    subPackagePath=packagePath+"/"+subPackagePath;
                }
                String subPackageName=fileName;
                if (packageName!=null){
                    subPackageName=packageName+"."+subPackageName;
                }
                addClass(classSet,subPackagePath,subPackageName);
            }

        }
    }

    private static void doAddClass(Set<Class<?>> classSet,String className){
        Class<?> clazz=loadClass(className,false);
        classSet.add(clazz);
    }

    //jar包文件读取
    private static void addClassInJar(Set<Class<?>> classSet, URL url) throws IOException {
        JarURLConnection jarURLConnection=(JarURLConnection) url.openConnection();
        if (jarURLConnection!=null){
            JarFile jarFile=jarURLConnection.getJarFile();
            if (jarFile!=null){
                Enumeration<JarEntry> jarEntries=jarFile.entries();
                while (jarEntries.hasMoreElements()){
                    JarEntry jarEntry=jarEntries.nextElement();
                    String jarEntryName=jarEntry.getName();
                    if(jarEntryName.endsWith(".class")){
                        String className=jarEntryName.substring(0,jarEntryName.lastIndexOf("."));
                        doAddClass(classSet,className);
                    }
                }
            }
        }
    }


}

