package com.mikeoertli.rangeselector.swing.ui.listener;

import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.core.RangeController;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.swing.ui.common.RightClickMenuSwingController;
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
public class RangeSelectionSwingMouseAdapter extends MouseAdapter
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RangeController rangeController;
    private final RightClickMenuSwingController rightClickController;
    private final IRangeViewController rangeViewController;

    public RangeSelectionSwingMouseAdapter(RangeController rangeController, IRangeViewController rangeViewController)
    {
        this.rangeController = rangeController;
        this.rangeViewController = rangeViewController;
        rightClickController = new RightClickMenuSwingController(rangeViewController);
    }

    @Override
    public void mousePressed(MouseEvent event)
    {
        if (event.getClickCount() != 2 && SwingUtilities.isLeftMouseButton(event))
        {
            rangeController.startRangeCapture(event.getX());
        } else if (SwingUtilities.isRightMouseButton(event))
        {
            logger.trace("mousePressed - Processing right click");
            rightClickController.processEventShowPopup(event);
        }
    }

    @Override
    public void mouseReleased(MouseEvent event)
    {
        if (rangeController.isArmed() && SwingUtilities.isLeftMouseButton(event))
        {
            rangeController.endRangeCapture(event.getX());
        } else if (SwingUtilities.isRightMouseButton(event))
        {
            logger.trace("mouseReleased - Processing right click");
            rightClickController.processEventShowPopup(event);
        }
    }

    @Override
    public void mouseClicked(MouseEvent event)
    {
        if (!rangeController.isLocked())
        {
            if (SwingUtilities.isLeftMouseButton(event))
            {
                if (event.getClickCount() == 2)
                {
                    logger.trace("Selecting complete range due to single click.");
                    RangeConfiguration rangeConfiguration = rangeViewController.getRangeConfiguration();
                    rangeController.setSelectedRange(rangeConfiguration.getRangeMin(), rangeConfiguration.getRangeMax());
                } else
                {
                    logger.trace("Resetting selection due to single click.");
                    rangeController.reset();
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
        if (rangeController.isArmed())
        {
            rangeController.endRangeCapture(e.getX());
        }
    }

    @Override
    public void mouseDragged(MouseEvent e)
    {
        rangeController.selectedRangeChanged(e.getX());
    }
}
