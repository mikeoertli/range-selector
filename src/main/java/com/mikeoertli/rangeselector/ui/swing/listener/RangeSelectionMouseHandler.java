package com.mikeoertli.rangeselector.ui.swing.listener;

import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.ui.common.IMouseInputHandler;
import com.mikeoertli.rangeselector.ui.swing.common.RightClickMenuManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.SwingUtilities;
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
    private boolean locked = false;

    private int startRange = -1;

    private int endRange = -1;

    // Used to capture the end point while dragging is still in progress
    private int lastKnown = -1;

    private int captureCount = 0;

    private final IRangeViewController controller;
    private final RightClickMenuManager rightClickMenuManager;

    public RangeSelectionMouseHandler(IRangeViewController controller)
    {
        this.controller = controller;
        rightClickMenuManager = new RightClickMenuManager(controller);
    }

    @Override
    public void mousePressed(MouseEvent event)
    {
        if (event.getClickCount() != 2 && SwingUtilities.isLeftMouseButton(event))
        {
            startRangeCapture(event.getX());
        } else if (SwingUtilities.isRightMouseButton(event))
        {
            logger.trace("mousePressed - Processing right click");
            rightClickMenuManager.processEventShowPopup(event);
        }
    }

    @Override
    public void mouseReleased(MouseEvent event)
    {
        if (armed && SwingUtilities.isLeftMouseButton(event))
        {
            endRangeCapture(event.getX());
        } else if (SwingUtilities.isRightMouseButton(event))
        {
            logger.trace("mouseReleased - Processing right click");
            rightClickMenuManager.processEventShowPopup(event);
        }
    }

    @Override
    public void mouseClicked(MouseEvent event)
    {
        if (!locked)
        {
            if (SwingUtilities.isLeftMouseButton(event))
            {
                if (event.getClickCount() == 2)
                {
                    logger.trace("Selecting complete range due to single click.");
                    controller.selectAll();
                } else
                {
                    logger.trace("Resetting selection due to single click.");
                    reset();
                }
            } else
            {
                logger.trace("mouseClicked - Mouse click event for button {} is ignored.", event.getButton());
            }
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

    private void endRangeCapture(int xLocation)
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

    @Override
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

    @Override
    public void setLocked(boolean locked)
    {
        this.locked = locked;
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
