package com.mikeoertli.rangeselector.data;

import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Captures the state of a range selection panel
 *
 * @since 0.0.1
 */
public class RangeConfiguration
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    /**
     * Constant indicates that there is no selected range for the min or max where this is used
     */
    public static final int NO_SELECTION = -1;

    /**
     * Constant to define the default minimum index of the selectable range if none is given
     */
    protected static final int DEFAULT_RANGE_MINIMUM = 0;

    /**
     * Constant to defne the default maximum index of the selectable range if none is given
     */
    protected static final int DEFAULT_RANGE_MAXIMUM = 100;

    protected int rangeMin;
    protected int rangeMax;
    protected int selectionMin = NO_SELECTION;
    protected int selectionMax = NO_SELECTION;

    protected final String fullDescription;
    protected final String shortLabel;

    protected boolean locked;

    protected Instant lastUpdateTime;

    protected String primaryLabel;
    protected String secondaryLabel;

    protected final List<Integer> primaryData;
    protected final List<Integer> secondaryData;

    /**
     * Creates a new default range configuration using all defaults
     */
    public RangeConfiguration()
    {
        this(DEFAULT_RANGE_MINIMUM, DEFAULT_RANGE_MAXIMUM, null, null, new ArrayList<>(), new ArrayList<>(),
                null, null);
    }

    /**
     * Clones the given configuration into a new one.
     *
     * @param toClone the range configuration to clone (save for the timestamp, which will be now)
     */
    public RangeConfiguration(RangeConfiguration toClone)
    {
        this(toClone.getRangeMin(), toClone.getRangeMax(), toClone.getFullDescription(), toClone.getShortLabel(),
                toClone.getPrimaryData(), toClone.getSecondaryData(), toClone.getPrimaryLabel(), toClone.getSecondaryLabel());
        setSelectionMin(toClone.getSelectionMin());
        setSelectionMax(toClone.getSelectionMax());
        setLocked(toClone.isLocked());
        setPrimaryData(toClone.getPrimaryData());
        setSecondaryData(toClone.getSecondaryData());
        onStateUpdated();
    }

    /**
     * Constructor that defines a new range configuration using all parameters.
     *
     * @param rangeMin        the minimum index of the selectable range (default: {@link #DEFAULT_RANGE_MINIMUM})
     * @param rangeMax        the maximum index of the selectable range (default: {@link #DEFAULT_RANGE_MAXIMUM})
     * @param fullDescription the full/long description of what this range data represents
     * @param shortLabel      the short description of what this range data represents
     * @param primaryData     the primary data set to be used, if applicable, otherwise null or an empty list
     * @param secondaryData   the secondary data set to be used, if applicable, otherwise null or an empty list.
     *                        Note that this should only be used if the primary data set is used and this should either
     *                        be the exact same length as the primary data set or should be empty.
     * @param primaryLabel    the label to give the primary data in the legend, if applicable
     * @param secondaryLabel  the label to give the secondary data in the legend, if applicable
     */
    public RangeConfiguration(int rangeMin, int rangeMax, String fullDescription, String shortLabel, List<Integer> primaryData,
                              List<Integer> secondaryData, String primaryLabel, String secondaryLabel)
    {
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;

        this.fullDescription = fullDescription;
        this.shortLabel = shortLabel;

        if (primaryData == null)
        {
            primaryData = new ArrayList<>();
        }
        this.primaryData = new ArrayList<>(primaryData);

        if (secondaryData == null)
        {
            secondaryData = new ArrayList<>();
        }
        this.secondaryData = new ArrayList<>(secondaryData);

        this.primaryLabel = primaryLabel;
        this.secondaryLabel = secondaryLabel;

        onStateUpdated();

        logger.trace("Initialized state: {}", this);
    }

    /**
     * @return an unmodifiable copy of the primary data set
     */
    public List<Integer> getPrimaryData()
    {
        return Collections.unmodifiableList(primaryData);
    }

    /**
     * @return an unmodifiable copy of the secondary data set
     */
    public List<Integer> getSecondaryData()
    {
        return Collections.unmodifiableList(secondaryData);
    }

    /**
     * @param data the new data set to replace the entirety of the secondary data set
     */
    public void setPrimaryData(List<Integer> data)
    {
        primaryData.clear();
        primaryData.addAll(data);
    }

    /**
     * @param data the new data set to replace the entirety of the secondary data set
     */
    public void setSecondaryData(List<Integer> data)
    {
        secondaryData.clear();
        secondaryData.addAll(data);
    }

    /**
     * @return the primary data set label for the legend (if applicable)
     */
    public String getPrimaryLabel()
    {
        return primaryLabel;
    }

    /**
     * @param primaryLabel sets the primary data set label for the legend (if applicable)
     */
    public void setPrimaryLabel(String primaryLabel)
    {
        this.primaryLabel = primaryLabel;
    }

    /**
     * @return the secondary data set label for the legend (if applicable)
     */
    public String getSecondaryLabel()
    {
        return secondaryLabel;
    }

    /**
     * @param secondaryLabel sets the secondary data set label for the legend (if applicable)
     */
    public void setSecondaryLabel(String secondaryLabel)
    {
        this.secondaryLabel = secondaryLabel;
    }

    /**
     * @return the short label to describe what the range represents
     */
    public String getShortLabel()
    {
        return shortLabel;
    }

    /**
     * @return the full/long description that describes what the range selection panel and the range itself represents
     */
    public String getFullDescription()
    {
        return fullDescription;
    }

    /**
     * The non-safe way to retrieve teh selected range from {@link #getSelectionMin()} to {@link #getSelectionMax()} or
     * else null if those are not both >= 0.
     * <p>
     * For the safe selected range retrieval, look at {@link #tryGetSelectedRange()}
     *
     * @return the selected range if any, otherwise null
     * @see #tryGetSelectedRange()
     */
    public Range<Integer> getSelectedRange()
    {
        return tryGetSelectedRange().orElse(null);
    }

    /**
     * Same as {@link #getSelectedRange()} but the result is an optional to support a safe technique for
     * obtaining the selected range.
     *
     * @return the selected range from {@link #getSelectionMin()} to {@link #getSelectionMax()} if those are both
     * >= 0, otherwise {@link Optional#empty()}
     */
    public Optional<Range<Integer>> tryGetSelectedRange()
    {
        if (selectionMin >= 0 && selectionMax >= 0)
        {
            return Optional.of(Range.between(selectionMin, selectionMax));
        } else
        {
            return Optional.empty();
        }
    }

    /**
     * @return a boolean to indicate that there is a valid selected range (true) or not (false)
     */
    public boolean hasSelection()
    {
        return tryGetSelectedRange().isPresent();
    }

    /**
     * Sets the minimum index of the selected range, this should be -1 (or {@link #NO_SELECTION}) if there is no selected
     * range.
     * <p>
     * Note that when a selection is from right -> left, this will be the end of the selection rather than the start.
     * <p>
     * If the range selection min being configured here is different than what it was before, the last update time
     * will be captured, otherwise nothing happens.
     *
     * @param selectionMin the new minimum index along the X-axis of the selected range, this will be between the
     *                     {@link #getRangeMin()} and {@link #getRangeMax()} values and less than or equal to
     *                     {@link #getSelectionMax()}.
     */
    public void setSelectionMin(int selectionMin)
    {
        if (this.selectionMin != selectionMin)
        {
            this.selectionMin = selectionMin;
            onStateUpdated();
        }
    }

    /**
     * Sets the maximum index of the selected range, this should be -1 (or {@link #NO_SELECTION}) if there is no selected
     * range.
     * <p>
     * Note that when a selection is from right -> left, this will be the start of the selection.
     * <p>
     * If the range selection max being configured here is different than what it was before, the last update time
     * will be captured, otherwise nothing happens.
     *
     * @param selectionMax the new maximum index along the X-axis of the selected range, this will be between the
     *                     {@link #getRangeMin()} and {@link #getRangeMax()} values and will be greater than or equal
     *                     to {@link #getSelectionMin()}
     */
    public void setSelectionMax(int selectionMax)
    {
        if (this.selectionMax != selectionMax)
        {
            this.selectionMax = selectionMax;
            onStateUpdated();
        }
    }

    /**
     * @return the absolute minimum index of the range that could be selected (always 0)
     */
    public int getRangeMin()
    {
        return rangeMin;
    }

    /**
     * @return the absolute maximum possible index of the range that could be selected
     */
    public int getRangeMax()
    {
        return rangeMax;
    }

    /**
     * Set the upper limit (maximum) of the selectable range
     * <p>
     * Note the omission of setting the range minimum is intentional as it is always zero.
     *
     * @param rangeMax the upper limit (maximum) of the selectable range
     */
    public void setRangeMax(int rangeMax)
    {
        this.rangeMax = rangeMax;
        onStateUpdated();
    }

    /**
     * @return the minimum point of selection or -1 if there is none, while selection is active, this is just the min
     * of the selected (but still changing) region. If the selection is right -> left, this will be the end of the
     * selected range since that will be the minimum of the selected range values.
     */
    public int getSelectionMin()
    {
        return selectionMin;
    }

    /**
     * @return the maximum point of selection or -1 if there is none, while selection is active, this is just the max
     * of the selected (but still changing) region. If the selection is right -> left, this will be the start of the
     * selected range since that will be the maximum of the selected range values.
     */
    public int getSelectionMax()
    {
        return selectionMax;
    }

    /**
     * @return a boolean to indicate whether or not the range configuration is locked
     */
    public boolean isLocked()
    {
        return locked;
    }

    /**
     * Sets the locked state of the range configuration
     *
     * @param locked a boolean to indicate whether or not the range configuration is locked
     */
    public void setLocked(boolean locked)
    {
        if (this.locked != locked)
        {
            this.locked = locked;
            onStateUpdated();
        }
    }

    /**
     * @return the instant at which this configuration was last updated
     */
    public Instant getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    /**
     * Clear the selected range, ex: called on double click
     */
    public void clearSelection()
    {
        setSelectionMin(NO_SELECTION);
        setSelectionMax(NO_SELECTION);
    }

    /**
     * Capture the last time this configuration was updated
     */
    private void onStateUpdated()
    {
        lastUpdateTime = Instant.now();
    }

    /**
     * Retrieves the largest value from either data set, this is used to normalize data in the Y direction
     * when necessary.
     *
     * @return the largest value in either the primary or secondary data set, if applicable, otherwise 0
     */
    public int getLargestValueFromDataSets()
    {
        final int maxPrimaryDataPoint = primaryData.stream().mapToInt(i -> i).max().orElse(0);
        final int maxSecondaryDataPoint = secondaryData.stream().mapToInt(i -> i).max().orElse(0);
        return Math.max(maxPrimaryDataPoint, maxSecondaryDataPoint);
    }

    /**
     * @return the size of the data sets, otherwise 0
     */
    public int getDataSize()
    {
        final int primaryDataSetSize = primaryData.size();
        final int secondaryDataSetSize = secondaryData.size();
        if (primaryDataSetSize != secondaryDataSetSize && secondaryDataSetSize != 0)
        {
            logger.warn("The primary data set contains {} data, but the secondary has {}, these should match or else " +
                    "the secondary should be empty.", primaryDataSetSize, secondaryDataSetSize);
        }
        return primaryDataSetSize;
    }
}
