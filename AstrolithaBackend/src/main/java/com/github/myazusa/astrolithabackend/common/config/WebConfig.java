package com.github.myazusa.astrolithabackend.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.resource.location.rag.origin}")
    private String resourceLocation;

    @Value("${app.resource.location.rag.mapping}")
    private String resourceLocationHttpMapping;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourceLocationHttpMapping)
                .addResourceLocations(resourceLocation)
                .setCacheControl(CacheControl.maxAge(Duration.ofDays(30)))
                .resourceChain(true);
    }
}
