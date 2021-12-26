package com.mikeoertli.rangeselector.ui.javafx.listener;

import com.mikeoertli.rangeselector.api.IRangeViewController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class StageSizeChangeListener implements ChangeListener<Number>
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

//    private Timer resizeCompleteTimer;
//    private static final int DEFAULT_RESIZE_TIMER_WAIT_MS = 100;
//    private static final String TIMER_NAME = "STAGE_TIMER";
//    private final int resizeNotificationDelayMs;

    private final IRangeViewController controller;

//    /**
//     * Construct a new size change listener
//     *
//     * @param controller                the controller to be notified when the panel resizing is complete
//     * @param resizeNotificationDelayMs the number of ms to delay notifying the controller of the resize to avoid
//     *                                  churn while a panel is still being resized (Default is 100ms).
//     */
//    public StageSizeChangeListener(IRangeViewController controller, int resizeNotificationDelayMs)
//    {
//        this.controller = controller;
//        this.resizeNotificationDelayMs = resizeNotificationDelayMs;
//    }

    /**
     * Construct a new size change listener using the default 100ms delay
     *
     * @param controller the controller to be notified when the panel resizing is complete
     */
    public StageSizeChangeListener(IRangeViewController controller)
    {
        this.controller = controller;
    }

    @Override
    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
    {
        logger.trace("{} -- {} changed from {} to {}", getClass().getSimpleName(), observable.getClass().getSimpleName(), oldValue, newValue);
        controller.onViewSizeChanged();

//        if (resizeCompleteTimer == null)
//        {
////            ActionListener actionListener = e1 -> {
////                resizeCompleteTimer.stop();
////                resizeCompleteTimer = null;
////                controller.onViewSizeChanged();
////            };
//            resizeCompleteTimer = new Timer(TIMER_NAME);
//            resizeCompleteTimer.
//            resizeCompleteTimer.start();
//        } else
//        {
//            resizeCompleteTimer.restart();
//        }
    }
}
