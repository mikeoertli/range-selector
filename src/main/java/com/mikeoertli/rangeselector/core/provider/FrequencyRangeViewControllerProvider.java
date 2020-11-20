package com.mikeoertli.rangeselector.core.provider;

import com.mikeoertli.rangeselector.api.IRangeType;
import com.mikeoertli.rangeselector.api.IRangeViewControllerProvider;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.data.rangetype.FrequencyUnits;
import com.mikeoertli.rangeselector.ui.swing.histogram.HistogramRangeSelectorPanelController;
import org.springframework.stereotype.Component;

/**
 * Manages all things related to a range selection GUI and I/O
 *
 * @since 0.0.1
 */
@Component
public class FrequencyRangeViewControllerProvider implements IRangeViewControllerProvider<HistogramRangeSelectorPanelController>
{

    @Override
    public HistogramRangeSelectorPanelController createViewController(GuiFrameworkType guiFramework, RangeConfiguration rangeConfiguration)
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
            throw new IllegalArgumentException(guiFramework + " is not a supported GUI framework.");
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
