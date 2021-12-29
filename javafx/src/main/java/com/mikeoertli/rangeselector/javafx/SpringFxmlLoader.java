package com.mikeoertli.rangeselector.javafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Will load the FXML hierarchy as specified in the load method and register
 * Spring as the FXML Controller Factory. Allows Spring and Java FX to coexist
 * once the Spring Application context has been bootstrapped.
 * <p>
 * Borrowed heavily from: https://github.com/RamAlapure/JavaFXSpringBootApp/blob/master/src/main/java/com/codetreatise/config/SpringFXMLLoader.java
 *
 * @since 0.1.0
 */
@Component
public class SpringFxmlLoader
{
    private final ResourceBundle resourceBundle;
    private final ApplicationContext context;

    @Autowired
    public SpringFxmlLoader(ApplicationContext context, ResourceBundle resourceBundle)
    {
        this.resourceBundle = resourceBundle;
        this.context = context;
    }

    public Parent load(String fxmlPath) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(context::getBean); // Uses the Spring app context to get the FXML controllers
        loader.setResources(resourceBundle);
        loader.setLocation(getClass().getResource(fxmlPath));
        return loader.load();
    }
}
