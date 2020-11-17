package com.mikeoertli.rangeselector.api;

/**
 * A simple range type is the same as a {@link IRangeType} except that it doesn't have a different data type
 * for raw data and for display
 *
 * @since 0.0.1
 * @param <N> the data type used for the raw data and for display, most types should use this instead of {@link IRangeType}
 */
public interface ISimpleRangeType<N extends Number> extends IRangeType<N, N>
{
    @Override
    default N convertDisplayValueToRawValue(N displayValue, String units)
    {
        return displayValue;
    }

    @Override
    default N convertRawValueToAutoScaledDisplayValue(N rawValue)
    {
        return rawValue;
    }

    @Override
    default String getValueAsString(N rawValue, boolean includeUnits)
    {
        return rawValue + (includeUnits && getUnitsOfMeasure().isPresent() ? getUnitsOfMeasure().get() : "");
    }
}
