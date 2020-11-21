package com.mikeoertli.rangeselector.api;

/**
 * Interface for a class to implement if it wants to be notified of the range selection (and de-selection) events.
 *
 * @since 0.0.2
 */
public interface IRangeSelectionListener
{
    /**
     * A new range has been selected.
     *
     * @param rangeMin     the minimum end of the selectable range, usually 0
     * @param rangeMax     the maximum of the selectable range
     * @param selectionMin the minimum index of the selected range, will be greater than or equal to {@code rangeMin}
     * @param selectionMax the maximum index of the selected range, will be less than or equal to {@code rangeMax}
     */
    void onRangeSelected(int rangeMin, int rangeMax, int selectionMin, int selectionMax);

    /**
     * The range selection has been cleared, no range is selected
     */
    void onRangeSelectionCleared();
}
