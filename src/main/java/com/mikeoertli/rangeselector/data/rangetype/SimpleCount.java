package com.mikeoertli.rangeselector.data.rangetype;

import com.mikeoertli.rangeselector.api.IRangeType;
import com.mikeoertli.rangeselector.api.ISimpleRangeType;

import java.util.Optional;

/**
 * An ultra-simple {@link IRangeType} for {@link Integer} values like counts
 *
 * @since 0.0.1
 */
public class SimpleCount implements ISimpleRangeType<Integer>
{
    public static final String DEFAULT_COUNT_UNITS = "count";
    public static final String DEFAULT_COUNT_NAME = "Simple Count";

    protected final Integer minimum;
    protected final Integer maximum;
    protected final String name;
    protected final String units;

    public SimpleCount(Integer minimum, Integer maximum, String name, String units)
    {
        this.minimum = minimum;
        this.maximum = maximum;
        this.name = name;
        this.units = units;
    }

    public SimpleCount()
    {
        this(Integer.MIN_VALUE, Integer.MAX_VALUE, DEFAULT_COUNT_NAME, DEFAULT_COUNT_UNITS);
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public Optional<String> getUnitsOfMeasure()
    {
        return Optional.of(units);
    }

    @Override
    public Optional<Integer> getAbsoluteMinimum()
    {
        return Optional.of(minimum);
    }

    @Override
    public Optional<Integer> getAbsoluteMaximum()
    {
        return Optional.of(maximum);
    }

    @Override
    public String getValueAsString(Integer rawValue, boolean includeUnits)
    {
        return rawValue + (includeUnits ? units : "");
    }
}
