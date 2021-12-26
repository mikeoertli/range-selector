package com.mikeoertli.rangeselector.ui.javafx.histogram;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class HistogramSelectionFxApplication// extends Application
{

//    public static void main(String[] args)
//    {
//        launch(args);
//    }
//
//    @Override
    public void start(Stage primaryStage) throws IOException
    {

        FXMLLoader loader = new FXMLLoader();
        HistogramSelectionFxPaneController controller = new HistogramSelectionFxPaneController();
        loader.setController(controller);

        File fxmlFile = new File("com/mikeoertli/rangeselector/ui/javafx/histogram/HistogramSelectionFxPane.fxml");
        URL fxmlUrl = fxmlFile.toURI().toURL();
        loader.setLocation(fxmlUrl);

        VBox vbox = loader.load();
        HistogramSelectionFxPaneController histogramController = loader.getController();

        Scene scene = new Scene(vbox);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
