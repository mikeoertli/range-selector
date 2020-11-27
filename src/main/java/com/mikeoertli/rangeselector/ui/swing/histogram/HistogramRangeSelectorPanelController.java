package com.mikeoertli.rangeselector.ui.swing.histogram;

import com.mikeoertli.rangeselector.api.IRangeSelectionListener;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.ui.swing.ARangeSelectionPanel;
import com.mikeoertli.rangeselector.ui.swing.ASwingRangeViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The controller for a swing based range selection panel
 *
 * @since 0.0.1
 */
public class HistogramRangeSelectorPanelController extends ASwingRangeViewController
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public HistogramRangeSelectorPanelController(RangeConfiguration rangeConfiguration, IRangeSelectionListener selectionListener)
    {
        super(rangeConfiguration, selectionListener);
    }

    @Override
    protected ARangeSelectionPanel createPanel()
    {
        HistogramSelectionPanel panel = new HistogramSelectionPanel(this);
        final String primaryLabel = rangeConfiguration.getPrimaryLabel();
        if (StringUtils.hasText(primaryLabel))
        {
            panel.setPrimaryLabelText(primaryLabel);
        }

        final String secondaryLabel = rangeConfiguration.getSecondaryLabel();
        if (StringUtils.hasText(secondaryLabel))
        {
            panel.setSecondaryLabelText(secondaryLabel);
        }

        return panel;
    }

    int getNumBins()
    {
        return rangeConfiguration.getDataSize();
    }

    @Override
    public int getMinimumViewWidth()
    {
        return getNumBins(); // min is 1px per bin, so just return that here
    }

    /**
     * @return the primary data set normalized to 100.0 (normalizes against the max of primary AND secondary data)
     */
    public List<Integer> getScaledPrimaryData()
    {
        return getScaledData(rangeConfiguration.getPrimaryData());
    }

    /**
     * @return the secondary data set normalized to 100.0 (normalizes against the max of primary AND secondary data)
     */
    public List<Integer> getScaledSecondaryData()
    {
        return getScaledData(rangeConfiguration.getSecondaryData());
    }

    @Override
    public void onViewConfigurationChanged()
    {
        super.onViewConfigurationChanged();

        if (panel != null)
        {
            ((HistogramSelectionPanel) panel).updateLegend();
        }
    }

    private List<Integer> getScaledData(List<Integer> dataSet)
    {
        final int maxValue = rangeConfiguration.getLargestValueFromDataSets();

        // Normalize to 100.0
        return dataSet.stream()
                .map(val -> (1.0 * (maxValue - val) / maxValue) * 100.0)
                .map(Double::intValue)
                .collect(Collectors.toList());
    }
}
