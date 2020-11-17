package com.mikeoertli.rangeselector.api;

import org.apache.commons.lang3.Range;

import javax.swing.JSlider;
import java.util.Optional;

/**
 * Interface to define characteristics associated with the range type
 *
 * @param <RAW>     the data type of the raw data
 * @param <DISPLAY> the data type of the data when displayed in a user friendly way
 * @since 0.0.1
 */
public interface IRangeType<RAW extends Number, DISPLAY extends Number>
{
    /**
     * @return the display name of this type of range, ex: "Speed" or "Frequency"
     */
    String getName();

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
    Optional<RAW> getAbsoluteMinimum();

    /**
     * Defines an absolute maximum inherent to the type of range being handled. Note that an actual range
     * will have its own min/max, but that the range min/max is bound by the {@link IRangeType#getAbsoluteMinimum()} and
     * this maximum.
     *
     * @return the absolute maximum associated with this type of range, if any, otherwise {@link Optional#empty()}
     */
    Optional<RAW> getAbsoluteMaximum();

    /**
     * Retrieves the raw data value from the information that is user-facing on the display. For example, "1.2 GHz"
     * where {@code 1.2} would be the {@code displayValue} and {@code "GHz"} would be the {@code units}, would yield
     * {@code 1_200_000_000L} (in this example, the {@code RAW} type is {@link Long}).
     *
     * @param displayValue the display value as shown to the user (ex: 1.2)
     * @param units        the units of measure used for the display value
     * @return the display value converted to the raw data type
     */
    RAW convertDisplayValueToRawValue(DISPLAY displayValue, String units);

    /**
     * Converts the given raw data value into a value that is optimal for display.
     *
     * @param rawValue the raw value to be converted into a more display-friendly measure
     * @return the display friendly value that represents the raw value
     */
    DISPLAY convertRawValueToAutoScaledDisplayValue(RAW rawValue);

    /**
     * Returns the given raw value as a string that is scaled to a GUI friendly range, optionally including the units of
     * measure in the output. For example, {@code 1_200_000_000L} could be turned into {@code "1.2 GHz"}.
     *
     * @param rawValue     the raw data value to be scaled to a GUI friendly range and then displayed
     * @param includeUnits indicates whether the resulting string should include units of measure or not
     * @return the raw value scaled to a GUI friendly range, optionally containing the units of measure depending what
     * parameter was passed in.
     */
    String getValueAsString(RAW rawValue, boolean includeUnits);

    /**
     * Provides the given range as a double without scaling. This is done to help insulate a proliferation of classes
     * from having to handle a bunch of data types and just work with double, which should be flexbile enough to handle
     * most situations.
     *
     * @param rawRange the raw data range to be converted to double
     * @return the same range, unscaled, converted to doubles
     */
    default Range<Double> getRangeAsRawDouble(Range<RAW> rawRange)
    {
        return Range.between(rawRange.getMinimum().doubleValue(), rawRange.getMaximum().doubleValue());
    }
}
