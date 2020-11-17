package com.mikeoertli.rangeselector.ui.swing;

import org.apache.commons.lang3.Range;

import java.awt.*;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Swing panel containing the range selection GUI
 *
 * @since 0.0.1
 */
public class RangeSelectorPanel extends JPanel
{
    public RangeSelectorPanel(Range<Double> range)
    {
        initComponents();
//        rangeSlider.set
    }

    void setSliderEnabled(boolean enabled)
    {
        SwingUtilities.invokeLater(() -> rangeSlider.setEnabled(enabled));
    }

    void setShowLabels(boolean showLabels)
    {
        SwingUtilities.invokeLater(() -> rangeSlider.setPaintLabels(showLabels));
    }

    void setShowTickMarks(boolean showTickMarks)
    {
        SwingUtilities.invokeLater(() -> rangeSlider.setPaintTicks(showTickMarks));
    }

    void setSnapToTicks(boolean snapToTicks)
    {
        SwingUtilities.invokeLater(() -> rangeSlider.setSnapToTicks(snapToTicks));
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        sliderPanel = new JPanel();
        rangeSlider = new JSlider();

        //======== this ========
        setName("this");
        setLayout(new BorderLayout());

        //======== sliderPanel ========
        {
            sliderPanel.setName("sliderPanel");
            sliderPanel.setLayout(new GridBagLayout());
            ((GridBagLayout)sliderPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
            ((GridBagLayout)sliderPanel.getLayout()).rowHeights = new int[] {0, 0, 0};
            ((GridBagLayout)sliderPanel.getLayout()).columnWeights = new double[] {0.0, 1.0, 0.0, 1.0E-4};
            ((GridBagLayout)sliderPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 1.0E-4};

            //---- rangeSlider ----
            rangeSlider.setPaintLabels(true);
            rangeSlider.setPaintTicks(true);
            rangeSlider.setPreferredSize(new Dimension(400, 31));
            rangeSlider.setName("rangeSlider");
            sliderPanel.add(rangeSlider, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 5, 0), 0, 0));
        }
        add(sliderPanel, BorderLayout.CENTER);
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel sliderPanel;
    private JSlider rangeSlider;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
