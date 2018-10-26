package com.gxg.configure;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

/**
 * 常用错误页面配置
 * @author 郭欣光
 * @date 2018/10/26 17:11
 */

@Configuration
public class ErrorPageConfig {

    /**
     * 配置默认错误页面（仅用于内嵌tomcatq启动时）
     * 使用这种方式，在打包为war后不起作用
     * @return
     * @author 郭欣光
     */
    @Bean
    public WebServerFactoryCustomizer<ConfigurableWebServerFactory> webServerFactoryCustomizer() {
        return (container -> {
            ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
            ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500");
            container.addErrorPages(error404Page, error500Page);
        });
    }
}
