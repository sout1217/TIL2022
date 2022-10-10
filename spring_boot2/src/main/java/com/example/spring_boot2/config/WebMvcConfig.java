package com.example.spring_boot2.config;

import com.example.spring_boot2.config.coverter.SortTypeFormatter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Bean
    public SortTypeFormatter sortTypeFormatter() {
        return new SortTypeFormatter();
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(sortTypeFormatter());
    }
}
