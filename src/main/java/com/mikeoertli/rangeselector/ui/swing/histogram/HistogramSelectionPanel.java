package com.mikeoertli.rangeselector.ui.swing.histogram;

import com.mikeoertli.rangeselector.api.IRangeSelectorView;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;

/**
 * Range selection panel that uses a histogram as the underlying image/selectable GUI
 *
 * @since 0.0.1
 */
public class HistogramSelectionPanel extends JPanel implements IRangeSelectorView
{
    private final FrequencyRangeSelectorPanelController controller;

    public HistogramSelectionPanel(FrequencyRangeSelectorPanelController controller)
    {
        this.controller = controller;
        initComponents();
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

    @Override
    public void reset()
    {

    }

    @Override
    public boolean isLocked()
    {
        return isEnabled();
    }

    @Override
    public void lockPanel()
    {
        setEnabled(false);
    }

    @Override
    public void unlockPanel()
    {
        setEnabled(true);
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
