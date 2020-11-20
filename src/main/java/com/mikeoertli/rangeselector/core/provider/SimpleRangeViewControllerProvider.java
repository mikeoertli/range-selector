package com.mikeoertli.rangeselector.core.provider;

import com.mikeoertli.rangeselector.api.IRangeType;
import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.api.IRangeViewControllerProvider;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.data.rangetype.SimpleCount;
import com.mikeoertli.rangeselector.ui.swing.simple.SimpleRangeSelectorPanelController;
import org.springframework.stereotype.Component;

/**
 * Provider of
 *
 * @since 0.0.2
 */
@Component
public class SimpleRangeViewControllerProvider implements IRangeViewControllerProvider<SimpleRangeSelectorPanelController>
{

    @Override
    public SimpleRangeSelectorPanelController createViewController(GuiFrameworkType guiFramework, RangeConfiguration rangeConfiguration)
    {
        SimpleRangeSelectorPanelController controller = new SimpleRangeSelectorPanelController();
        if (rangeConfiguration != null)
        {
            controller.restoreState(rangeConfiguration);
        }
        return controller;
    }

    @Override
    public boolean isConfigurationSupported(IRangeType rangeType, GuiFrameworkType guiFrameworkType)
    {
        return rangeType instanceof SimpleCount && guiFrameworkType == GuiFrameworkType.SWING;
    }

    @Override
    public String getDescription()
    {
        return "Simple Range Selection";
    }
}
