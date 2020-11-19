package com.mikeoertli.rangeselector;

import com.mikeoertli.rangeselector.ui.swing.histogram.FrequencyRangeSelectorPanelController;
import com.mikeoertli.rangeselector.ui.swing.simple.SimpleRangeSelectorPanelController;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import javax.swing.JDialog;
import javax.swing.WindowConstants;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class RangeSelectorApplication
{

    public static void main(String[] args)
    {
        ApplicationContext context = new SpringApplicationBuilder(RangeSelectorApplication.class)
                .web(WebApplicationType.NONE)
                .headless(false)
                .bannerMode(Banner.Mode.OFF)
                .run(args);
    }

    public RangeSelectorApplication()
    {
        JDialog simpleDialog = new JDialog();
        simpleDialog.setSize(400, 100);
        SimpleRangeSelectorPanelController simpleController = new SimpleRangeSelectorPanelController();
        simpleDialog.add(simpleController.getPanel());
        simpleDialog.setVisible(true);
        simpleDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JDialog histogramDialog = new JDialog();
        histogramDialog.setSize(400, 200);
        FrequencyRangeSelectorPanelController histogramController = new FrequencyRangeSelectorPanelController();
        List<Integer> primaryData = Arrays.asList(10, 5, 15, 2, 14, 3, 0, 9);
        List<Integer> secondaryData = Arrays.asList(1, 8, 0, 0, 20, 12, 4, 13);
        histogramController.setPrimaryDataPoints(primaryData);
        histogramController.setSecondaryDataPoints(secondaryData);
        histogramDialog.add(histogramController.getPanel());
        histogramDialog.setVisible(true);
        histogramDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
