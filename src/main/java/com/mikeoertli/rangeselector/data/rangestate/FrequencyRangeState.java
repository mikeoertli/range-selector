package com.mikeoertli.rangeselector.data.rangestate;

import com.mikeoertli.rangeselector.data.rangetype.FrequencyUnits;
import org.apache.commons.lang3.Range;

/**
 * Captures the state for a frequency range selection UI
 *
 * @since 0.0.1
 */
public class FrequencyRangeState extends ARangeState
{
    public static final String CENTER_FREQUENCY_DESCRIPTION = "Center Frequency (MHz)";
    public static final String CENTER_FREQUENCY_SHORT_LABEL = "Freq (MHz)";

    public FrequencyRangeState(Range<Double> absoluteAllowedRange)
    {
        super(FrequencyUnits.MHZ, absoluteAllowedRange, CENTER_FREQUENCY_DESCRIPTION, CENTER_FREQUENCY_SHORT_LABEL);
    }
}