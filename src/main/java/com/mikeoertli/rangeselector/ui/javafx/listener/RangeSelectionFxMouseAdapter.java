package com.mikeoertli.rangeselector.ui.javafx.listener;

import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.core.RangeController;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.Map;

/**
 * Handler for mouse input events in JavaFX GUIs
 * Manages updating the {@link RangeController} to track the selected range
 *
 * @since 0.1.0
 */
public class RangeSelectionFxMouseAdapter implements EventHandler<MouseEvent>
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final RangeController rangeController;
    private final Map<EventType<? extends MouseEvent>, EventHandler<MouseEvent>> eventHandlers;
    private final IRangeViewController rangeViewController;

    public RangeSelectionFxMouseAdapter(RangeController rangeController, IRangeViewController rangeViewController)
    {
        this.rangeController = rangeController;
        this.rangeViewController = rangeViewController;

        eventHandlers = initializeEventTypeHandlers();
    }

    private Map<EventType<? extends MouseEvent>, EventHandler<MouseEvent>> initializeEventTypeHandlers()
    {

        Map<EventType<? extends MouseEvent>, EventHandler<MouseEvent>> handlerMap = new HashMap<>();
        handlerMap.put(MouseEvent.MOUSE_CLICKED, this::onMouseClicked);
        handlerMap.put(MouseEvent.MOUSE_DRAGGED, this::onMouseDragged);
        handlerMap.put(MouseEvent.MOUSE_EXITED, this::onMouseExited);
        handlerMap.put(MouseEvent.MOUSE_PRESSED, this::onMousePressed);
        handlerMap.put(MouseEvent.MOUSE_RELEASED, this::onMouseReleased);
        return handlerMap;
    }

    @Override
    public void handle(MouseEvent event)
    {
        EventType<? extends MouseEvent> eventType = event.getEventType();

        if (eventHandlers.containsKey(eventType))
        {
            eventHandlers.get(eventType).handle(event);
        }
    }

    private void onMouseClicked(MouseEvent event)
    {

        if (!rangeController.isLocked())
        {
            if (isLeftClick(event))
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

    private boolean isLeftClick(MouseEvent event)
    {
        return MouseButton.PRIMARY.equals(event.getButton());
    }

    private void onMouseDragged(MouseEvent event)
    {
        rangeController.selectedRangeChanged((int) event.getX());
    }

    private void onMouseExited(MouseEvent event)
    {
        if (rangeController.isArmed())
        {
            rangeController.endRangeCapture((int) event.getX());
        }
    }

    private void onMousePressed(MouseEvent event)
    {
        if (event.getClickCount() != 2 && isLeftClick(event))
        {
            rangeController.startRangeCapture((int) event.getX());
        }
    }

    private void onMouseReleased(MouseEvent event)
    {
        if (rangeController.isArmed() && isLeftClick(event))
        {
            rangeController.endRangeCapture((int) event.getX());
        }
    }
}
