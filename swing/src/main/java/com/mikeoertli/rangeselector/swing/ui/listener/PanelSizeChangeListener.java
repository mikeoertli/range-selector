package com.mikeoertli.rangeselector.swing.ui.listener;

import com.mikeoertli.rangeselector.api.IRangeViewController;

import javax.swing.Timer;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * Listens to changes in the {@link com.mikeoertli.rangeselector.api.IRangeSelectorView} and notifies its
 * {@link com.mikeoertli.rangeselector.api.IRangeViewController} that the component as been resized.
 * <p>
 * In an effort to reduce churn, there is a configurable (100ms default) delay between a resize event and the notification
 * of the resize, this allows for a reduction in the amount of times a resize event is processed at each pixel change
 * as the resize takes place.
 *
 * @since 0.0.2
 */
public class PanelSizeChangeListener extends ComponentAdapter
{
    private Timer resizeCompleteTimer;
    private static final int DEFAULT_RESIZE_TIMER_WAIT_MS = 100;
    private final int resizeNotificationDelayMs;

    private final IRangeViewController controller;

    /**
     * Construct a new size change listener
     *
     * @param controller                the controller to be notified when the panel resizing is complete
     * @param resizeNotificationDelayMs the number of ms to delay notifying the controller of the resize to avoid
     *                                  churn while a panel is still being resized (Default is 100ms).
     */
    public PanelSizeChangeListener(IRangeViewController controller, int resizeNotificationDelayMs)
    {
        this.controller = controller;
        this.resizeNotificationDelayMs = resizeNotificationDelayMs;
    }

    /**
     * Construct a new size change listener using the default 100ms delay
     *
     * @param controller the controller to be notified when the panel resizing is complete
     */
    public PanelSizeChangeListener(IRangeViewController controller)
    {
        this(controller, DEFAULT_RESIZE_TIMER_WAIT_MS);
    }

    @Override
    public void componentResized(ComponentEvent e)
    {

        if (resizeCompleteTimer == null)
        {
            ActionListener actionListener = e1 -> {
                resizeCompleteTimer.stop();
                resizeCompleteTimer = null;
                controller.onViewSizeChanged();
            };
            resizeCompleteTimer = new Timer(resizeNotificationDelayMs, actionListener);
            resizeCompleteTimer.start();
        } else
        {
            resizeCompleteTimer.restart();
        }
    }
}
