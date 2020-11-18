package com.mikeoertli.rangeselector.data.rangestate;

import com.mikeoertli.rangeselector.data.rangetype.SimpleCount;
import org.apache.commons.lang3.Range;

/**
 * The range state for simple count ranges
 *
 * @since 0.0.1
 */
public class SimpleCountState extends ARangeState
{
    public SimpleCountState(SimpleCount rangeType, Range<Double> allowedRange, String fullDescription, String shortLabel)
    {
        super(rangeType, allowedRange, fullDescription, shortLabel);
    }
}
