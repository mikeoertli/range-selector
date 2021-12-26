package com.mikeoertli.rangeselector;

import com.mikeoertli.rangeselector.swing.demo.ThirdPartySwingApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

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

    @Autowired
    public RangeSelectorApplication(ThirdPartySwingApp thirdPartySwingApp)
    {
        thirdPartySwingApp.runDemo();

//        JDialog simpleDialog = new JDialog();
//        simpleDialog.setSize(400, 100);
//        SimpleRangeSelectorPanelController simpleController = new SimpleRangeSelectorPanelController();
//        simpleDialog.add(simpleController.getPanel());
//        simpleDialog.setVisible(true);
//        simpleDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//
//        JDialog histogramDialog = new JDialog();
//        histogramDialog.setSize(400, 200);
//        HistogramRangeSelectorPanelController histogramController = new HistogramRangeSelectorPanelController();
//        List<Integer> primaryData = Arrays.asList(10, 5, 15, 2, 14, 3, 0, 9);
//        List<Integer> secondaryData = Arrays.asList(1, 8, 0, 0, 20, 12, 4, 13);
//        histogramController.setPrimaryDataPoints(primaryData);
//        histogramController.setSecondaryDataPoints(secondaryData);
//        histogramDialog.add(histogramController.getPanel());
//        histogramDialog.setVisible(true);
//        histogramDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
