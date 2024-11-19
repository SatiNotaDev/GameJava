package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.lang.NonNull;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
    
    @Override
    public void configureViewResolvers(@NonNull ViewResolverRegistry registry) {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        registry.viewResolver(resolver);
    }

    @Override
    public void addResourceHandlers(@NonNull ResourceHandlerRegistry registry) {
        // Статические ресурсы
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
                
        // CSS файлы
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/");
                
        // JavaScript файлы
        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/");
    }
}