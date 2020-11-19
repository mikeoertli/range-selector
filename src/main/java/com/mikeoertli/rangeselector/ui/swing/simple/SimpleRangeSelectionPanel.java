package com.mikeoertli.rangeselector.ui.swing.simple;

import com.mikeoertli.rangeselector.ui.swing.ARangeSelectionPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public class SimpleRangeSelectionPanel extends ARangeSelectionPanel
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final int WIDTH = 400;
    private static final int HEIGHT = 100;

    public SimpleRangeSelectionPanel(SimpleRangeSelectorPanelController controller)
    {
        super(controller);

        initComponents();
    }

    @Override
    protected void paintRegionBeforeSelection(Graphics graphics, int startOfSelectedRange)
    {
        graphics.setColor(Color.RED);
        graphics.fillRect(0, 0, startOfSelectedRange, getHeight());
    }

    @Override
    protected void paintSelectedRegion(Graphics graphics, int startOfSelectedRange, int endOfSelectedRange)
    {
        graphics.setColor(Color.CYAN);
        graphics.fillRect(startOfSelectedRange, 0, (endOfSelectedRange - startOfSelectedRange), getHeight());
    }

    @Override
    protected void paintRegionAfterSelection(Graphics graphics, int endOfSelectedRange)
    {
        graphics.setColor(Color.RED);
        graphics.fillRect(endOfSelectedRange, 0, getWidth() - endOfSelectedRange, getHeight());
    }

    @Override
    protected void paintUnselected(Graphics graphics)
    {
        graphics.setColor(Color.YELLOW);
        graphics.fillRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void reset()
    {
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
