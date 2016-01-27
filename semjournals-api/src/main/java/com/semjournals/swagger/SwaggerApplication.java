package com.semjournals.swagger;

import io.swagger.jaxrs.config.BeanConfig;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class SwaggerApplication extends HttpServlet {

    private static final long serialVersionUID = -6039834823506457822L;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        BeanConfig beanConfig = new BeanConfig();

        final String contextPath = config.getServletContext().getContextPath();
        final StringBuilder sbBasePath = new StringBuilder(); 
        sbBasePath.append(contextPath);
        sbBasePath.append("/v1");
        beanConfig.setBasePath(sbBasePath.toString());

        // API Info
        beanConfig.setVersion("1.0-SNAPSHOT");
        beanConfig.setTitle("SEMJournals API");
        beanConfig.setResourcePackage("com.semjournals.rest.resources");
        beanConfig.setScan(true);
    }
}