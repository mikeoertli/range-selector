package com.mikeoertli.rangeselector.core;

import com.mikeoertli.rangeselector.api.IRangeViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Common mouse input to selected range management between different GUI frameworks with different mouse
 * input events and listeners.
 *
 * @since 0.1.0
 */
public class RangeController
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private boolean armed = false;
    private boolean locked = false;

    private int startRange = -1;

    private int endRange = -1;

    // Used to capture the end point while dragging is still in progress
    private int lastKnown = -1;

    private int captureCount = 0;

    private final IRangeViewController controller;

    public RangeController(IRangeViewController controller)
    {
        this.controller = controller;
    }

    public void selectedRangeChanged(int xLocation)
    {
        lastKnown = xLocation;
        saveUpdatedRange();
    }

    public void startRangeCapture(int xLocation)
    {
        if (!locked)
        {

            logger.trace("Start range capture #{}", captureCount);
            reset();

            armed = true;
            startRange = xLocation;
            lastKnown = xLocation;

            captureCount++;

            saveUpdatedRange();
        }
    }

    public void endRangeCapture(int xLocation)
    {
        if (!locked)
        {
            armed = false;
            endRange = xLocation;
            lastKnown = xLocation;

            logger.trace("End of capture range (#{}). Start: {}, end: {}.", captureCount, startRange, endRange);

            saveUpdatedRange();
        }
    }

    public boolean isArmed()
    {
        return armed;
    }

    public boolean hasRange()
    {
        return startRange >= 0 && lastKnown >= 0;
    }

    public void reset()
    {
        if (!locked)
        {
            logger.trace("Resetting armed status and start/stop/lastKnown range. Was armed? {}", armed);

            armed = false;
            startRange = -1;
            endRange = -1;
            lastKnown = -1;
            saveUpdatedRange();
        }
    }

    private void saveUpdatedRange()
    {
        controller.onRangeSelectionChanged(getSelectedRangeMinimum(), getSelectedRangeMaximum());
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;
    }

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

    public int getSelectedRangeMaximum()
    {
        // Selection could have taken place R -> L or L -> R, need to account for "end" being > "start"
        // We will always return from this method with the maximum value and consider that the end.
        // In the event there is no end, we return the latest point.
        return Math.max(startRange, (armed ? lastKnown : endRange));
    }

    public int getSelectedRangeSize()
    {
        return getSelectedRangeMaximum() - getSelectedRangeMinimum();
    }

    public void setSelectedRange(int selectionMin, int selectionMax)
    {
        reset();
        startRange = selectionMin;
        endRange = selectionMax;
        lastKnown = selectionMax;
    }
}
