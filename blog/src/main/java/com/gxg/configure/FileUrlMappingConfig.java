package com.gxg.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 磁盘资源与url映射配置
 * @author 郭欣光
 * @date 2018/10/15 15:34
 */

@Configuration
public class FileUrlMappingConfig extends WebMvcConfigurerAdapter {

    @Value("${blog.base.dir}")
    private String blogBaseDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/blog_resource/**").addResourceLocations("file:" + blogBaseDir);
    }
}
