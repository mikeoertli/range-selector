package com.mikeoertli.rangeselector.core.provider;

import com.mikeoertli.rangeselector.api.IRangeSelectionListener;
import com.mikeoertli.rangeselector.api.IRangeType;
import com.mikeoertli.rangeselector.api.IRangeViewControllerProvider;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.data.rangetype.FrequencyUnits;
import com.mikeoertli.rangeselector.ui.javafx.histogram.HistogramSelectionFxPaneController;
import org.springframework.stereotype.Component;

/**
 * Manages all things related to a range selection GUI and I/O
 *
 * @since 0.0.1
 */
@Component
public class FxHistogramViewControllerProvider implements IRangeViewControllerProvider<HistogramSelectionFxPaneController>
{

    @Override
    public HistogramSelectionFxPaneController createViewController(RangeConfiguration rangeConfiguration, IRangeSelectionListener selectionListener)
    {
        return new HistogramSelectionFxPaneController(rangeConfiguration, selectionListener);
    }

    @Override
    public boolean isConfigurationSupported(IRangeType rangeType, GuiFrameworkType guiFrameworkType)
    {
        return rangeType instanceof FrequencyUnits && GuiFrameworkType.JAVA_FX == guiFrameworkType;
    }

    @Override
    public String getDescription()
    {
        return "Frequency Histogram for JavaFX";
    }
}
