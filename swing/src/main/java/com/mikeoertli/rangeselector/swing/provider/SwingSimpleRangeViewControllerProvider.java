package com.mikeoertli.rangeselector.swing.provider;

import com.mikeoertli.rangeselector.api.IRangeSelectionListener;
import com.mikeoertli.rangeselector.api.IRangeType;
import com.mikeoertli.rangeselector.api.IRangeViewControllerProvider;
import com.mikeoertli.rangeselector.api.IRangeViewProviderRegistry;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.data.rangetype.SimpleCount;
import com.mikeoertli.rangeselector.swing.ui.simple.SimpleRangeSelectionPanelController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Provider of simple range view controllers for Swing GUIs
 *
 * @since 0.0.2
 */
@Component
public class SwingSimpleRangeViewControllerProvider implements IRangeViewControllerProvider<SimpleRangeSelectionPanelController>
{

    @Autowired
    public SwingSimpleRangeViewControllerProvider(IRangeViewProviderRegistry registry)
    {
        registry.registerRangeViewControlProvider(this);
    }

    @Override
    public SimpleRangeSelectionPanelController createViewController(RangeConfiguration rangeConfiguration, IRangeSelectionListener selectionListener)
    {
        SimpleRangeSelectionPanelController controller = new SimpleRangeSelectionPanelController();
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
