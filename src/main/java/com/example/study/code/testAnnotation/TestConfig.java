package com.example.study.code.testAnnotation;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
//@AutoConfiguration
public class TestConfig {
    @Bean
    public Student test() {
        System.out.println("student");
        return new Student();
    }
}
