package com.zhou.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletRegistration;

/*
    AbstractAnnotationConfigDispatcherServletInitializer 会创建DispatcherServlet 和 ContextLoaderListener。
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    protected String[] getServletMappings() {
        return new String[]{"/"};
    }


    /*
     这个方法tomcat启动就会调用,可以代替web.xml注册filter,listener,servlet等
     */
    //@Override
    //public void onStartup(ServletContext container) throws ServletException {
    //    //config log4j
    //    container.setInitParameter("log4jConfigLocation", "classpath:log4j.properties");
    //    container.addListener(Log4jConfigListener.class);
    //    super.onStartup(container);
    //}

    /*
     为程序添加Filter
     */
    //@Override
    //protected Filter[] getServletFilters() {
    //    return new Filter[] { new MyFilter() };
    //}

    /*
    处理 Multipart ,多文件上传
    可以使用 MultipartConfigElement 的多种构造器来设置其他参数
     */
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        registration.setMultipartConfig(new MultipartConfigElement("E:\\fileupload\\"));
    }
}