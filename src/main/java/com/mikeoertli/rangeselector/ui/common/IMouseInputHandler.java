package com.mikeoertli.rangeselector.ui.common;

/**
 * Common interface for the mouse and mouse motion listeners that listen for input from the user performing a selection.
 *
 * @since 0.0.2
 */
public interface IMouseInputHandler
{
    /**
     * @return a boolean to indicate whether there is an open/in-progress/active selection
     */
    boolean isArmed();

    /**
     * @return a boolean to indicate that there is a selected range
     */
    boolean hasRange();

    /**
     * @return the minimum of the selected range or -1 if there is no selected range
     */
    int getSelectedRangeMinimum();

    /**
     * @return the maximum of the selected range or -1 if there is no selected range or a range selection has started,
     * but has not completed (and therefore has no end).
     */
    int getSelectedRangeMaximum();

    /**
     * @return the size of the selected range or 0 if there is no selected range
     */
    int getSelectedRangeSize();

    /**
     * Sets the selected range programmatically.
     *
     * @param selectionMin the minimum index of the selected range
     * @param selectionMax the maximum index of the eslected range
     */
    void setSelectedRange(int selectionMin, int selectionMax);

    /**
     * Cancel and clear any range selection, whether in-progress or complete. If there is no range selection, no
     * action is taken.
     */
    void reset();

    /**
     * Enable or disable the selection lock. When enabled, the current selection state is locked and cannot be changed.
     * If no selection is made, none can be made until unlocked. If a selection is made, it cannot be cleared, nor can
     * another selection be made until it is unlocked.
     *
     * @param locked locks the current selection state if true
     */
    void setLocked(boolean locked);
}
