package com.mikeoertli.rangeselector.core;

import com.mikeoertli.rangeselector.api.IRangeType;
import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.api.IRangeViewControllerProvider;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.data.rangetype.FrequencyUnits;
import com.mikeoertli.rangeselector.ui.swing.histogram.HistogramRangeSelectorPanelController;

import javax.naming.OperationNotSupportedException;

/**
 * Manages all things related to a range selection GUI and I/O
 *
 * @since 0.0.1
 */
public class FrequencyRangeViewControllerProvider implements IRangeViewControllerProvider
{

    @Override
    public IRangeViewController createViewController(GuiFrameworkType guiFramework, RangeConfiguration rangeConfiguration) throws OperationNotSupportedException
    {
        if (GuiFrameworkType.SWING == guiFramework)
        {
            final HistogramRangeSelectorPanelController controller = new HistogramRangeSelectorPanelController();
            if (rangeConfiguration != null)
            {
                controller.restoreState(rangeConfiguration);
            }
            return controller;
        } else
        {
            throw new OperationNotSupportedException(guiFramework + " is not a supported GUI framework.");
        }
    }

    @Override
    public boolean isConfigurationSupported(IRangeType rangeType, GuiFrameworkType guiFrameworkType)
    {
        return rangeType instanceof FrequencyUnits && GuiFrameworkType.SWING == guiFrameworkType;
    }

    @Override
    public String getDescription()
    {
        return "Frequency Histogram";
    }
}
