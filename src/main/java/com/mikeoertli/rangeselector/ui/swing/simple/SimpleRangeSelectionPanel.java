package com.mikeoertli.rangeselector.ui.swing.simple;

import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.lang.invoke.MethodHandles;

/**
 * Panel for the selectable range based on a rectangle of color.
 *
 * @since 0.0.1
 */
public class SimpleRangeSelectionPanel extends JPanel implements IRangeSelectorView
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int WIDTH = 400;
    private static final int HEIGHT = 100;
    private final SimpleRangeSelectorPanelController controller;
//    private final RangeSelectionMouseListener selectionListener;

    public SimpleRangeSelectionPanel(SimpleRangeSelectorPanelController controller)
    {
        this.controller = controller;

        initComponents();
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        final RangeConfiguration rangeState = controller.getRangeConfiguration();

        if (rangeState.hasSelection())
        {
            final int startOfSelectedRange = rangeState.getSelectionMin();
            final int endOfSelectedRange = rangeState.getSelectionMax();
            final int sizeOfSelectedRange = endOfSelectedRange - startOfSelectedRange;

            graphics.setColor(Color.RED);
            graphics.fillRect(0, 0, startOfSelectedRange, HEIGHT);

            graphics.setColor(Color.CYAN);
            graphics.fillRect(startOfSelectedRange, 0, sizeOfSelectedRange, HEIGHT);

            graphics.setColor(Color.RED);
            graphics.fillRect(endOfSelectedRange, 0, WIDTH - endOfSelectedRange, HEIGHT);
        } else
        {
            graphics.setColor(Color.YELLOW);
            graphics.fillRect(0, 0, WIDTH, HEIGHT);
        }
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
