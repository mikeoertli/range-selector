package com.mikeoertli.rangeselector.ui.swing.histogram;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
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

    private static final int DEFAULT_GAP_BETWEEN_BARS_IN_PIXELS = 2;

    private Color primarySelectedColor = DEFAULT_SELECTED_PRIMARY_COLOR;
    private Color primaryUnselectedColor = DEFAULT_UNSELECTED_PRIMARY_COLOR;
    private Color secondarySelectedColor = DEFAULT_SELECTED_SECONDARY_COLOR;
    private Color secondaryUnselectedColor = DEFAULT_UNSELECTED_SECONDARY_COLOR;
    private int pixelGapBetweenBars = DEFAULT_GAP_BETWEEN_BARS_IN_PIXELS;

    public HistogramSelectionPanel(HistogramRangeSelectorPanelController controller)
    {
        super(controller);

        initComponents();
    }

    private HistogramRangeSelectorPanelController getHistogramController()
    {
        return (HistogramRangeSelectorPanelController) controller;
    }

    @Override
    protected void paintRegionBeforeSelection(Graphics graphics, int startOfSelectedRange)
    {
        ((RenderPanel) histogramPanel).setStartOfSelectedRange(startOfSelectedRange);
    }

    @Override
    protected void paintSelectedRegion(Graphics graphics, int startOfSelectedRange, int endOfSelectedRange)
    {
        ((RenderPanel) histogramPanel).setStartOfSelectedRange(startOfSelectedRange);
        ((RenderPanel) histogramPanel).setEndOfSelectedRange(endOfSelectedRange);
    }

    @Override
    protected void paintRegionAfterSelection(Graphics graphics, int endOfSelectedRange)
    {
        ((RenderPanel) histogramPanel).setEndOfSelectedRange(endOfSelectedRange);
    }

    @Override
    protected void paintUnselected(Graphics graphics)
    {
        ((RenderPanel) histogramPanel).setStartOfSelectedRange(-1);
        ((RenderPanel) histogramPanel).setEndOfSelectedRange(-1);
    }

    @Override
    public void reset()
    {
        // TODO
        logger.trace("Resetting {}", getClass().getSimpleName());
    }

    void setPrimaryLabelText(String value)
    {
        SwingUtilities.invokeLater(() -> primaryLabel.setText(value));
    }

    void setSecondaryLabelText(String value)
    {
        SwingUtilities.invokeLater(() -> secondaryLabel.setText(value));
    }

    void setPrimarySelectedColor(Color primarySelectedColor)
    {
        this.primarySelectedColor = primarySelectedColor;
        updateLegend();
    }

    void setPrimaryUnselectedColor(Color primaryUnselectedColor)
    {
        this.primaryUnselectedColor = primaryUnselectedColor;
        updateLegend();
    }

    void setSecondarySelectedColor(Color secondarySelectedColor)
    {
        this.secondarySelectedColor = secondarySelectedColor;
        updateLegend();
    }

    void setSecondaryUnselectedColor(Color secondaryUnselectedColor)
    {
        this.secondaryUnselectedColor = secondaryUnselectedColor;
        updateLegend();
    }

    void setPixelGapBetweenBars(int numPixels)
    {
        pixelGapBetweenBars = numPixels;
        refreshView();
    }

    private void updateLegend()
    {
        SwingUtilities.invokeLater(() -> {
            legendPanel.revalidate();
            legendPanel.repaint();
        });
    }

    private void createUIComponents() {
        primarySelectedColorPanel = new ColorLegendPanel(primarySelectedColor);
        primaryUnselectedColorPanel = new ColorLegendPanel(primaryUnselectedColor);
        secondarySelectedColorPanel = new ColorLegendPanel(secondarySelectedColor);
        secondaryUnselectedColorPanel = new ColorLegendPanel(secondaryUnselectedColor);
        histogramPanel = new RenderPanel();
    }

    private static class ColorLegendPanel extends JPanel
    {
        private static final int LEGEND_HEIGHT = 8;
        private Color color;

        public ColorLegendPanel(Color color)
        {
            this.color = color;
        }

        public void setColor(Color color)
        {
            this.color = color;
        }

        @Override
        protected void paintComponent(Graphics graphics)
        {
            final Dimension size = getSize();
            final int startY = (int) ((size.getHeight() - LEGEND_HEIGHT) / 2);
            graphics.setColor(color);
            graphics.fillRect(0, startY, (int) size.getWidth(), LEGEND_HEIGHT);
        }
    }

    private class RenderPanel extends JPanel
    {
        private int startOfSelectedRange;
        private int endOfSelectedRange;

        public void setStartOfSelectedRange(int startOfSelectedRange)
        {
            this.startOfSelectedRange = startOfSelectedRange;
        }

        public void setEndOfSelectedRange(int endOfSelectedRange)
        {
            this.endOfSelectedRange = endOfSelectedRange;
        }

        @Override
        protected void paintComponent(Graphics graphics)
        {
            if (startOfSelectedRange >= 0 && endOfSelectedRange >= startOfSelectedRange)
            {
                paintRegionBeforeSelection(graphics, startOfSelectedRange);

                paintSelectedRegion(graphics, startOfSelectedRange, endOfSelectedRange);

                paintRegionAfterSelection(graphics, endOfSelectedRange);
            } else
            {
                paintUnselected(graphics);
            }
        }

        private void paintRegionBeforeSelection(Graphics graphics, int startOfSelectedRange)
        {
            final int firstSelectedDataIndex = getHistogramDataIndexForPixel(startOfSelectedRange, true);

            final List<Integer> scaledPrimaryData = getHistogramController().getScaledPrimaryData();
            final List<Integer> scaledSecondaryData = getHistogramController().getScaledSecondaryData();
            paintDataBars(scaledPrimaryData, graphics, 0, firstSelectedDataIndex, primaryUnselectedColor);
            paintDataBars(scaledSecondaryData, graphics, 0, firstSelectedDataIndex, secondaryUnselectedColor);
        }

        private void paintSelectedRegion(Graphics graphics, int startOfSelectedRange, int endOfSelectedRange)
        {
            final int firstSelectedDataIndex = getHistogramDataIndexForPixel(startOfSelectedRange, true);
            final int endSelectedDataIndex = getHistogramDataIndexForPixel(endOfSelectedRange, false);

            paintDataBars(getHistogramController().getScaledPrimaryData(), graphics, firstSelectedDataIndex, endSelectedDataIndex, primarySelectedColor);
            paintDataBars(getHistogramController().getScaledSecondaryData(), graphics, firstSelectedDataIndex, endSelectedDataIndex, secondarySelectedColor);
        }

        private void paintRegionAfterSelection(Graphics graphics, int endOfSelectedRange)
        {
            final int endSelectedDataIndex = getHistogramDataIndexForPixel(endOfSelectedRange, false);

            if (getHistogramController().getNumBins() > endSelectedDataIndex)
            {
                final List<Integer> scaledPrimaryData = getHistogramController().getScaledPrimaryData();
                final List<Integer> scaledSecondaryData = getHistogramController().getScaledSecondaryData();
                final int firstBarToDraw = endSelectedDataIndex;
                final int lastBarToDraw = scaledPrimaryData.size();
                paintDataBars(scaledPrimaryData, graphics, firstBarToDraw, lastBarToDraw, primaryUnselectedColor);
                paintDataBars(scaledSecondaryData, graphics, firstBarToDraw, lastBarToDraw, secondaryUnselectedColor);
            }
        }

        private void paintUnselected(Graphics graphics)
        {
            final List<Integer> scaledPrimaryData = getHistogramController().getScaledPrimaryData();
            final List<Integer> scaledSecondaryData = getHistogramController().getScaledSecondaryData();
            final int lastBarToDraw = scaledPrimaryData.size();
            final int firstBarToDraw = 0;
            paintDataBars(scaledPrimaryData, graphics, firstBarToDraw, lastBarToDraw, primaryUnselectedColor);
            paintDataBars(scaledSecondaryData, graphics, firstBarToDraw, lastBarToDraw, secondaryUnselectedColor);
        }

        private void paintDataBars(List<Integer> scaledDataSet, Graphics graphics, int firstBarToDraw, int lastBarToDraw, Color color)
        {
            final int barWidth = calculateBarWidth(scaledDataSet.size());

            graphics.setColor(color);
            final int panelHeight = histogramPanel.getHeight();

            for (int index = firstBarToDraw; index < lastBarToDraw; index++)
            {
                final Integer dataValue = scaledDataSet.get(index);
                final int barHeight = calculateBarHeight(dataValue);
                final int startLocX = barWidth * index + pixelGapBetweenBars * index;
                final int startLocY = panelHeight - barHeight;

                graphics.fillRect(startLocX, startLocY, barWidth, barHeight);
            }
        }

        private int getHistogramDataIndexForPixel(int pixelIndex, boolean roundDown)
        {
            final int numBins = getHistogramController().getNumBins();
            final Dimension size = histogramPanel.getSize();
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
            return (int) (histogramPanel.getSize().getHeight() * (value / 100.0));
        }

        private int calculateBarWidth(int numBars)
        {
            final int numPixelsForBars = histogramPanel.getWidth() - (numBars - 1) * pixelGapBetweenBars;
            return numPixelsForBars / numBars;
        }
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        legendPanel = new JPanel();
        primaryLabel = new JLabel();
        secondaryLabel = new JLabel();
        primSelectedLabel = new JLabel();
        hSpacer1 = new JPanel(null);
        secSelectedLabel = new JLabel();
        primUnselectedLabel = new JLabel();
        secUnselectedPanel = new JLabel();

        //======== this ========
        setName("this");
        setLayout(new BorderLayout());

        //======== histogramPanel ========
        {
            histogramPanel.setName("histogramPanel");
            histogramPanel.setLayout(null);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < histogramPanel.getComponentCount(); i++) {
                    Rectangle bounds = histogramPanel.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = histogramPanel.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                histogramPanel.setMinimumSize(preferredSize);
                histogramPanel.setPreferredSize(preferredSize);
            }
        }
        add(histogramPanel, BorderLayout.CENTER);

        //======== legendPanel ========
        {
            legendPanel.setBorder(new TitledBorder(null, "LEGEND", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
                new Font(".SF NS Text", Font.BOLD, 13)));
            legendPanel.setName("legendPanel");
            legendPanel.setLayout(new GridBagLayout());
            ((GridBagLayout)legendPanel.getLayout()).columnWidths = new int[] {0, 75, 0, 0, 93, 0};
            ((GridBagLayout)legendPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0};
            ((GridBagLayout)legendPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 1.0, 0.0, 0.0, 1.0E-4};
            ((GridBagLayout)legendPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 1.0E-4};

            //---- primaryLabel ----
            primaryLabel.setText("PRIMARY");
            primaryLabel.setName("primaryLabel");
            legendPanel.add(primaryLabel, new GridBagConstraints(0, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 5, 5), 0, 0));

            //---- secondaryLabel ----
            secondaryLabel.setText("SECONDARY");
            secondaryLabel.setName("secondaryLabel");
            legendPanel.add(secondaryLabel, new GridBagConstraints(3, 0, 2, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 5, 0), 0, 0));

            //---- primSelectedLabel ----
            primSelectedLabel.setText("Selected");
            primSelectedLabel.setFont(primSelectedLabel.getFont().deriveFont(primSelectedLabel.getFont().getStyle() | Font.ITALIC, primSelectedLabel.getFont().getSize() - 1f));
            primSelectedLabel.setName("primSelectedLabel");
            legendPanel.add(primSelectedLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 5, 5), 0, 0));

            //======== primarySelectedColorPanel ========
            {
                primarySelectedColorPanel.setName("primarySelectedColorPanel");
                primarySelectedColorPanel.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < primarySelectedColorPanel.getComponentCount(); i++) {
                        Rectangle bounds = primarySelectedColorPanel.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = primarySelectedColorPanel.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    primarySelectedColorPanel.setMinimumSize(preferredSize);
                    primarySelectedColorPanel.setPreferredSize(preferredSize);
                }
            }
            legendPanel.add(primarySelectedColorPanel, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

            //---- hSpacer1 ----
            hSpacer1.setName("hSpacer1");
            legendPanel.add(hSpacer1, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 5), 0, 0));

            //---- secSelectedLabel ----
            secSelectedLabel.setText("Selected");
            secSelectedLabel.setFont(secSelectedLabel.getFont().deriveFont(secSelectedLabel.getFont().getStyle() | Font.ITALIC, secSelectedLabel.getFont().getSize() - 1f));
            secSelectedLabel.setName("secSelectedLabel");
            legendPanel.add(secSelectedLabel, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 5, 5), 0, 0));

            //======== secondarySelectedColorPanel ========
            {
                secondarySelectedColorPanel.setName("secondarySelectedColorPanel");
                secondarySelectedColorPanel.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < secondarySelectedColorPanel.getComponentCount(); i++) {
                        Rectangle bounds = secondarySelectedColorPanel.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = secondarySelectedColorPanel.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    secondarySelectedColorPanel.setMinimumSize(preferredSize);
                    secondarySelectedColorPanel.setPreferredSize(preferredSize);
                }
            }
            legendPanel.add(secondarySelectedColorPanel, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));

            //---- primUnselectedLabel ----
            primUnselectedLabel.setText("Unselected");
            primUnselectedLabel.setFont(primUnselectedLabel.getFont().deriveFont(primUnselectedLabel.getFont().getStyle() | Font.ITALIC, primUnselectedLabel.getFont().getSize() - 1f));
            primUnselectedLabel.setName("primUnselectedLabel");
            legendPanel.add(primUnselectedLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));

            //======== primaryUnselectedColorPanel ========
            {
                primaryUnselectedColorPanel.setName("primaryUnselectedColorPanel");
                primaryUnselectedColorPanel.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < primaryUnselectedColorPanel.getComponentCount(); i++) {
                        Rectangle bounds = primaryUnselectedColorPanel.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = primaryUnselectedColorPanel.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    primaryUnselectedColorPanel.setMinimumSize(preferredSize);
                    primaryUnselectedColorPanel.setPreferredSize(preferredSize);
                }
            }
            legendPanel.add(primaryUnselectedColorPanel, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 5), 0, 0));

            //---- secUnselectedPanel ----
            secUnselectedPanel.setText("Unselected");
            secUnselectedPanel.setFont(secUnselectedPanel.getFont().deriveFont(secUnselectedPanel.getFont().getStyle() | Font.ITALIC, secUnselectedPanel.getFont().getSize() - 1f));
            secUnselectedPanel.setName("secUnselectedPanel");
            legendPanel.add(secUnselectedPanel, new GridBagConstraints(3, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                new Insets(0, 0, 0, 5), 0, 0));

            //======== secondaryUnselectedColorPanel ========
            {
                secondaryUnselectedColorPanel.setName("secondaryUnselectedColorPanel");
                secondaryUnselectedColorPanel.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for(int i = 0; i < secondaryUnselectedColorPanel.getComponentCount(); i++) {
                        Rectangle bounds = secondaryUnselectedColorPanel.getComponent(i).getBounds();
                        preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                        preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                    }
                    Insets insets = secondaryUnselectedColorPanel.getInsets();
                    preferredSize.width += insets.right;
                    preferredSize.height += insets.bottom;
                    secondaryUnselectedColorPanel.setMinimumSize(preferredSize);
                    secondaryUnselectedColorPanel.setPreferredSize(preferredSize);
                }
            }
            legendPanel.add(secondaryUnselectedColorPanel, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        }
        add(legendPanel, BorderLayout.SOUTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel histogramPanel;
    private JPanel legendPanel;
    private JLabel primaryLabel;
    private JLabel secondaryLabel;
    private JLabel primSelectedLabel;
    private JPanel primarySelectedColorPanel;
    private JPanel hSpacer1;
    private JLabel secSelectedLabel;
    private JPanel secondarySelectedColorPanel;
    private JLabel primUnselectedLabel;
    private JPanel primaryUnselectedColorPanel;
    private JLabel secUnselectedPanel;
    private JPanel secondaryUnselectedColorPanel;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
