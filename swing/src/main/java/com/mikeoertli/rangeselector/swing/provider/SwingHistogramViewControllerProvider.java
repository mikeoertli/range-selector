package com.mikeoertli.rangeselector.swing.provider;

import com.mikeoertli.rangeselector.api.IRangeSelectionListener;
import com.mikeoertli.rangeselector.api.IRangeType;
import com.mikeoertli.rangeselector.api.IRangeViewControllerProvider;
import com.mikeoertli.rangeselector.api.IRangeViewProviderRegistry;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.data.rangetype.FrequencyUnits;
import com.mikeoertli.rangeselector.swing.ui.histogram.HistogramRangeSelectorPanelController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Manages all things related to a range selection GUI and I/O
 *
 * @since 0.0.1
 */
@Component
public class SwingHistogramViewControllerProvider implements IRangeViewControllerProvider<HistogramRangeSelectorPanelController>
{
    @Autowired
    public SwingHistogramViewControllerProvider(IRangeViewProviderRegistry registry)
    {
        registry.registerRangeViewControlProvider(this);
    }

    @Override
    public HistogramRangeSelectorPanelController createViewController(RangeConfiguration rangeConfiguration, IRangeSelectionListener selectionListener)
    {
        return new HistogramRangeSelectorPanelController(rangeConfiguration, selectionListener);
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
