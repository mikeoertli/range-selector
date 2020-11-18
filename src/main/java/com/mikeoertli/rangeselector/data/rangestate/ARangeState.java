package com.mikeoertli.rangeselector.data.rangestate;

import com.mikeoertli.rangeselector.api.IRangeState;
import com.mikeoertli.rangeselector.api.IRangeType;
import org.apache.commons.lang3.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

/**
 * Captures the state of a range selection panel
 *
 * @since 0.0.1
 */
public class ARangeState implements IRangeState
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected final IRangeType rangeType;
    protected final String fullDescription;
    protected final String shortLabel;
    protected Range<Double> selectedRange;
    protected final Range<Double> allowedRange;
    protected boolean locked;

    protected Instant lastUpdateTime;

    public ARangeState(IRangeType rangeType, Range<Double> allowedRange, String fullDescription, String shortLabel)
    {
        Objects.requireNonNull(allowedRange, "Absolute allowable range (absoluteAllowedRange) cannot be null!");
        Objects.requireNonNull(rangeType, "Range Type (rangeType) cannot be null!");

        this.allowedRange = allowedRange;
        this.rangeType = rangeType;
        this.fullDescription = StringUtils.hasText(fullDescription) ? fullDescription : rangeType.getLabel();
        this.shortLabel = StringUtils.hasText(shortLabel) ? shortLabel : rangeType.getLabel();

        onStateUpdated();

        logger.trace("Initialized state: {}", this);
    }

    @Override
    public String getShortLabel()
    {
        return shortLabel;
    }

    @Override
    public String getFullDescription()
    {
        return fullDescription;
    }

    @Override
    public IRangeType getRangeType()
    {
        return rangeType;
    }

    @Override
    public Optional<Range<Double>> getSelectedRange()
    {
        return Optional.ofNullable(selectedRange);
    }

    @Override
    public void setSelectedRange(Range<Double> selectedRange)
    {
        if (!this.selectedRange.equals(selectedRange))
        {
            this.selectedRange = selectedRange;
            onStateUpdated();
        }
    }

    @Override
    public Range<Double> getRangeExtrema()
    {
        return allowedRange;
    }

    @Override
    public boolean isLocked()
    {
        return locked;
    }

    @Override
    public void setLocked(boolean locked)
    {
        if (this.locked != locked)
        {
            this.locked = locked;
            onStateUpdated();
        }
    }

    @Override
    public Instant getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    @Override
    public void update(IRangeState updateState)
    {
        setLocked(updateState.isLocked());
        if (updateState.getSelectedRange().isPresent())
        {
            setSelectedRange(updateState.getSelectedRange().get());
        } else
        {
            clearSelection();
        }
    }

    protected void clearSelection()
    {
        setSelectedRange(null);
    }

    protected void onStateUpdated()
    {
        lastUpdateTime = Instant.now();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ARangeState)) return false;

        ARangeState that = (ARangeState) o;

        if (locked != that.locked) return false;
        if (!rangeType.equals(that.rangeType)) return false;
        if (!fullDescription.equals(that.fullDescription)) return false;
        if (selectedRange != null ? !selectedRange.equals(that.selectedRange) : that.selectedRange != null)
        {
            return false;
        }
        if (!allowedRange.equals(that.allowedRange)) return false;
        return lastUpdateTime.equals(that.lastUpdateTime);
    }

    @Override
    public int hashCode()
    {
        int result = rangeType.hashCode();
        result = 31 * result + fullDescription.hashCode();
        result = 31 * result + (selectedRange != null ? selectedRange.hashCode() : 0);
        result = 31 * result + allowedRange.hashCode();
        result = 31 * result + (locked ? 1 : 0);
        result = 31 * result + lastUpdateTime.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return getClass().getSimpleName() + "{" +
                "rangeType=" + rangeType +
                ", selectedRange=" + selectedRange +
                ", absoluteAllowedRange=" + allowedRange +
                ", locked=" + locked +
                ", fullDescription=" + fullDescription +
                ", shortLabel=" + shortLabel +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
