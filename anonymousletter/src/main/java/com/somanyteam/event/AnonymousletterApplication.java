package com.somanyteam.event;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.somanyteam.event.mapper")
@SpringBootApplication
public class AnonymousletterApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnonymousletterApplication.class, args);
    }

}
