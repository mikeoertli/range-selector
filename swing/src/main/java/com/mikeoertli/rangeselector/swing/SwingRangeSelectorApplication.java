package com.mikeoertli.rangeselector.swing;

import com.mikeoertli.rangeselector.swing.demo.ThirdPartySwingApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mikeoertli.rangeselector.core", "com.mikeoertli.rangeselector.swing"})
public class SwingRangeSelectorApplication
{

    public static void main(String[] args)
    {
        ApplicationContext context = new SpringApplicationBuilder(SwingRangeSelectorApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    @Autowired
    public SwingRangeSelectorApplication(ThirdPartySwingApp thirdPartySwingApp)
    {
        thirdPartySwingApp.runDemo();
    }
}
