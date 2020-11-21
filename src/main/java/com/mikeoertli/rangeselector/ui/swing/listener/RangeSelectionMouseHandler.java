package com.mikeoertli.rangeselector.ui.swing.listener;

import com.mikeoertli.rangeselector.ui.common.IMouseInputHandler;
import com.mikeoertli.rangeselector.api.IRangeViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.invoke.MethodHandles;

/**
 * Listens to the mouse events associated with making a range selection
 *
 * @since 0.0.1
 */
public class RangeSelectionMouseHandler extends MouseAdapter implements IMouseInputHandler
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private boolean armed = false;

    private int startRange = -1;

    private int endRange = -1;

    // Used to capture the end point while dragging is still in progress
    private int lastKnown = -1;

    private int captureCount = 0;

    private final IRangeViewController controller;

    public RangeSelectionMouseHandler(IRangeViewController controller)
    {
        this.controller = controller;
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        if (e.getClickCount() != 2)
        {
            startRangeCapture(e.getX());
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (armed)
        {
            endRangeCapture(e.getX());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2)
        {
            logger.trace("Selecting complete range due to single click.");
            controller.selectAll();
        } else
        {
            logger.trace("Resetting selection due to single click.");
            reset();
        }
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        if (armed)
        {
            endRangeCapture(e.getX());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        selectedRangeChanged(e.getX());
    }

    private void selectedRangeChanged(int xLocation)
    {
        lastKnown = xLocation;
        saveUpdatedRange();
    }

    private void startRangeCapture(int xLocation)
    {
        logger.trace("Start range capture #{}", captureCount);
        reset();

        armed = true;
        startRange = xLocation;
        lastKnown = xLocation;

        captureCount++;

        saveUpdatedRange();
    }

    private void endRangeCapture(int xLocation)
    {
        armed = false;
        endRange = xLocation;
        lastKnown = xLocation;

        logger.trace("End of capture range (#{}). Start: {}, end: {}.", captureCount, startRange, endRange);

        saveUpdatedRange();
    }

    @Override
    public void reset()
    {
        logger.trace("Resetting armed status and start/stop/lastKnown range. Was armed? {}", armed);

        armed = false;
        startRange = -1;
        endRange = -1;
        lastKnown = -1;
        saveUpdatedRange();
    }

    private void saveUpdatedRange()
    {
        controller.onRangeSelectionChanged(getSelectedRangeMinimum(), getSelectedRangeMaximum());
    }

    @Override
    public boolean isArmed()
    {
        return armed;
    }

    @Override
    public boolean hasRange()
    {
        return startRange >= 0 && lastKnown >= 0;
    }

    @Override
    public int getSelectedRangeMinimum()
    {
        // Selection could have taken place R -> L or L -> R, need to account for "end" being > "start"
        // We will always return from this method with the minimum value and consider that the start
        if (armed)
        {
            return lastKnown >= 0 ? Math.min(lastKnown, startRange) : startRange;
        } else
        {
            return Math.min(startRange, endRange);
        }
    }

    @Override
    public int getSelectedRangeMaximum()
    {
        // Selection could have taken place R -> L or L -> R, need to account for "end" being > "start"
        // We will always return from this method with the maximum value and consider that the end.
        // In the event there is no end, we return the latest point.
        return Math.max(startRange, (armed ? lastKnown : endRange));
    }

    @Override
    public int getSelectedRangeSize()
    {
        return getSelectedRangeMaximum() - getSelectedRangeMinimum();
    }

    @Override
    public void setSelectedRange(int selectionMin, int selectionMax)
    {
        reset();
        startRange = selectionMin;
        endRange = selectionMax;
        lastKnown = selectionMax;
    }
}
