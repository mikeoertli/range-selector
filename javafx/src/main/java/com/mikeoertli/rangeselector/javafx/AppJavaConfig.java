package com.mikeoertli.rangeselector.javafx;

import com.mikeoertli.rangeselector.javafx.ui.StageManager;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ResourceBundle;

/**
 * Borrowed heavily from: https://github.com/RamAlapure/JavaFXSpringBootApp/blob/master/src/main/java/com/codetreatise/config/AppJavaConfig.java
 *
 * @since 0.1.0
 */
@Configuration
public class AppJavaConfig
{

    @Autowired
    SpringFxmlLoader springFxmlLoader;

    /**
     * Useful when dumping stack trace to a string for logging.
     *
     * @return ExceptionWriter contains logging utility methods
     */
    @Bean
    @Scope("prototype")
    public StringWriter exceptionWriter()
    {
        return new StringWriter();
    }

    @Bean
    public ResourceBundle resourceBundle()
    {
        return ResourceBundle.getBundle("Bundle");
    }

    @Bean
    @Lazy(value = true) //Stage only created after Spring context bootstap
    public StageManager stageManager(Stage stage) throws IOException
    {
        return new StageManager(springFxmlLoader, stage);
    }
}