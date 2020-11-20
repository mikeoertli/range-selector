package com.mikeoertli.rangeselector.data.rangetype;

import com.mikeoertli.rangeselector.api.IRangeType;

import java.util.Optional;

/**
 * An ultra-simple {@link IRangeType} for {@link Integer} values like counts
 *
 * @since 0.0.1
 */
public class SimpleCount implements IRangeType
{
    public static final SimpleCount BASIC = new SimpleCount(0, 100);
    public static final SimpleCount LARGE = new SimpleCount(0, 1000);
    public static final SimpleCount SYMMETRICAL = new SimpleCount(-100, 1000);

    private static final int DEFAULT_RANGE_MINIMUM = 0;
    private static final int DEFAULT_RANGE_MAXIMUM = 100;

    protected final Integer minimum;
    protected final Integer maximum;
    protected String label;
    protected String units;

    private SimpleCount(Integer minimum, Integer maximum)
    {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public SimpleCount()
    {
        this(DEFAULT_RANGE_MINIMUM, DEFAULT_RANGE_MAXIMUM);
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public void setUnits(String units)
    {
        this.units = units;
    }

    @Override
    public String getLabel()
    {
        return label;
    }

    @Override
    public Optional<String> getUnitsOfMeasure()
    {
        return Optional.of(units);
    }

    @Override
    public Optional<Double> getAbsoluteMinimum()
    {
        return Optional.of(minimum.doubleValue());
    }

    @Override
    public Optional<Double> getAbsoluteMaximum()
    {
        return Optional.of(maximum.doubleValue());
    }
}
