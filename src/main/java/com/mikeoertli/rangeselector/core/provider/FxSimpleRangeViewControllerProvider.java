package com.mikeoertli.rangeselector.core.provider;

import com.mikeoertli.rangeselector.api.IRangeSelectionListener;
import com.mikeoertli.rangeselector.api.IRangeType;
import com.mikeoertli.rangeselector.api.IRangeViewControllerProvider;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.data.rangetype.SimpleCount;
import com.mikeoertli.rangeselector.ui.javafx.simple.SimpleFxController;
import org.springframework.stereotype.Component;

/**
 * Provider of
 *
 * @since 0.0.2
 */
@Component
public class FxSimpleRangeViewControllerProvider implements IRangeViewControllerProvider<SimpleFxController>
{

    @Override
    public SimpleFxController createViewController(RangeConfiguration rangeConfiguration, IRangeSelectionListener selectionListener)
    {
        SimpleFxController controller = new SimpleFxController();
        if (rangeConfiguration != null)
        {
            controller.restoreState(rangeConfiguration);
        }
        return controller;
    }

    @Override
    public boolean isConfigurationSupported(IRangeType rangeType, GuiFrameworkType guiFrameworkType)
    {
        return rangeType instanceof SimpleCount && guiFrameworkType == GuiFrameworkType.JAVA_FX;
    }

    @Override
    public String getDescription()
    {
        return "Simple JavaFX Range Selection";
    }
}
