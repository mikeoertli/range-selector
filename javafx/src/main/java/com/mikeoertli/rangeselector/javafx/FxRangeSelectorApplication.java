package com.mikeoertli.rangeselector.javafx;

import com.mikeoertli.rangeselector.Constants;
import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.api.IRangeViewControllerProvider;
import com.mikeoertli.rangeselector.api.IRangeViewProviderRegistry;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.data.rangetype.FrequencyUnits;
import com.mikeoertli.rangeselector.data.rangetype.SimpleCount;
import com.mikeoertli.rangeselector.javafx.provider.FxHistogramViewControllerProvider;
import com.mikeoertli.rangeselector.javafx.ui.AJavaFxRangeViewController;
import com.mikeoertli.rangeselector.javafx.ui.histogram.HistogramSelectionFxPaneController;
import com.mikeoertli.rangeselector.javafx.ui.simple.SimpleFxController;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;

import static com.mikeoertli.rangeselector.data.DataUtilities.buildRandomDataSet;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mikeoertli.rangeselector.core", "com.mikeoertli.rangeselector.javafx"})
public class FxRangeSelectorApplication extends Application
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private HistogramSelectionFxPaneController histogramController;
    private HistogramSelectionFxPaneController smallHistogramController;
    private SimpleFxController simpleFxController;
    private final IRangeViewProviderRegistry registry;

    public static void main(String[] args)
    {
        final ApplicationContext context = new SpringApplicationBuilder(FxRangeSelectorApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
        Application.launch(args);
    }

    @Autowired
    public FxRangeSelectorApplication(IRangeViewProviderRegistry registry)
    {
        this.registry = registry;
//        this.registry = context.getBean(IRangeViewProviderRegistry.class);
    }

    public void initializeApplication()
    {
        final Optional<IRangeViewControllerProvider<? extends IRangeViewController>> histogramViewControllerProvider =
                registry.getRangeViewControlProvider(FrequencyUnits.MHZ, GuiFrameworkType.JAVA_FX);

        final int numBars = 150;

        histogramViewControllerProvider.ifPresent(provider -> {
            if (provider instanceof FxHistogramViewControllerProvider)
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

    @Override
    public void start(Stage primaryStage) throws Exception
    {
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
    }

    @Override
    public void init() throws Exception
    {
        initializeApplication();
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
}
