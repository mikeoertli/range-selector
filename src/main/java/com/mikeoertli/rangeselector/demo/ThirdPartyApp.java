package com.mikeoertli.rangeselector.demo;

import com.mikeoertli.rangeselector.Constants;
import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.api.IRangeViewControllerProvider;
import com.mikeoertli.rangeselector.api.IRangeViewProviderRegistry;
import com.mikeoertli.rangeselector.core.provider.FrequencyRangeViewControllerProvider;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.data.rangetype.FrequencyUnits;
import com.mikeoertli.rangeselector.data.rangetype.SimpleCount;
import com.mikeoertli.rangeselector.demo.ui.DemoSelectionDialog;
import com.mikeoertli.rangeselector.ui.swing.ASwingRangeViewController;
import com.mikeoertli.rangeselector.ui.swing.histogram.HistogramRangeSelectorPanelController;
import com.mikeoertli.rangeselector.ui.swing.simple.SimpleRangeSelectorPanelController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.Image;
import java.awt.Taskbar;
import java.awt.Toolkit;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Application to test drive the range selection API
 *
 * @since 0.0.2
 */
@Component
public class ThirdPartyApp
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private HistogramRangeSelectorPanelController controller;
    private HistogramRangeSelectorPanelController smallSimController;
    private SimpleRangeSelectorPanelController simpleController;

    @Autowired
    public ThirdPartyApp(IRangeViewProviderRegistry registry)
    {
        final Optional<IRangeViewControllerProvider<? extends IRangeViewController>> frequencyRangeSelectionProvider =
                registry.getRangeViewControlProvider(FrequencyUnits.MHZ, GuiFrameworkType.SWING);

        // The range min/max needs to be the number of bars when dealing with a histogram
        final int numBars = 150;
        frequencyRangeSelectionProvider.ifPresent(provider -> {
            if (provider instanceof FrequencyRangeViewControllerProvider)
            {
                List<Integer> primaryData = buildRandomDataSet(numBars, 0, 20);
                List<Integer> secondaryData = buildRandomDataSet(numBars, 0, 5);
                RangeConfiguration rangeConfiguration = new RangeConfiguration(0, numBars,
                        "Frequency (MHz)", "Frequency", primaryData, secondaryData,
                        "# Targets", "# Detections");

                controller = ((FrequencyRangeViewControllerProvider) provider).createSwingViewController(rangeConfiguration, null);

                List<Integer> smallPrimary = buildRandomDataSet(5, 0, 20);
                List<Integer> smallSecondary = buildRandomDataSet(5, 0, 5);
                RangeConfiguration smallRangeConfig = new RangeConfiguration(0, numBars,
                        "Frequency (MHz)", "Frequency", smallPrimary, smallSecondary,
                        "# Targets", "# Detections");

                smallSimController = ((FrequencyRangeViewControllerProvider) provider).createSwingViewController(smallRangeConfig, null);
            }
        });

        final Optional<IRangeViewControllerProvider<? extends IRangeViewController>> simpleProvider =
                registry.getRangeViewControlProvider(SimpleCount.BASIC, GuiFrameworkType.SWING);

        simpleProvider.ifPresent(provider -> {
            RangeConfiguration config = new RangeConfiguration();
            simpleController = (SimpleRangeSelectorPanelController) provider.createSwingViewController(config, null);
        });
    }

    private List<Integer> buildRandomDataSet(int numDataPoints, int minValue, int maxValue)
    {
        List<Integer> dataSet = new ArrayList<>();

        for (int i = 0; i < numDataPoints; i++)
        {
            dataSet.add(ThreadLocalRandom.current().nextInt(minValue, maxValue));
        }

        return dataSet;
    }

    public void runDemo()
    {
        DemoSelectionDialog demoSelectionDialog = new DemoSelectionDialog(this);
        demoSelectionDialog.setTitle(Constants.DEMO_APP_NAME);
        demoSelectionDialog.setVisible(true);
    }

    public void showSimpleDemo()
    {

        showDialog(simpleController, "Simple Demo");
    }

    public void showSmallHistogramDemo()
    {
        showDialog(smallSimController, "Demo Histogram (SMALL)");
    }

    public void showLargeHistogramDemo()
    {
        showDialog(controller, "Demo Histogram");
    }

    protected void showDialog(ASwingRangeViewController controller, String panelTitle)
    {
        SwingUtilities.invokeLater(() -> {
            if (controller != null)
            {
                final IRangeSelectorView view = controller.getView();
                final JDialog dialog = new JDialog();
                dialog.setTitle(panelTitle);
                final Image image = Toolkit.getDefaultToolkit().getImage(ThirdPartyApp.class.getClassLoader().getResource(Constants.RANGE_SELECTOR_ICON_32X32_PATH));
                dialog.setIconImage(image);
                final Taskbar taskbar = Taskbar.getTaskbar();
                taskbar.setIconImage(image);

                final JPanel panel = (JPanel) view;
                dialog.setSize(panel.getPreferredSize());
                dialog.getContentPane().add(panel);
                dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                dialog.setModal(true);
                dialog.pack();
                dialog.setVisible(true);
            } else
            {
                logger.error("Invalid configuration, could not create range selection view for " + panelTitle);
            }
        });
    }
}
