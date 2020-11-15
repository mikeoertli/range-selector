package com.mikeoertli.rangeselector.data.rangetype;

import com.mikeoertli.rangeselector.api.IRangeType;

import java.util.Optional;

/**
 * An ultra-simple {@link IRangeType} for {@link Integer} values like counts
 *
 * @since 0.0.1
 */
public class SimpleCount implements IRangeType<Integer>
{


    @Override
    public String getName()
    {
        return "Count";
    }

    @Override
    public Optional<Integer> getAbsoluteMinimum()
    {
        return Optional.of(Integer.MIN_VALUE);
    }

    @Override
    public Optional<Integer> getAbsoluteMaximum()
    {
        return Optional.of(Integer.MAX_VALUE);
    }
}
