package com.ashish.wallet.config;

import com.ashish.wallet.exception.WalletExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan({"com.ashish.accounttransactionservice", "com.ashish.wallet"})
public class WalletConfig implements WebMvcConfigurer {

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(new WalletExceptionHandler());
    }

    @Bean
    public ContentNegotiatingViewResolver contentNegotiatingViewResolver() {
        var contentNegotiatingViewResolver = new ContentNegotiatingViewResolver();
        var contentNegotiationManagerFactoryBean = new ContentNegotiationManagerFactoryBean();
        contentNegotiatingViewResolver.setContentNegotiationManager(contentNegotiationManagerFactoryBean.getObject());
        List<View> defaultViews = new ArrayList<>();
        defaultViews.add(new MappingJackson2JsonView());
        contentNegotiatingViewResolver.setDefaultViews(defaultViews);
        contentNegotiatingViewResolver.setViewResolvers(new ArrayList<>());
        return contentNegotiatingViewResolver;
    }

}
