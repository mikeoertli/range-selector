package com.mikeoertli.rangeselector.api;

/**
 * Interface to which all range selector panels must adhere.
 *
 * @since 0.0.1
 */
public interface IRangeSelectorView<MOUSE_LISTENER>
{

    /**
     * Reset the panel to a clean, initialized state. This will erase all data.
     */
    void reset();

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
     * GUI framework being used, for example...
     * {@link java.awt.event.MouseListener} and {@link java.awt.event.MouseMotionListener} for Swing
     * {@link javafx.event.EventHandler} for JavaFX
     *
     * @param handler the handler to be added
     * @throws IllegalArgumentException if the given mouse input handler doesn't meet the requirements dictated by the
     *                                  particular GUI framework
     */
    void addMouseInputHandler(MOUSE_LISTENER handler) throws IllegalArgumentException;

    /**
     * Removes a range selection handler. This will almost certainly have to be cast to the appropriate type for the
     * GUI framework being used, for example...
     * {@link java.awt.event.MouseListener} and {@link java.awt.event.MouseMotionListener} for Swing
     * {@link javafx.event.EventHandler} for JavaFX
     *
     * @param handler the handler to be removed
     */
    void removeMouseInputHandler(MOUSE_LISTENER handler);

    /**
     * Refresh, redraw, repaint - do what is needed to do to ensure the most recent selected range is accurately drawn.
     */
    void refreshView();
}
