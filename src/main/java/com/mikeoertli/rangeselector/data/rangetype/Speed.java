package com.mikeoertli.rangeselector.data.rangetype;

import com.mikeoertli.rangeselector.api.IRangeType;

import java.util.Optional;

/**
 * Speed range type, in meters per second.
 *
 * @since 0.0.1
 */
public class Speed implements IRangeType<Double>
{
    public static final double SPEED_OF_LIGHT_MPS = 299_792_458.0;

    @Override
    public String getName()
    {
        return "Speed";
    }

    @Override
    public Optional<String> getUnitsOfMeasure()
    {
        return Optional.of("m/s");
    }

    @Override
    public Optional<Double> getAbsoluteMinimum()
    {
        return Optional.of(0.0);
    }

    @Override
    public Optional<Double> getAbsoluteMaximum()
    {
        return Optional.of(SPEED_OF_LIGHT_MPS);
    }
}
