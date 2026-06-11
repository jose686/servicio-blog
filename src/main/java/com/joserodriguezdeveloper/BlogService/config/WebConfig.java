package com.joserodriguezdeveloper.BlogService.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Esto mapea la URL /uploads/** a la carpeta física static/uploads/
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:static/uploads/");
    }
}