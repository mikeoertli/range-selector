package com.mikeoertli.rangeselector;

import com.mikeoertli.rangeselector.ui.swing.PlaygroundUi;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class RangeSelectorApplication {

	public static void main(String[] args) {
		ApplicationContext context = new SpringApplicationBuilder(RangeSelectorApplication.class)
				.web(WebApplicationType.NONE)
				.headless(false)
				.bannerMode(Banner.Mode.OFF)
				.run(args);
	}

	public RangeSelectorApplication()
	{
		PlaygroundUi playgroundUi = new PlaygroundUi();
	}
}
