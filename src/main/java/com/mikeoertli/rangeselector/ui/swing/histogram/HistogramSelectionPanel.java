package com.mikeoertli.rangeselector.ui.swing.histogram;

import com.mikeoertli.rangeselector.ui.swing.ARangeSelectionPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Range selection panel that uses a histogram as the underlying image/selectable GUI
 *
 * @since 0.0.2
 */
public class HistogramSelectionPanel extends ARangeSelectionPanel
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int ALPHA = 175;
    private static final Color DEFAULT_SELECTED_PRIMARY_COLOR = new Color(Color.CYAN.getRed(), Color.CYAN.getGreen(), Color.CYAN.getBlue(), ALPHA);
    private static final Color DEFAULT_SELECTED_SECONDARY_COLOR = new Color(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), ALPHA);
    private static final Color DEFAULT_UNSELECTED_PRIMARY_COLOR = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), ALPHA);
    private static final Color DEFAULT_UNSELECTED_SECONDARY_COLOR = new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), ALPHA);

    private static final int GAP_BETWEEN_BARS_IN_PIXELS = 2;

    public HistogramSelectionPanel(FrequencyRangeSelectorPanelController controller)
    {
        super(controller);

        initComponents();
    }

    private FrequencyRangeSelectorPanelController getHistogramController()
    {
        return (FrequencyRangeSelectorPanelController) controller;
    }

    @Override
    protected void paintRegionBeforeSelection(Graphics graphics, int startOfSelectedRange)
    {
        final int firstSelectedDataIndex = getHistogramDataIndexForPixel(startOfSelectedRange, true);

        final List<Integer> scaledPrimaryData = getHistogramController().getScaledPrimaryData();
        final List<Integer> scaledSecondaryData = getHistogramController().getScaledSecondaryData();
        paintDataBars(scaledPrimaryData, graphics, 0, firstSelectedDataIndex, DEFAULT_UNSELECTED_PRIMARY_COLOR);
        paintDataBars(scaledSecondaryData, graphics, 0, firstSelectedDataIndex, DEFAULT_UNSELECTED_SECONDARY_COLOR);
    }

    @Override
    protected void paintSelectedRegion(Graphics graphics, int startOfSelectedRange, int endOfSelectedRange)
    {
        final int firstSelectedDataIndex = getHistogramDataIndexForPixel(startOfSelectedRange, true);
        final int endSelectedDataIndex = getHistogramDataIndexForPixel(endOfSelectedRange, false);

        paintDataBars(getHistogramController().getScaledPrimaryData(), graphics, firstSelectedDataIndex, endSelectedDataIndex, DEFAULT_SELECTED_PRIMARY_COLOR);
        paintDataBars(getHistogramController().getScaledSecondaryData(), graphics, firstSelectedDataIndex, endSelectedDataIndex, DEFAULT_SELECTED_SECONDARY_COLOR);
    }

    @Override
    protected void paintRegionAfterSelection(Graphics graphics, int endOfSelectedRange)
    {
        final int endSelectedDataIndex = getHistogramDataIndexForPixel(endOfSelectedRange, false);

        if (getHistogramController().getNumBins() > endSelectedDataIndex)
        {
            final List<Integer> scaledPrimaryData = getHistogramController().getScaledPrimaryData();
            final List<Integer> scaledSecondaryData = getHistogramController().getScaledSecondaryData();
            final int firstBarToDraw = endSelectedDataIndex;
            final int lastBarToDraw = scaledPrimaryData.size();
            paintDataBars(scaledPrimaryData, graphics, firstBarToDraw, lastBarToDraw, DEFAULT_UNSELECTED_PRIMARY_COLOR);
            paintDataBars(scaledSecondaryData, graphics, firstBarToDraw, lastBarToDraw, DEFAULT_UNSELECTED_SECONDARY_COLOR);
        }
    }

    @Override
    protected void paintUnselected(Graphics graphics)
    {
        final List<Integer> scaledPrimaryData = getHistogramController().getScaledPrimaryData();
        final List<Integer> scaledSecondaryData = getHistogramController().getScaledSecondaryData();
        final int lastBarToDraw = scaledPrimaryData.size();
        final int firstBarToDraw = 0;
        paintDataBars(scaledPrimaryData, graphics, firstBarToDraw, lastBarToDraw, DEFAULT_UNSELECTED_PRIMARY_COLOR);
        paintDataBars(scaledSecondaryData, graphics, firstBarToDraw, lastBarToDraw, DEFAULT_UNSELECTED_SECONDARY_COLOR);
    }

    private void paintDataBars(List<Integer> scaledDataSet, Graphics graphics, int firstBarToDraw, int lastBarToDraw, Color color)
    {
        final int barWidth = calculateBarWidth(scaledDataSet.size());

        graphics.setColor(color);
        final int panelHeight = getHeight();

        for (int index = firstBarToDraw; index < lastBarToDraw; index++)
        {
            final Integer dataValue = scaledDataSet.get(index);
            final int barHeight = calculateBarHeight(dataValue);
            final int startLocX = barWidth * index + GAP_BETWEEN_BARS_IN_PIXELS * Math.max(0, (index - 1));
            final int startLocY = panelHeight - barHeight;

            graphics.fillRect(startLocX, startLocY, barWidth, barHeight);
        }
    }

    private int getHistogramDataIndexForPixel(int pixelIndex, boolean roundDown)
    {
        final int numBins = getHistogramController().getNumBins();
        final Dimension size = getSize();
        final double totalWidth = size.getWidth();
        final double percentOfWidthBeforeSelection = 1.0 - ((totalWidth - pixelIndex) / totalWidth);
        if (roundDown)
        {
            return (int) Math.floor(numBins * percentOfWidthBeforeSelection);
        } else
        {
            return (int) Math.ceil(numBins * percentOfWidthBeforeSelection);
        }
    }

    private int calculateBarHeight(int value)
    {
        return (int) (getSize().getHeight() * (value / 100.0));
    }

    private int calculateBarWidth(int numBars)
    {
        final int numPixelsForBars = getWidth() - (numBars - 1) * GAP_BETWEEN_BARS_IN_PIXELS;
        return numPixelsForBars / numBars;
    }

    @Override
    public void reset()
    {
        // TODO
        logger.trace("Resetting {}", getClass().getSimpleName());
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents

        //======== this ========
        setName("this");
        setLayout(null);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for (int i = 0; i < getComponentCount(); i++)
            {
                Rectangle bounds = getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            setMinimumSize(preferredSize);
            setPreferredSize(preferredSize);
        }
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
