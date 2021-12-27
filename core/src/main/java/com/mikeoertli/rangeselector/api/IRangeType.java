package com.mikeoertli.rangeselector.api;

import java.util.Optional;

/**
 * Interface to define characteristics associated with the range type
 *
 * @since 0.0.1
 */
public interface IRangeType
{
    /**
     * @return the display name of this type of range, ex: "Speed" or "Frequency"
     */
    String getRangeTypeDisplayName();

    /**
     * @return The units of measure (for display purposes) if any for this range type. (ex: "MPH" or "MHz"
     */
    default Optional<String> getUnitsOfMeasure()
    {
        return Optional.empty();
    }

    /**
     * Defines an absolute minimum inherent to the type of range being handled. Note that an actual range
     * will have its own min/max, but that the range min/max is bound by this minimum and
     * this {@link IRangeType#getAbsoluteMaximum()} ()}.
     *
     * @return the absolute minimum associated with this type of range, if any, otherwise {@link Optional#empty()}
     */
    Optional<Double> getAbsoluteMinimum();

    /**
     * Defines an absolute maximum inherent to the type of range being handled. Note that an actual range
     * will have its own min/max, but that the range min/max is bound by the {@link IRangeType#getAbsoluteMinimum()} and
     * this maximum.
     *
     * @return the absolute maximum associated with this type of range, if any, otherwise {@link Optional#empty()}
     */
    Optional<Double> getAbsoluteMaximum();
}
