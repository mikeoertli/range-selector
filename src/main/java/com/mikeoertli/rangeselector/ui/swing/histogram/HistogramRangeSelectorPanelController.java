package com.mikeoertli.rangeselector.ui.swing.histogram;

import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import com.mikeoertli.rangeselector.api.IViewStyleProvider;
import com.mikeoertli.rangeselector.ui.swing.ASwingRangeViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.awt.Color;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The controller for a swing based range selection panel
 *
 * @since 0.0.1
 */
public class HistogramRangeSelectorPanelController extends ASwingRangeViewController implements IViewStyleProvider
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int ALPHA = 175; // 0-255
    private static final Color DEFAULT_SELECTED_PRIMARY_COLOR = new Color(Color.CYAN.getRed(), Color.CYAN.getGreen(), Color.CYAN.getBlue(), ALPHA);
    private static final Color DEFAULT_SELECTED_SECONDARY_COLOR = new Color(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), ALPHA);
    private static final Color DEFAULT_UNSELECTED_PRIMARY_COLOR = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), ALPHA);
    private static final Color DEFAULT_UNSELECTED_SECONDARY_COLOR = new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), ALPHA);

    private static final int DEFAULT_GAP_BETWEEN_BARS_IN_PIXELS = 2;

    private Color primarySelectedColor = DEFAULT_SELECTED_PRIMARY_COLOR;
    private Color primaryUnselectedColor = DEFAULT_UNSELECTED_PRIMARY_COLOR;
    private Color secondarySelectedColor = DEFAULT_SELECTED_SECONDARY_COLOR;
    private Color secondaryUnselectedColor = DEFAULT_UNSELECTED_SECONDARY_COLOR;
    private int pixelGapBetweenBars = DEFAULT_GAP_BETWEEN_BARS_IN_PIXELS;

    public HistogramRangeSelectorPanelController()
    {
        super();
    }

    @Override
    protected IRangeSelectorView createPanel()
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
        final int numBins = getNumBins();
        return numBins + getPixelGapBetweenBars() * (numBins - 1);
    }

    /**
     * @return the primary data set normalized to 100.0 (normalizes against the max of primary AND secondary data)
     */
    public List<Integer> getScaledPrimaryData()
    {
        return getScaledData(rangeConfiguration.getPrimaryData());
    }

    @Override
    public Color getPrimarySelectedColor()
    {
        return primarySelectedColor;
    }

    @Override
    public Color getPrimaryUnselectedColor()
    {
        return primaryUnselectedColor;
    }

    @Override
    public Color getSecondarySelectedColor()
    {
        return secondarySelectedColor;
    }

    @Override
    public Color getSecondaryUnselectedColor()
    {
        return secondaryUnselectedColor;
    }

    @Override
    public int getPixelGapBetweenBars()
    {
        return pixelGapBetweenBars;
    }

    /**
     * @return the secondary data set normalized to 100.0 (normalizes against the max of primary AND secondary data)
     */
    public List<Integer> getScaledSecondaryData()
    {
        return getScaledData(rangeConfiguration.getSecondaryData());
    }

    @Override
    public void setPrimarySelectedColor(Color primarySelectedColor)
    {
        this.primarySelectedColor = primarySelectedColor;
        ((HistogramSelectionPanel) panel).updateLegend();
    }

    @Override
    public void setPrimaryUnselectedColor(Color primaryUnselectedColor)
    {
        this.primaryUnselectedColor = primaryUnselectedColor;
        ((HistogramSelectionPanel) panel).updateLegend();
    }

    @Override
    public void setSecondarySelectedColor(Color secondarySelectedColor)
    {
        this.secondarySelectedColor = secondarySelectedColor;
        ((HistogramSelectionPanel) panel).updateLegend();
    }

    @Override
    public void setSecondaryUnselectedColor(Color secondaryUnselectedColor)
    {
        this.secondaryUnselectedColor = secondaryUnselectedColor;
        ((HistogramSelectionPanel) panel).updateLegend();
    }

    @Override
    public void setPixelGapBetweenBars(int numPixels)
    {
        pixelGapBetweenBars = numPixels;
        panel.refreshView();
    }

    private List<Integer> getScaledData(List<Integer> dataSet)
    {
        final int maxValue = rangeConfiguration.getDataAbsoluteMax();

        // Normalize to 100.0
        return dataSet.stream()
                .map(val -> (1.0 * (maxValue - val) / maxValue) * 100.0)
                .map(Double::intValue)
                .collect(Collectors.toList());
    }
}
