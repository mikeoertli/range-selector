package com.mikeoertli.rangeselector.javafx.ui.histogram;

import com.mikeoertli.rangeselector.api.IRangeSelectionListener;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.javafx.ui.AFxRangeSelectionPane;
import com.mikeoertli.rangeselector.javafx.ui.AJavaFxRangeViewController;

/**
 * Histogram range selection panel for JavaFX
 *
 * @since 0.1.0
 */
public class HistogramSelectionFxPaneController extends AJavaFxRangeViewController
{
    public HistogramSelectionFxPaneController(RangeConfiguration rangeConfiguration, IRangeSelectionListener selectionListener)
    {
        super(rangeConfiguration, selectionListener);
    }

    public HistogramSelectionFxPaneController()
    {
    }

    @Override
    public int getMinimumViewWidth()
    {
        return getNumBins();
    }

    @Override
    protected AFxRangeSelectionPane createPane()
    {
        final HistogramSelectionFxPane pane = new HistogramSelectionFxPane(this);
        return pane;
    }

    int getNumBins()
    {
        return rangeConfiguration.getDataSize();
    }
}
