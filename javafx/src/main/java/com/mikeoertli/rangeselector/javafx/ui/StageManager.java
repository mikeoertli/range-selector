package com.mikeoertli.rangeselector.javafx.ui;

import com.mikeoertli.rangeselector.javafx.SpringFxmlLoader;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

/**
 * Taken from: https://github.com/RamAlapure/JavaFXSpringBootApp/blob/master/src/main/java/com/codetreatise/config/StageManager.java
 * Manages switching Scenes on the Primary Stage
 *
 * @since 0.1.0
 */
public class StageManager
{

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final Stage primaryStage;
    private final SpringFxmlLoader springFxmlLoader;

    public StageManager(SpringFxmlLoader springFxmlLoader, Stage stage)
    {
        this.springFxmlLoader = springFxmlLoader;
        this.primaryStage = stage;
    }

    public void switchScene(final FxmlView view)
    {
        logger.debug("switchScene to {}", view);
        Parent viewRootNodeHierarchy = loadViewNodeHierarchy(view.getFxmlResourcePath());
        logger.debug("switchScene to {} found parent of: {}", view, viewRootNodeHierarchy.getId());
        show(viewRootNodeHierarchy, view.getTitle());
    }

    private void show(final Parent rootNode, String title)
    {
        Scene scene = prepareScene(rootNode);

        logger.debug("show -- showing node with title: {}", title);

        primaryStage.setTitle(title);
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.centerOnScreen();

        try
        {
            primaryStage.show();
        } catch (Exception exception)
        {
            logAndExit("Unable to show scene for title" + title, exception);
        }
    }

    private Scene prepareScene(Parent rootNode)
    {
        Scene scene = primaryStage.getScene();

        if (scene == null)
        {
            scene = new Scene(rootNode);
        }
        scene.setRoot(rootNode);
        return scene;
    }

    /**
     * Loads the object hierarchy from a FXML document and returns to root node
     * of that hierarchy.
     *
     * @return Parent root node of the FXML document hierarchy
     */
    private Parent loadViewNodeHierarchy(String fxmlFilePath)
    {
        Parent rootNode = null;
        try
        {
            rootNode = springFxmlLoader.load(fxmlFilePath);
            Objects.requireNonNull(rootNode, "A Root FXML node must not be null");
        } catch (Exception exception)
        {
            logAndExit("Unable to load FXML view from path: " + fxmlFilePath, exception);
        }
        return rootNode;
    }

    private void logAndExit(String errorMsg, Exception exception)
    {
        logger.error(errorMsg, exception, exception.getCause());
        Platform.exit();
    }
}