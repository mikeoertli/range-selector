package com.mikeoertli.rangeselector.demo.javafx;

import com.mikeoertli.rangeselector.Constants;
import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.api.IRangeViewControllerProvider;
import com.mikeoertli.rangeselector.api.IRangeViewProviderRegistry;
import com.mikeoertli.rangeselector.core.provider.FxHistogramViewControllerProvider;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.data.rangetype.FrequencyUnits;
import com.mikeoertli.rangeselector.data.rangetype.SimpleCount;
import com.mikeoertli.rangeselector.ui.javafx.AJavaFxRangeViewController;
import com.mikeoertli.rangeselector.ui.javafx.histogram.HistogramSelectionFxPaneController;
import com.mikeoertli.rangeselector.ui.javafx.simple.SimpleFxController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Demo application of a third party app that uses JavaFX
 *
 * @since 0.1.0
 */
@Component
public class ThirdPartyFxApp extends Application
{

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private HistogramSelectionFxPaneController histogramController;
    private HistogramSelectionFxPaneController smallHistogramController;
    private SimpleFxController simpleFxController;
    private final IRangeViewProviderRegistry registry;

//    @Autowired
//    private final IRangeViewProviderRegistry registry;

    @Autowired
    public ThirdPartyFxApp(IRangeViewProviderRegistry registry)
    {
        this.registry = registry;
    }

    public void initializeApplication(IRangeViewProviderRegistry registry)
    {
        final Optional<IRangeViewControllerProvider<? extends IRangeViewController>> histogramViewControllerProvider =
                registry.getRangeViewControlProvider(FrequencyUnits.MHZ, GuiFrameworkType.JAVA_FX);

        final int numBars = 150;

        histogramViewControllerProvider.ifPresent(provider -> {
            if (provider instanceof HistogramSelectionFxPaneController)
            {
                List<Integer> primaryData = buildRandomDataSet(numBars, 20);
                List<Integer> secondaryData = buildRandomDataSet(numBars, 5);
                RangeConfiguration rangeConfiguration = new RangeConfiguration(0, numBars,
                        "Frequency (MHz)", "Frequency", primaryData, secondaryData,
                        "# Targets", "# Detections");

                histogramController = ((FxHistogramViewControllerProvider) provider).createViewController(rangeConfiguration, null);

                List<Integer> smallPrimary = buildRandomDataSet(5, 20);
                List<Integer> smallSecondary = buildRandomDataSet(5, 5);
                RangeConfiguration smallRangeConfig = new RangeConfiguration(0, numBars,
                        "Frequency (MHz)", "Frequency", smallPrimary, smallSecondary,
                        "# Targets", "# Detections");

                smallHistogramController = ((FxHistogramViewControllerProvider) provider).createViewController(smallRangeConfig, null);
            }
        });

        final Optional<IRangeViewControllerProvider<? extends IRangeViewController>> simpleProvider =
                registry.getRangeViewControlProvider(SimpleCount.BASIC, GuiFrameworkType.SWING);

        simpleProvider.ifPresent(provider -> {
            RangeConfiguration config = new RangeConfiguration();
            simpleFxController = (SimpleFxController) provider.createViewController(config, null);
        });
    }

    private List<Integer> buildRandomDataSet(int numDataPoints, int maxValue)
    {
        List<Integer> dataSet = new ArrayList<>();

        for (int i = 0; i < numDataPoints; i++)
        {
            dataSet.add(ThreadLocalRandom.current().nextInt(0, maxValue));
        }

        return dataSet;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
//        Stage demoStage = new Stage();
        primaryStage.setTitle(Constants.DEMO_APP_NAME);

        final Button simpleButton = new Button("Simple");
        final Button histogramButton = new Button("Histogram");
        final Button largeHistogramButton = new Button("Large");

        simpleButton.setOnMouseClicked(event -> showSimpleDemo(primaryStage));
        histogramButton.setOnMouseClicked(event -> showSmallHistogramDemo(primaryStage));
        largeHistogramButton.setOnMouseClicked(event -> showLargeHistogramDemo(primaryStage));

        final Group buttonGroup = new Group(simpleButton, histogramButton, largeHistogramButton);

        final Scene mainScene = new Scene(buttonGroup, 600, 400);
        primaryStage.setScene(mainScene);
        primaryStage.show();

//        DemoSelectionDialog demoSelectionDialog = new DemoSelectionDialog(this);
//        demoSelectionDialog.setTitle(Constants.DEMO_APP_NAME);
//        demoSelectionDialog.setVisible(true);
    }

    @Override
    public void init() throws Exception
    {
//        super.init();
        initializeApplication(registry);
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    public void runDemo()
    {

    }

    public void showSimpleDemo(Stage primaryStage)
    {
        showDialog(simpleFxController, "Simple Demo", primaryStage);
    }

    public void showSmallHistogramDemo(Stage primaryStage)
    {
        showDialog(smallHistogramController, "Demo Histogram (SMALL)", primaryStage);
    }

    public void showLargeHistogramDemo(Stage primaryStage)
    {
        showDialog(histogramController, "Demo Histogram", primaryStage);
    }

    protected void showDialog(AJavaFxRangeViewController controller, String panelTitle, Stage primaryStage)
    {

            if (controller != null)
            {
                final Scene scene = controller.createScene(primaryStage.getMaxWidth(), primaryStage.getMaxHeight());
                primaryStage.setTitle(panelTitle);
                primaryStage.setScene(scene);
            } else
            {
                logger.error("Invalid configuration, could not create range selection view for {}", panelTitle);
            }

    }

    public static void main(String[] args) throws Exception
    {
        launch(args);
    }
}
