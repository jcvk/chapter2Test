package com.qdk;

import com.qdk.bean.Data;
import com.qdk.bean.Handler;
import com.qdk.bean.Param;
import com.qdk.helper.BeanHelper;
import com.qdk.helper.ConfigHelper;
import com.qdk.helper.ControllerHelper;
import com.qdk.helper.JsonUtil;
import com.qdk.util.CodecUtil;
import com.qdk.util.ReflectionUtil;
import com.qdk.util.StreamUtil;
import com.qdk.util.StringUtil;
import com.sun.deploy.util.ArrayUtil;
import com.sun.deploy.util.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.log4j.lf5.util.StreamUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lenovo on 2017/6/19.
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
        String requestMethod=req.getMethod().toLowerCase();
        String requestPath=req.getPathInfo();

        Handler handler= ControllerHelper.getHandler(requestMethod,requestPath);
        if (handler!=null){
            Class<?> controllerClass=handler.getControllerClass();
            Object controllerBean= BeanHelper.getBean(controllerClass);

            Map<String,Object> paramMap=new HashMap<String, Object>();
            Enumeration<String> paramNames=req.getParameterNames();
            while (paramNames.hasMoreElements()){
                String paraName=paramNames.nextElement();
                String paramValue=req.getParameter(paraName);
                paramMap.put(paraName,paramValue);
            }
            String body= CodecUtil.decodeURL(StreamUtil.getString(req.getInputStream()));
            if(StringUtil.isNotEmpty(body)){
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
                    String json= JsonUtil.toJson(model);
                    writer.write(json);
                    writer.flush();
                    writer.close();
                }
            }

        }
    }
}
