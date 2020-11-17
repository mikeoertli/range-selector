package com.mikeoertli.rangeselector.api;

import org.apache.commons.lang3.Range;

import java.time.Instant;
import java.util.Optional;

/**
 * Defines the basic interface for all types of range and range view state/configuration information. It is recommended
 * to extend {@link com.mikeoertli.rangeselector.data.rangestate.ARangeState} if adding your own.
 *
 * @since 0.0.1
 */
public interface IRangeState<RANGE extends IRangeType<? extends Number, ? extends Number>>
{
    /**
     * @return the shortened label version of the description that captures info about the type of data in this label
     */
    String getShortLabel();

    /**
     * @return the full description of the data captured in this state object
     */
    String getFullDescription();

    /**
     * @return the {@link IRangeType} of teh view/controller associated with this saved ranged state
     */
    RANGE getRangeType();

    /**
     * @return the selected/active range from the view if any, otherwise {@link Optional#empty()}
     */
    Optional<Range<Double>> getSelectedRange();

    /**
     * Set the selected/active range using the RAW data value. This range MUST fall within the absolute range defined
     * by {@link #getRangeExtrema()}.
     * <p>
     * Range containment is determined via {@link Range#containsRange(Range)}.
     *
     * @param selectedRange the range that is selected within the absolute range in the .
     * @throws IllegalArgumentException if the given range is not within the {@link #getRangeExtrema()}
     */
    void setSelectedRange(Range<Double> selectedRange) throws IllegalArgumentException;

    /**
     * This is the absolute maximum allowable range (the extrema of the visible range selector as well as the min/max
     * allowable values for the {@link #getSelectedRange()}.
     *
     * @return the absolute allowable range
     */
    Range<Double> getRangeExtrema();

    /**
     * @return a boolean to indicate whether the range selection UI is locked (i.e. disabled) to prevent user input (true) or not (false)
     */
    boolean isLocked();

    /**
     * Set/clear the locked state of the view.
     *
     * @param locked indicates whether the view is locked to prevent user input
     */
    void setLocked(boolean locked);

    /**
     * @return the {@link Instant} that this configuration was last updated
     */
    Instant getLastUpdateTime();
}
