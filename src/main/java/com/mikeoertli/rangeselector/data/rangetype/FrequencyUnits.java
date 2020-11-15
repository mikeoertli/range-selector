package com.mikeoertli.rangeselector.data.rangetype;

import com.mikeoertli.rangeselector.api.IRangeType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * An {@link IRangeType} and unit of measure for frequency data (Hz)
 *
 * @since 0.0.1
 */
public enum FrequencyUnits implements IRangeType<Long>
{
    HZ("Hz", 1L),
    KHZ("KHz", 1000L),
    MHZ("MHz", 1_000_000L),
    GHZ("GHz", 1_000_000_000L),
    THZ("THz", 1_000_000_000_000L),
    UNKNOWN("UNKNOWN", 1L);

    private final String displayName;
    private final long multiplierToGetHz;

    FrequencyUnits(String displayName, long multiplierToGetHz)
    {
        this.displayName = displayName;
        this.multiplierToGetHz = multiplierToGetHz;
    }

    public static FrequencyUnits fromString(String inUnits)
    {
        FrequencyUnits[] values = values();
        for (FrequencyUnits unit : values)
        {
            if (unit.getDisplayName().equalsIgnoreCase(inUnits) && unit != UNKNOWN)
            {
                return unit;
            }
        }
        return UNKNOWN;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public long getMultiplierToGetHz()
    {
        return multiplierToGetHz;
    }

    @Override
    public String getName()
    {
        return "Frequency";
    }

    @Override
    public Optional<String> getUnitsOfMeasure()
    {
        return Optional.of(getDisplayName());
    }

    @Override
    public Optional<Long> getAbsoluteMinimum()
    {
        return Optional.of(0L);
    }

    @Override
    public Optional<Long> getAbsoluteMaximum()
    {
        return Optional.empty();
    }

    /**
     * Given a frequency, determine what units of measure are the most logical, Hz, KHz, MHz,
     * or GHz. Return the string of the frequency divided down then append the units.
     *
     * @param frequencyHz the frequency (in Hz) to convert to a string
     * @return the string representing a logical representation of the given speed
     */
    public static String getFrequencyStringFromNumber(long frequencyHz)
    {
        Double doubleFreqHz = getFrequencyNumberScaledToGuiFriendlyRange(frequencyHz);

        // Note: we use %s for frequency here because it handles decimal points more gracefully than something like %.3f etc.
        final FrequencyUnits frequencyUnits = getBestFrequencyUnitsForFrequency(frequencyHz);
        String frequencyUnitsDisplayName;
        if (UNKNOWN == frequencyUnits)
        {
            frequencyUnitsDisplayName = "";
        } else
        {
            frequencyUnitsDisplayName = " " + frequencyUnits.getDisplayName();
        }
        return String.format("%s%s", doubleFreqHz, frequencyUnitsDisplayName);
    }

    /**
     * Given a frequency, determine what units of measure are the most logical, Hz, KHz, MHz,
     * or GHz. Return the string of these units.
     *
     * @param frequencyHz the frequency (in Hz) to use for determining the most logical (user friendly) units for GUI display
     * @return the enum representing a GUI-friendly units for this frequency
     */
    public static FrequencyUnits getBestFrequencyUnitsForFrequency(long frequencyHz)
    {
        List<FrequencyUnits> valuesSortedMaxToMin = new ArrayList<>(Arrays.asList(values()));
        valuesSortedMaxToMin.remove(UNKNOWN);
        Collections.reverse(valuesSortedMaxToMin);

        for (FrequencyUnits unit : valuesSortedMaxToMin)
        {
            if (frequencyHz >= unit.getMultiplierToGetHz())
            {
                return unit;
            }
        }

        return UNKNOWN;
    }

    /**
     * Convert the given frequency in the stated units into the desired units.
     *
     * @param originalFrequency the original frequency in the units defined by next arg
     * @param startingUnits     the units of the given frequency
     * @param desiredUnits      the desired frequency of the result
     * @return the converted frequency in the new desired units
     */
    public static double convertFrequency(double originalFrequency, FrequencyUnits startingUnits, FrequencyUnits desiredUnits)
    {
        double frequencyHz = originalFrequency * startingUnits.getMultiplierToGetHz();

        double resultInDesiredUnits = frequencyHz / desiredUnits.getMultiplierToGetHz();

        if (desiredUnits == HZ && (resultInDesiredUnits % 1.0 != 0.0))
        {
            return Math.max(0, Math.floor(resultInDesiredUnits));
        } else
        {
            return Math.max(0, resultInDesiredUnits);
        }
    }

    /**
     * Get the number part of the "smart" GUI friendly representation of a frequency. This is assumed to be
     * coupled with {@link #getBestFrequencyUnitsForFrequency(long)} and is responsible for populating the
     * digits of {@link #getFrequencyStringFromNumber(long)}.
     *
     * @param frequencyHz the frequency in Hz to convert to a GUI-friendly number
     * @return the GUI-friendly version of the given frequency, units are assumed and can be obtained via passing
     * the same source frequency (in Hz) into {@link #getBestFrequencyUnitsForFrequency(long)}.
     */
    public static Double getFrequencyNumberScaledToGuiFriendlyRange(long frequencyHz)
    {
        double frequencyHzDouble = new Long(frequencyHz).doubleValue();
        FrequencyUnits desiredUnits = getBestFrequencyUnitsForFrequency(frequencyHz);
        double convertedFrequency = convertFrequency(frequencyHzDouble, HZ, desiredUnits);
        return Math.max(0, convertedFrequency);
    }

    /**
     * Return the frequency in Hz given some GUI values. For example, 3.14 and "KHz" would return
     * 3140L where the returned value is always in Hz. If the frequency is already in Hz and a
     * value containing a decimal is provided, that decimal is ignored since fractional Hz is
     * not a valid input. i.e. 99.5 Hz will return 99L.
     *
     * @param frequency the frequency double value in the units provided by the given string
     * @param units     the units obtained form the GUI or wherever
     * @return the frequency in units of Hz
     */
    public static long getFrequencyHzFromGuiValueAndUnits(double frequency, String units)
    {
        FrequencyUnits unitsEnum = fromString(units);
        return new Double(frequency * unitsEnum.getMultiplierToGetHz()).longValue();
    }
}
