package com.mikeoertli.rangeselector;

import com.mikeoertli.rangeselector.ui.swing.simple.SimpleRangeSelectorPanelController;
import org.springframework.boot.Banner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

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
        JDialog dialog = new JDialog();
        dialog.setSize(400, 100);
        SimpleRangeSelectorPanelController controller = new SimpleRangeSelectorPanelController();
        dialog.add(controller.getPanel());
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
