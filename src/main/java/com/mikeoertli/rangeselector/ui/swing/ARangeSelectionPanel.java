package com.mikeoertli.rangeselector.ui.swing;

import com.mikeoertli.rangeselector.api.IRangeSelectionListener;
import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.invoke.MethodHandles;

/**
 * Panel for the selectable range based on a rectangle of color.
 *
 * @since 0.0.2
 */
public abstract class ARangeSelectionPanel extends JPanel implements IRangeSelectorView
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected final ISwingViewController controller;

    protected ARangeSelectionPanel(ISwingViewController controller)
    {
        this.controller = controller;
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

            paintRegionBeforeSelection(graphics, startOfSelectedRange);

            paintSelectedRegion(graphics, startOfSelectedRange, endOfSelectedRange);

            paintRegionAfterSelection(graphics, endOfSelectedRange);
        } else
        {
            paintUnselected(graphics);
        }
    }

    protected abstract void paintRegionBeforeSelection(Graphics graphics, int startOfSelectedRange);

    protected abstract void paintSelectedRegion(Graphics graphics, int startOfSelectedRange, int endOfSelectedRange);

    protected abstract void paintRegionAfterSelection(Graphics graphics, int endOfSelectedRange);

    protected abstract void paintUnselected(Graphics graphics);

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

    @Override
    public void addRangeSelectionListener(IRangeSelectionListener listener)
    {
        if (listener instanceof MouseListener)
        {
            addMouseListener((MouseListener) listener);
        } else
        {
            logger.error("Illegal range selection listener. A Swing panel requires an IRangeSelectionListener that is a " +
                    "MouseListener and the given listener ({}) is not.", listener.getClass().getSimpleName());
        }

        if (listener instanceof MouseMotionListener)
        {
            addMouseMotionListener((MouseMotionListener) listener);
        } else
        {
            logger.error("Illegal range selection listener. A Swing panel requires an IRangeSelectionListener that is a " +
                    "MouseMotionListener and the given listener ({}) is not.", listener.getClass().getSimpleName());
        }
    }

    @Override
    public void removeRangeSelectionListener(IRangeSelectionListener listener)
    {
        if (listener instanceof MouseListener)
        {
            removeMouseListener((MouseListener) listener);
        }

        if (listener instanceof MouseMotionListener)
        {
            removeMouseMotionListener((MouseMotionListener) listener);
        }
    }

    @Override
    public void refreshView()
    {
        repaint();
    }
}
