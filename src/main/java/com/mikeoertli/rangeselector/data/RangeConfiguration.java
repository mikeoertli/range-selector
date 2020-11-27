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
    private static final int NO_SELECTION = -1;
    private static final int DEFAULT_RANGE_MINIMUM = 0;
    private static final int DEFAULT_RANGE_MAXIMUM = 100;

    protected int rangeMin;
    protected int rangeMax;
    protected int selectionMin = -1;
    protected int selectionMax = -1;

    protected final String fullDescription;
    protected final String shortLabel;

    protected boolean locked;

    protected Instant lastUpdateTime;

    protected String primaryLabel;
    protected String secondaryLabel;

    protected final List<Integer> primaryData;
    protected final List<Integer> secondaryData;

    public RangeConfiguration()
    {
        this(DEFAULT_RANGE_MINIMUM, DEFAULT_RANGE_MAXIMUM, null, null, new ArrayList<>(), new ArrayList<>(),
                null, null);
    }

    public RangeConfiguration(RangeConfiguration toClone)
    {
        this(toClone.getRangeMin(), toClone.getRangeMax(), toClone.getFullDescription(), toClone.getShortLabel(),
                toClone.getPrimaryData(), toClone.getSecondaryData(), toClone.getPrimaryLabel(), toClone.getSecondaryLabel());
        setSelectionMin(toClone.getSelectionMin());
        setSelectionMax(toClone.getSelectionMax());
        setLocked(toClone.isLocked());
        setPrimaryData(toClone.getPrimaryData());
        setSecondaryData(toClone.getSecondaryData());
        lastUpdateTime = toClone.getLastUpdateTime();
    }

    public RangeConfiguration(int rangeMin, int rangeMax, String fullDescription, String shortLabel, List<Integer> primaryData,
                              List<Integer> secondaryData, String primaryLabel, String secondaryLabel)
    {
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;

        this.fullDescription = fullDescription;
        this.shortLabel = shortLabel;

        this.primaryData = new ArrayList<>(primaryData);
        this.secondaryData = new ArrayList<>(secondaryData);

        this.primaryLabel = primaryLabel;
        this.secondaryLabel = secondaryLabel;

        onStateUpdated();

        logger.trace("Initialized state: {}", this);
    }

    public List<Integer> getPrimaryData()
    {
        return Collections.unmodifiableList(primaryData);
    }

    public List<Integer> getSecondaryData()
    {
        return Collections.unmodifiableList(secondaryData);
    }

    public void setPrimaryData(List<Integer> data)
    {
        primaryData.clear();
        primaryData.addAll(data);
    }

    public void setSecondaryData(List<Integer> data)
    {
        secondaryData.clear();
        secondaryData.addAll(data);
    }

    public String getPrimaryLabel()
    {
        return primaryLabel;
    }

    public void setPrimaryLabel(String primaryLabel)
    {
        this.primaryLabel = primaryLabel;
    }

    public String getSecondaryLabel()
    {
        return secondaryLabel;
    }

    public void setSecondaryLabel(String secondaryLabel)
    {
        this.secondaryLabel = secondaryLabel;
    }

    public String getShortLabel()
    {
        return shortLabel;
    }

    public String getFullDescription()
    {
        return fullDescription;
    }

    public Range<Integer> getSelectedRange()
    {
        return tryGetSelectedRange().orElse(null);
    }

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

    public boolean hasSelection()
    {
        return tryGetSelectedRange().isPresent();
    }

    public void setSelectionMin(int selectionMin)
    {
        if (this.selectionMin != selectionMin)
        {
            this.selectionMin = selectionMin;
            onStateUpdated();
        }
    }

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
