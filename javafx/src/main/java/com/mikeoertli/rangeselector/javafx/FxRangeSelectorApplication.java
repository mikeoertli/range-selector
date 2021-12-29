package com.mikeoertli.rangeselector.javafx;

import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.api.IRangeViewControllerProvider;
import com.mikeoertli.rangeselector.api.IRangeViewProviderRegistry;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.data.rangetype.FrequencyUnits;
import com.mikeoertli.rangeselector.data.rangetype.SimpleCount;
import com.mikeoertli.rangeselector.javafx.provider.FxHistogramViewControllerProvider;
import com.mikeoertli.rangeselector.javafx.ui.AJavaFxRangeViewController;
import com.mikeoertli.rangeselector.javafx.ui.FxmlView;
import com.mikeoertli.rangeselector.javafx.ui.StageManager;
import com.mikeoertli.rangeselector.javafx.ui.histogram.HistogramSelectionFxPaneController;
import com.mikeoertli.rangeselector.javafx.ui.simple.SimpleFxController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static com.mikeoertli.rangeselector.data.DataUtilities.buildRandomDataSet;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mikeoertli.rangeselector.core", "com.mikeoertli.rangeselector.javafx"})
public class FxRangeSelectorApplication extends Application
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private HistogramSelectionFxPaneController histogramController;
    private HistogramSelectionFxPaneController smallHistogramController;
    private SimpleFxController simpleFxController;
    private StageManager stageManager;
    private static ApplicationContext context;

    public static void main(String[] args)
    {
        CompletableFuture.supplyAsync(() -> context = SpringApplication.run(FxRangeSelectorApplication.class, args))
                .whenComplete((ctx, throwable) -> {
                    if (throwable != null)
                    {
                        logger.error("Failed to load spring application context: ", throwable);
                    } else
                    {
                        logger.debug("Spring context successfully initialized, launching JavaFX app: {}", ctx.getApplicationName());
                        Application.launch(args);
                    }
                }).thenAccept(applicationContext -> logger.debug("JavaFX application launch complete: {}", applicationContext.getApplicationName()));
    }

    public FxRangeSelectorApplication()
    {
        logger.debug("Creating FxRangeSelectorApplication...");
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        stageManager = Objects.requireNonNull(context.getBean(StageManager.class, primaryStage), "Unable to find the StageManager");
        stageManager.switchScene(FxmlView.MAIN_MENU);
    }

    @Override
    public void init() throws Exception
    {
        IRangeViewProviderRegistry registry = context.getBean(IRangeViewProviderRegistry.class);

        logger.trace("init -- registry initialized");

        final Optional<IRangeViewControllerProvider<? extends IRangeViewController>> histogramViewControllerProvider =
                registry.getRangeViewControlProvider(FrequencyUnits.MHZ, GuiFrameworkType.JAVA_FX);

        final int numBars = 150;

        histogramViewControllerProvider.ifPresent(provider -> {
            logger.trace("init -- initializing the histogram view controller provider...");
            if (provider instanceof FxHistogramViewControllerProvider)
            {
                List<Integer> primaryData = buildRandomDataSet(numBars, 20);
                List<Integer> secondaryData = buildRandomDataSet(numBars, 5);
                RangeConfiguration rangeConfiguration = new RangeConfiguration(0, numBars,
                        "Frequency (MHz)", "Frequency", primaryData, secondaryData,
                        "# Targets", "# Detections");

                histogramController = ((FxHistogramViewControllerProvider) provider).createViewController(rangeConfiguration);

                List<Integer> smallPrimary = buildRandomDataSet(5, 20);
                List<Integer> smallSecondary = buildRandomDataSet(5, 5);
                RangeConfiguration smallRangeConfig = new RangeConfiguration(0, numBars,
                        "Frequency (MHz)", "Frequency", smallPrimary, smallSecondary,
                        "# Targets", "# Detections");

                smallHistogramController = ((FxHistogramViewControllerProvider) provider).createViewController(smallRangeConfig);
            }
        });

        final Optional<IRangeViewControllerProvider<? extends IRangeViewController>> simpleProvider =
                registry.getRangeViewControlProvider(SimpleCount.BASIC, GuiFrameworkType.SWING);

        simpleProvider.ifPresent(provider -> {
            logger.trace("init -- initializing the simple view controller provider...");
            RangeConfiguration config = new RangeConfiguration();
            simpleFxController = (SimpleFxController) provider.createViewController(config);
        });
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
