package com.qdk.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by lenovo on 2017/6/18.
 */
public final class ClassUtil {
    private static final Logger LOGGER=Logger.getLogger(ClassUtil.class);

    /**
     * 获取加载器,只需要获取当前线程中的ClassLoader即可
     * @return
     */
    public static ClassLoader getClassLoader(){
        return Thread.currentThread().getContextClassLoader();

    }

    /**
     * 加载类
     * @param className
     * @param isInitialized
     * @return
     */
    public static Class<?> loadClass(String className,boolean isInitialized){
        Class<?> cls;
        try {
            cls=Class.forName(className,isInitialized,getClassLoader());

        }catch (ClassNotFoundException e){
            LOGGER.error("Load class failure",e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    /**
     * 记载指定包名下的所有类,需要根据包名并将其转换为文件路径
     * 读取class文件或者jar包，获取指定的类名去加载类
     * @param packageName
     * @return
     */
    public static Set<Class<?>> getClassSet(String packageName){
        Set<Class<?>> classSet=new HashSet<Class<?>>();
        try {
            Enumeration<URL> urls=getClassLoader().getResources(packageName.replace(".","/"));
            while (urls.hasMoreElements()){
                URL url=urls.nextElement();
                if(url!=null){
                    String protocol=url.getProtocol();
                    if(protocol.equals("file")){
                        String packagePath=url.getPath().replaceAll("%20","");
                        addClass(classSet,packagePath,packageName);

                    }else if(protocol.equals("jar")){
                        JarURLConnection jarURLConnection=(JarURLConnection)url.openConnection();
                        if(jarURLConnection!=null){
                            JarFile jarFile=jarURLConnection.getJarFile();
                            if(jarFile!=null){
                                Enumeration<JarEntry> jarEntries=jarFile.entries();
                                while (jarEntries.hasMoreElements()){
                                    JarEntry jarEntry=jarEntries.nextElement();
                                    String jarEntryName=jarEntry.getName();
                                    if (jarEntryName.endsWith(".Class")){
                                        String className=jarEntryName.substring(0,jarEntryName.lastIndexOf(".")).replaceAll("/",".");
                                        doAddClass(classSet,className);

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            LOGGER.error("get class set failure",e);
            throw new RuntimeException(e);
        }
        return classSet;

    }

    private static void addClass(Set<Class<?>> classSet,String packagePath,String packageName){
        final File[] files=new File(packageName).listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return (pathname.isFile()&&pathname.getName().endsWith(".Class"))||pathname.isDirectory();
            }
        });
        for(File file:files){
            String fileName=file.getName();
            if(file.isFile()){
                String className=fileName.substring(0,fileName.lastIndexOf("."));
                if (StringUtil.isNotEmpty(packageName)){
                    className=packageName+"."+className;
                }
                doAddClass(classSet,className);
            }else {
                String subPackagePath=fileName;
                if (StringUtil.isNotEmpty(packagePath)) {
                    subPackagePath=packagePath+"/"+subPackagePath;
                }
                String subPackageName=fileName;
                if(StringUtil.isNotEmpty(packageName)){
                    subPackageName=packageName+"."+subPackageName;

                }
                addClass(classSet,subPackagePath,subPackageName);
            }
        }
    }

    private static void doAddClass(Set<Class<?>> classSet,String className){
        Class<?> cls=loadClass(className,false);
        classSet.add(cls);
    }




}
