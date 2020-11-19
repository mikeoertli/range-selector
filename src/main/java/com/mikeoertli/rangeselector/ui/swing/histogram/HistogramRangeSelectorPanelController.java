package com.mikeoertli.rangeselector.ui.swing.histogram;

import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import com.mikeoertli.rangeselector.ui.swing.ASwingRangeViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
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
    private final List<Integer> primaryDataPoints = new ArrayList<>();
    private final List<Integer> secondaryDataPoints = new ArrayList<>();

    public HistogramRangeSelectorPanelController()
    {
        super();
    }

    @Override
    protected IRangeSelectorView createPanel()
    {
        return new HistogramSelectionPanel(this);
    }

    public void setPrimaryDataPoints(List<Integer> primary)
    {
        primaryDataPoints.clear();
        primaryDataPoints.addAll(primary);
        refreshPanel();
    }

    public void setSecondaryDataPoints(List<Integer> secondary)
    {
        secondaryDataPoints.clear();
        secondaryDataPoints.addAll(secondary);
        refreshPanel();
    }

    List<Integer> getPrimaryDataPoints()
    {
        return Collections.unmodifiableList(primaryDataPoints);
    }

    List<Integer> getSecondaryDataPoints()
    {
        return Collections.unmodifiableList(secondaryDataPoints);
    }

    public void setLegendLabels(String primary, String secondary)
    {
        ((HistogramSelectionPanel) panel).setPrimaryLabelText(primary);
        ((HistogramSelectionPanel) panel).setSecondaryLabelText(secondary);
    }

    int getNumBins()
    {
        return primaryDataPoints.size();
    }

    @Override
    public void shutdown()
    {
        super.shutdown();
        primaryDataPoints.clear();
        secondaryDataPoints.clear();
    }

    /**
     * @return the primary data set normalized to 100.0 (normalizes against the max of primary AND secondary data)
     */
    public List<Integer> getScaledPrimaryData()
    {
        return getScaledData(primaryDataPoints);
    }

    /**
     * @return the secondary data set normalized to 100.0 (normalizes against the max of primary AND secondary data)
     */
    public List<Integer> getScaledSecondaryData()
    {
        return getScaledData(secondaryDataPoints);
    }

    private List<Integer> getScaledData(List<Integer> dataSet)
    {
        final int maxPrimaryDataPoint = primaryDataPoints.stream().mapToInt(i -> i).max().orElse(0);
        final int maxSecondaryDataPoint = secondaryDataPoints.stream().mapToInt(i -> i).max().orElse(0);
        final int maxValue = Math.max(maxPrimaryDataPoint, maxSecondaryDataPoint);

        // Normalize to 100.0
        return dataSet.stream()
                .map(val -> (1.0 * (maxValue - val) / maxValue) * 100.0)
                .map(Double::intValue)
                .collect(Collectors.toList());
    }
}
