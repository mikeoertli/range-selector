package com.mikeoertli.rangeselector.ui.swing.histogram;

import com.mikeoertli.rangeselector.api.IViewStyleProvider;
import com.mikeoertli.rangeselector.ui.swing.ASwingRangeSelectionPanel;
import com.mikeoertli.rangeselector.ui.swing.listener.RangeSelectionSwingMouseAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * Range selection panel that uses a histogram as the underlying image/selectable GUI
 *
 * @since 0.0.2
 */
public class HistogramSelectionPanel extends ASwingRangeSelectionPanel
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int MAX_GAP_BETWEEN_BARS = 5;

    public HistogramSelectionPanel(HistogramRangeSelectorPanelController controller)
    {
        super(controller);

        initComponents();
        initializeLockListener();
    }

    private void initializeLockListener()
    {
        lockedIconLabel.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isLeftMouseButton(e))
                {
                    controller.toggleLock();
                }
            }
        });
    }

    private HistogramRangeSelectorPanelController getHistogramController()
    {
        return (HistogramRangeSelectorPanelController) controller;
    }

    @Override
    public void addMouseInputHandler(RangeSelectionSwingMouseAdapter handler) throws IllegalArgumentException
    {
        histogramPanel.addMouseListener(handler);
        histogramPanel.addMouseMotionListener(handler);
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
        logger.trace("Resetting {}", getClass().getSimpleName());
    }

    @Override
    public void lockPanel()
    {
        SwingUtilities.invokeLater(() -> lockedIconLabel.setIcon(controller.getViewStyleProvider().isDarkModeEnabled() ? LOCKED_DARK_MODE_ICON : LOCKED_LIGHT_MODE_ICON));
    }

    @Override
    public void unlockPanel()
    {
        SwingUtilities.invokeLater(() -> lockedIconLabel.setIcon(UNLOCKED_ICON));
    }

    void setPrimaryLabelText(String value)
    {
        SwingUtilities.invokeLater(() -> primaryLabel.setText("<html><u>" + value + "</u></html>"));
    }

    void setSecondaryLabelText(String value)
    {
        SwingUtilities.invokeLater(() -> secondaryLabel.setText("<html><u>" + value + "</u></html>"));
    }

    void updateLegend()
    {
        SwingUtilities.invokeLater(() -> {
            legendPanel.revalidate();
            legendPanel.repaint();
        });
    }

    private void createUIComponents()
    {
        final IViewStyleProvider styleProvider = controller.getViewStyleProvider();
        primarySelectedColorPanel = new ColorLegendPanel(styleProvider.getPrimarySelectedColor());
        primaryUnselectedColorPanel = new ColorLegendPanel(styleProvider.getPrimaryUnselectedColor());
        secondarySelectedColorPanel = new ColorLegendPanel(styleProvider.getSecondarySelectedColor());
        secondaryUnselectedColorPanel = new ColorLegendPanel(styleProvider.getSecondaryUnselectedColor());
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
            final IViewStyleProvider styleProvider = controller.getViewStyleProvider();

            final List<Integer> scaledPrimaryData = getHistogramController().getScaledPrimaryData();
            final List<Integer> scaledSecondaryData = getHistogramController().getScaledSecondaryData();
            paintDataBars(scaledPrimaryData, graphics, 0, firstSelectedDataIndex, styleProvider.getPrimaryUnselectedColor());
            paintDataBars(scaledSecondaryData, graphics, 0, firstSelectedDataIndex, styleProvider.getSecondaryUnselectedColor());
        }

        private void paintSelectedRegion(Graphics graphics, int startOfSelectedRange, int endOfSelectedRange)
        {
            final int firstSelectedDataIndex = getHistogramDataIndexForPixel(startOfSelectedRange, true);
            final int endSelectedDataIndex = getHistogramDataIndexForPixel(endOfSelectedRange, false);
            final IViewStyleProvider styleProvider = controller.getViewStyleProvider();

            paintDataBars(getHistogramController().getScaledPrimaryData(), graphics, firstSelectedDataIndex, endSelectedDataIndex, styleProvider.getPrimarySelectedColor());
            paintDataBars(getHistogramController().getScaledSecondaryData(), graphics, firstSelectedDataIndex, endSelectedDataIndex, styleProvider.getSecondarySelectedColor());
        }

        private void paintRegionAfterSelection(Graphics graphics, int endOfSelectedRange)
        {
            final int endSelectedDataIndex = getHistogramDataIndexForPixel(endOfSelectedRange, false);
            final IViewStyleProvider styleProvider = controller.getViewStyleProvider();

            if (getHistogramController().getNumBins() > endSelectedDataIndex)
            {
                final List<Integer> scaledPrimaryData = getHistogramController().getScaledPrimaryData();
                final List<Integer> scaledSecondaryData = getHistogramController().getScaledSecondaryData();
                final int lastBarToDraw = scaledPrimaryData.size();
                paintDataBars(scaledPrimaryData, graphics, endSelectedDataIndex, lastBarToDraw, styleProvider.getPrimaryUnselectedColor());
                paintDataBars(scaledSecondaryData, graphics, endSelectedDataIndex, lastBarToDraw, styleProvider.getSecondaryUnselectedColor());
            }
        }

        private void paintUnselected(Graphics graphics)
        {
            final List<Integer> scaledPrimaryData = getHistogramController().getScaledPrimaryData();
            final List<Integer> scaledSecondaryData = getHistogramController().getScaledSecondaryData();
            final int lastBarToDraw = scaledPrimaryData.size();
            final int firstBarToDraw = 0;
            final IViewStyleProvider styleProvider = controller.getViewStyleProvider();
            paintDataBars(scaledPrimaryData, graphics, firstBarToDraw, lastBarToDraw, styleProvider.getPrimaryUnselectedColor());
            paintDataBars(scaledSecondaryData, graphics, firstBarToDraw, lastBarToDraw, styleProvider.getSecondaryUnselectedColor());
        }

        private void paintDataBars(List<Integer> scaledDataSet, Graphics graphics, int firstBarToDraw, int lastBarToDraw, Color color)
        {
            if (!scaledDataSet.isEmpty())
            {
                final int barWidth = calculateBarWidth(scaledDataSet.size());

                graphics.setColor(color);
                final int panelHeight = histogramPanel.getHeight();
                final IViewStyleProvider styleProvider = controller.getViewStyleProvider();

                for (int index = firstBarToDraw; index < lastBarToDraw; index++)
                {
                    final Integer dataValue = scaledDataSet.get(index);
                    final int barHeight = calculateBarHeight(dataValue);
                    final int startLocX = barWidth * index + styleProvider.getPixelGapBetweenBars() * index;
                    final int startLocY = panelHeight - barHeight;

                    graphics.fillRect(startLocX, startLocY, barWidth, barHeight);
                }
            }
        }

        private int getHistogramDataIndexForPixel(int pixelIndex, boolean roundDown)
        {
            final int numBins = getHistogramController().getNumBins();
            final Dimension size = histogramPanel.getSize();
            final double totalWidth = size.getWidth();
            final int barWidth = calculateBarWidth(numBins);
            final int optimalPixelGap = getOptimalPixelGap((int) totalWidth, numBins);

            final int calculatedDataIndex;
            if (roundDown)
            {
                calculatedDataIndex = (int) Math.floor(1.0 * pixelIndex / (barWidth + optimalPixelGap));
            } else
            {
                calculatedDataIndex = (int) Math.ceil(1.0 * pixelIndex / (barWidth + optimalPixelGap));
            }
            return Math.min(numBins, calculatedDataIndex);
        }

        private int calculateBarHeight(int value)
        {
            return (int) (histogramPanel.getSize().getHeight() * (value / 100.0));
        }

        private int calculateBarWidth(int numBars)
        {
            final IViewStyleProvider styleProvider = controller.getViewStyleProvider();
            final int currentPixelGapBetweenBars = styleProvider.getPixelGapBetweenBars();
            final int panelWidth = histogramPanel.getWidth();
            final int optimalGapWidth = getOptimalPixelGap(panelWidth, numBars);
            if (optimalGapWidth != currentPixelGapBetweenBars)
            {
                styleProvider.setPixelGapBetweenBars(optimalGapWidth);
            }
            final int numPixelsForBars = panelWidth - (numBars - 1) * optimalGapWidth;

            return numPixelsForBars / numBars;
        }

        private int getOptimalPixelGap(int panelWidth, int numBars)
        {
            int minWastedSpace = panelWidth % numBars;
            int bestChoice = 0;

            for (int gap = 1; gap <= MAX_GAP_BETWEEN_BARS && ((numBars - 1) * gap) < panelWidth; gap++)
            {
                final int numPixelsForBars = panelWidth - ((numBars - 1) * gap);
                final int wastedSpace = numPixelsForBars % numBars;
                if (wastedSpace < minWastedSpace)
                {
                    logger.trace("Found a better gap size - was {} (result = {}px wasted), now it is {} ({} px wasted).", bestChoice, minWastedSpace, gap, wastedSpace);
                    minWastedSpace = wastedSpace;
                    bestChoice = gap;
                }
            }

            return bestChoice;
        }
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        createUIComponents();

        legendPanel = new JPanel();
        stateLabel = new JLabel();
        stateLegendVerticalSeparator = new JSeparator();
        primaryLabel = new JLabel();
        secondaryLabel = new JLabel();
        lockedIconLabel = new JLabel();
        hSpacer3 = new JPanel(null);
        hSpacer2 = new JPanel(null);
        primSelectedLabel = new JLabel();
        hSpacer1 = new JPanel(null);
        secSelectedLabel = new JLabel();
        primUnselectedLabel = new JLabel();
        secUnselectedPanel = new JLabel();

        //======== this ========
        setPreferredSize(new Dimension(500, 230));
        setMinimumSize(new Dimension(350, 140));
        setName("this");
        setLayout(new BorderLayout());

        //======== histogramPanel ========
        {
            histogramPanel.setName("histogramPanel");
            histogramPanel.setLayout(null);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for (int i = 0; i < histogramPanel.getComponentCount(); i++)
                {
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
            legendPanel.setBorder(new TitledBorder(null, "LEGEND & STATUS", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
                    new Font(".SF NS Text", Font.BOLD, 15)));
            legendPanel.setName("legendPanel");
            legendPanel.setLayout(new GridBagLayout());
            ((GridBagLayout) legendPanel.getLayout()).columnWidths = new int[]{0, 0, 0, 0, 0, 75, 0, 0, 98, 0, 0};
            ((GridBagLayout) legendPanel.getLayout()).rowHeights = new int[]{0, 0, 0, 0};
            ((GridBagLayout) legendPanel.getLayout()).columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.19999999999999998, 0.0, 0.0, 1.0, 1.0E-4};
            ((GridBagLayout) legendPanel.getLayout()).rowWeights = new double[]{0.0, 0.0, 0.0, 1.0E-4};

            //---- stateLabel ----
            stateLabel.setText("STATE");
            stateLabel.setFont(stateLabel.getFont().deriveFont(Font.BOLD | Font.ITALIC));
            stateLabel.setName("stateLabel");
            legendPanel.add(stateLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- stateLegendVerticalSeparator ----
            stateLegendVerticalSeparator.setOrientation(SwingConstants.VERTICAL);
            stateLegendVerticalSeparator.setName("stateLegendVerticalSeparator");
            legendPanel.add(stateLegendVerticalSeparator, new GridBagConstraints(2, 0, 1, 3, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- primaryLabel ----
            primaryLabel.setText("PRIMARY");
            primaryLabel.setFont(primaryLabel.getFont().deriveFont(Font.BOLD | Font.ITALIC));
            primaryLabel.setName("primaryLabel");
            legendPanel.add(primaryLabel, new GridBagConstraints(4, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- secondaryLabel ----
            secondaryLabel.setText("SECONDARY");
            secondaryLabel.setFont(secondaryLabel.getFont().deriveFont(Font.BOLD | Font.ITALIC));
            secondaryLabel.setName("secondaryLabel");
            legendPanel.add(secondaryLabel, new GridBagConstraints(7, 0, 2, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- lockedIconLabel ----
            lockedIconLabel.setIcon(new ImageIcon(getClass().getResource("/icon/lock/unlocked-32x32.png")));
            lockedIconLabel.setName("lockedIconLabel");
            legendPanel.add(lockedIconLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- hSpacer3 ----
            hSpacer3.setName("hSpacer3");
            legendPanel.add(hSpacer3, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- hSpacer2 ----
            hSpacer2.setName("hSpacer2");
            legendPanel.add(hSpacer2, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- primSelectedLabel ----
            primSelectedLabel.setText("Selected");
            primSelectedLabel.setFont(primSelectedLabel.getFont().deriveFont(primSelectedLabel.getFont().getStyle() | Font.ITALIC, primSelectedLabel.getFont().getSize() - 1f));
            primSelectedLabel.setName("primSelectedLabel");
            legendPanel.add(primSelectedLabel, new GridBagConstraints(4, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

            //======== primarySelectedColorPanel ========
            {
                primarySelectedColorPanel.setName("primarySelectedColorPanel");
                primarySelectedColorPanel.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < primarySelectedColorPanel.getComponentCount(); i++)
                    {
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
            legendPanel.add(primarySelectedColorPanel, new GridBagConstraints(5, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- hSpacer1 ----
            hSpacer1.setName("hSpacer1");
            legendPanel.add(hSpacer1, new GridBagConstraints(6, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- secSelectedLabel ----
            secSelectedLabel.setText("Selected");
            secSelectedLabel.setFont(secSelectedLabel.getFont().deriveFont(secSelectedLabel.getFont().getStyle() | Font.ITALIC, secSelectedLabel.getFont().getSize() - 1f));
            secSelectedLabel.setName("secSelectedLabel");
            legendPanel.add(secSelectedLabel, new GridBagConstraints(7, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 5, 5), 0, 0));

            //======== secondarySelectedColorPanel ========
            {
                secondarySelectedColorPanel.setName("secondarySelectedColorPanel");
                secondarySelectedColorPanel.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < secondarySelectedColorPanel.getComponentCount(); i++)
                    {
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
            legendPanel.add(secondarySelectedColorPanel, new GridBagConstraints(8, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

            //---- primUnselectedLabel ----
            primUnselectedLabel.setText("Unselected");
            primUnselectedLabel.setFont(primUnselectedLabel.getFont().deriveFont(primUnselectedLabel.getFont().getStyle() | Font.ITALIC, primUnselectedLabel.getFont().getSize() - 1f));
            primUnselectedLabel.setName("primUnselectedLabel");
            legendPanel.add(primUnselectedLabel, new GridBagConstraints(4, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 0, 5), 0, 0));

            //======== primaryUnselectedColorPanel ========
            {
                primaryUnselectedColorPanel.setName("primaryUnselectedColorPanel");
                primaryUnselectedColorPanel.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < primaryUnselectedColorPanel.getComponentCount(); i++)
                    {
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
            legendPanel.add(primaryUnselectedColorPanel, new GridBagConstraints(5, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

            //---- secUnselectedPanel ----
            secUnselectedPanel.setText("Unselected");
            secUnselectedPanel.setFont(secUnselectedPanel.getFont().deriveFont(secUnselectedPanel.getFont().getStyle() | Font.ITALIC, secUnselectedPanel.getFont().getSize() - 1f));
            secUnselectedPanel.setName("secUnselectedPanel");
            legendPanel.add(secUnselectedPanel, new GridBagConstraints(7, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.EAST, GridBagConstraints.VERTICAL,
                    new Insets(0, 0, 0, 5), 0, 0));

            //======== secondaryUnselectedColorPanel ========
            {
                secondaryUnselectedColorPanel.setName("secondaryUnselectedColorPanel");
                secondaryUnselectedColorPanel.setLayout(null);

                {
                    // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < secondaryUnselectedColorPanel.getComponentCount(); i++)
                    {
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
            legendPanel.add(secondaryUnselectedColorPanel, new GridBagConstraints(8, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));
        }
        add(legendPanel, BorderLayout.SOUTH);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel histogramPanel;
    private JPanel legendPanel;
    private JLabel stateLabel;
    private JSeparator stateLegendVerticalSeparator;
    private JLabel primaryLabel;
    private JLabel secondaryLabel;
    private JLabel lockedIconLabel;
    private JPanel hSpacer3;
    private JPanel hSpacer2;
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
