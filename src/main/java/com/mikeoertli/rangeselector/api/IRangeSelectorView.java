package com.mikeoertli.rangeselector.api;

import com.mikeoertli.rangeselector.ui.common.IMouseInputHandler;

/**
 * Interface to which all range selector panels must adhere.
 *
 * @since 0.0.1
 */
public interface IRangeSelectorView
{

    /**
     * Reset the panel to a clean, initialized state. This will erase all data.
     */
    void reset();

    /**
     * @return a boolean that indicates whether the panel is locked (i.e. unable to accept input/change) or not
     */
    boolean isLocked();

    /**
     * Locks the panel to prevent any changes, whether via the UI or programmatic EXCEPT via the {@link #reset()}
     * method, which will reset even a locked panel.
     * <p>
     * If the panel is already locked, nothing happens.
     */
    void lockPanel();

    /**
     * Unlocks the panel so that it may receive input/changes from the user or programmatically.
     * <p>
     * If the panel is already unlocked, nothing happens.
     */
    void unlockPanel();

    /**
     * Adds a range selection handler. This will almost certainly have to be cast to the appropriate type for the
     * GUI framework being used, for example a {@link java.awt.event.MouseListener} for Swing
     *
     * @param handler the handler to be added
     */
    void addMouseInputHandler(IMouseInputHandler handler);

    /**
     * Removes a range selection handler. This will almost certainly have to be cast to the appropriate type for the
     * GUI framework being used, for example a {@link java.awt.event.MouseListener} for Swing
     *
     * @param handler the handler to be removed
     */
    void removeMouseInputHandler(IMouseInputHandler handler);

    /**
     * Refresh, redraw, repaint - do what is needed to do to ensure the most recent selected range is accurately drawn.
     */
    void refreshView();
}
