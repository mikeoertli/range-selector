package com.mikeoertli.rangeselector.data;

import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
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

    public RangeConfiguration()
    {
        this(DEFAULT_RANGE_MINIMUM, DEFAULT_RANGE_MAXIMUM, null, null);
    }

    public RangeConfiguration(RangeConfiguration toClone)
    {
        this(toClone.getRangeMin(), toClone.getRangeMax(), toClone.getFullDescription(), toClone.getShortLabel());
        setSelectionMin(toClone.getSelectionMin());
        setSelectionMax(toClone.getSelectionMax());
        setLocked(toClone.isLocked());
        lastUpdateTime = toClone.getLastUpdateTime();
    }

    public RangeConfiguration(int rangeMin, int rangeMax, String fullDescription, String shortLabel)
    {
        this.rangeMin = rangeMin;
        this.rangeMax = rangeMax;

        this.fullDescription = fullDescription;
        this.shortLabel = shortLabel;

        onStateUpdated();

        logger.trace("Initialized state: {}", this);
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

    public int getRangeMin()
    {
        return rangeMin;
    }

    public int getRangeMax()
    {
        return rangeMax;
    }

    public void setRangeMin(int rangeMin)
    {
        this.rangeMin = rangeMin;
        onStateUpdated();
    }

    public void setRangeMax(int rangeMax)
    {
        this.rangeMax = rangeMax;
        onStateUpdated();
    }

    public int getSelectionMin()
    {
        return selectionMin;
    }

    public int getSelectionMax()
    {
        return selectionMax;
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked(boolean locked)
    {
        if (this.locked != locked)
        {
            this.locked = locked;
            onStateUpdated();
        }
    }

    public Instant getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public void update(RangeConfiguration updatedConfig)
    {
        if (updatedConfig.getRangeMax() != rangeMax || updatedConfig.getRangeMin() != rangeMin)
        {
            logger.warn("Cannot update the RangeConfiguration which has a range of {} to {} using another " +
                            "RangeConfiguration with range {} to {}.", rangeMin, rangeMax, updatedConfig.getRangeMin(),
                    updatedConfig.getRangeMax());
            return;
        }

        setLocked(updatedConfig.isLocked());

        if (updatedConfig.hasSelection())
        {
            setSelectionMin(updatedConfig.getSelectionMin());
            setSelectionMax(updatedConfig.getSelectionMax());
        } else
        {
            clearSelection();
        }
    }

    public void clearSelection()
    {
        setSelectionMin(NO_SELECTION);
        setSelectionMax(NO_SELECTION);
    }

    private void onStateUpdated()
    {
        lastUpdateTime = Instant.now();
    }

    public void setSelectedRange(Range<Integer> selectedRange)
    {
        setSelectionMin(selectedRange.getMinimum());
        setSelectionMax(selectedRange.getMaximum());
    }
}
