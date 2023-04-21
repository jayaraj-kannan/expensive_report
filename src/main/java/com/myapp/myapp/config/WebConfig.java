package com.myapp.myapp.config;

import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/access-denied").setViewName("access-denied");
        registry.addViewController("/").setViewName("homepage");
        registry.addViewController("/about-us").setViewName("about-us");
    }
}
