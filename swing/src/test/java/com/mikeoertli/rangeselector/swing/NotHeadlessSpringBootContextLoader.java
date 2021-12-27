package com.mikeoertli.rangeselector.swing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootContextLoader;

public class NotHeadlessSpringBootContextLoader extends SpringBootContextLoader
{
    @Override
    protected SpringApplication getSpringApplication() {
        SpringApplication application = super.getSpringApplication();
        application.setHeadless(false);
        return application;
    }
}