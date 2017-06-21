package com.qdk;

import com.qdk.bean.Data;
import com.qdk.bean.Handler;
import com.qdk.bean.Param;
import com.qdk.helper.BeanHelper;
import com.qdk.helper.ControllerHelper;
import com.qdk.helper.JsonUtil;
import com.qdk.util.CodecUtil;
import com.qdk.util.ReflectionUtil;
import com.qdk.util.StreamUtil;
import com.qdk.util.StringUtil;
import com.sun.deploy.util.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by QDK on 2017/6/19.
 */

public class DispatcherServlet extends HttpServlet{
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        //super.init();
        HelperLoader.init();
        //获取ServletContext 对象（用于注册Servlet）
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //得到POST或者GET
        String requestMethod=req.getMethod().toLowerCase();
        //得到路径即使url
        String requestPath=req.getPathInfo();

        Handler handler= ControllerHelper.getHandler(requestMethod,requestPath);
        if (handler!=null){
            //提取这个handler中的ControllerClass成员
            Class<?> controllerClass=handler.getControllerClass();
            Object controllerBean= BeanHelper.getBean(controllerClass);

            Map<String,Object> paramMap=new HashMap<String, Object>();
            //获取html表单中填写的信息
            Enumeration<String> paramNames=req.getParameterNames();
            while (paramNames.hasMoreElements()){
                String paraName=paramNames.nextElement();
                //每一个paraName都有一个paramValue，提取出来然后用ParaName保存起来
                String paramValue=req.getParameter(paraName);
                paramMap.put(paraName,paramValue);
            }
            String body= CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if(StringUtil.isNotEmpty(body)){
                //如果传递的参数不止一个，每个参数需要用"&"分开，baseUrl和传递的参数用"?"分开
                String[] params= StringUtils.splitString(body,"&");
                if(ArrayUtils.isNotEmpty(params)){
                    for(String param:params){
                        String[] array=StringUtils.splitString(param,"=");
                        if(ArrayUtils.isNotEmpty(array)&&array.length==2){
                            String paramName=array[0];
                            String paramValue=array[1];
                            paramMap.put(paramName,paramValue);
                        }
                    }
                }
            }
            Param param=new Param(paramMap);
            Method actionMethod=handler.getActionMethod();
            Object result= ReflectionUtil.invokeMethod(controllerBean,actionMethod,param);
            if(result instanceof Data){
                Data data=(Data) result;
                Object model=data.getModel();
                if(model!=null){
                    resp.setContentType("application/json");
                    resp.setCharacterEncoding("UTF-8");
                    PrintWriter writer=resp.getWriter();
                    //
                    String json= JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }

        }
    }
}
