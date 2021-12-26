package com.mikeoertli.rangeselector.javafx.ui;

import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Base class for JavaFX range selection GUIs
 */
public abstract class AFxRangeSelectionPane extends Pane implements IRangeSelectorView<EventHandler<MouseEvent>>
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected final IJavaFxViewController controller;

    public AFxRangeSelectionPane(IJavaFxViewController controller)
    {
        super();
        this.controller = controller;

        configureMinimumSize();
    }

    private void configureMinimumSize()
    {
        final double minWidth = getMinWidth();
        final double minHeight = getMinHeight();
        final int controllerMinWidth = controller.getMinimumViewWidth();

        if (controllerMinWidth != (int) minWidth)
        {
            logger.trace("Setting minimum size from {}x{} to {}x{}", minHeight, minWidth, minHeight, controllerMinWidth);
            setMinWidth(controllerMinWidth);
        }
    }

    @Override
    public void lockPanel()
    {
        // Ignore
    }

    @Override
    public void unlockPanel()
    {
        // Ignore
    }

    @Override
    public void addMouseInputHandler(EventHandler<MouseEvent> handler) throws IllegalArgumentException
    {
        setOnMouseClicked(handler);
        setOnMouseDragged(handler);
        setOnMouseExited(handler);
        setOnMousePressed(handler);
        setOnMouseReleased(handler);
    }

    @Override
    public void removeMouseInputHandler(EventHandler<MouseEvent> handler)
    {
        removeEventHandler(MouseEvent.MOUSE_CLICKED, handler);
        removeEventHandler(MouseEvent.MOUSE_DRAGGED, handler);
        removeEventHandler(MouseEvent.MOUSE_PRESSED, handler);
        removeEventHandler(MouseEvent.MOUSE_RELEASED, handler);
        removeEventHandler(MouseEvent.MOUSE_EXITED, handler);
    }

    @Override
    public void reset()
    {
        logger.trace("Resetting {}", getClass().getSimpleName());
    }

    @Override
    public void refreshView()
    {
        logger.trace("refreshView for {}", getClass().getSimpleName());
        configureMinimumSize();
    }
}
