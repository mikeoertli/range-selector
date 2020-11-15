package com.mikeoertli.rangeselector.data;

import com.mikeoertli.rangeselector.api.IRangeType;
import org.apache.commons.lang3.Range;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Captures the state of a range selection panel
 *
 * @since 0.0.1
 */
public class RangeState<N extends Number, RANGE extends IRangeType<N>>
{
    private final UUID controllerUuid;
    private final RANGE rangeType;

    private Range<N> selectedRange;
    private Range<N> allowedRange;
    private boolean locked;

    private Instant lastUpdateTime;

    public RangeState(UUID controllerUuid, RANGE rangeType)
    {
        Objects.requireNonNull(controllerUuid, "Controller UUID (controllerUuid) cannot be null!");
        Objects.requireNonNull(rangeType, "Range Type (rangeType) cannot be null!");

        this.controllerUuid = controllerUuid;
        this.rangeType = rangeType;

        onStateUpdated();
    }

    public UUID getControllerUuid()
    {
        return controllerUuid;
    }

    public RANGE getRangeType()
    {
        return rangeType;
    }

    public Range<N> getSelectedRange()
    {
        return selectedRange;
    }

    public void setSelectedRange(Range<N> selectedRange)
    {
        this.selectedRange = selectedRange;
        onStateUpdated();
    }

    public Range<N> getAllowedRange()
    {
        return allowedRange;
    }

    public void setAllowedRange(Range<N> allowedRange)
    {
        this.allowedRange = allowedRange;
        onStateUpdated();
    }

    public boolean isLocked()
    {
        return locked;
    }

    public void setLocked(boolean locked)
    {
        this.locked = locked;
        onStateUpdated();
    }

    public Instant getLastUpdateTime()
    {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Instant lastUpdateTime)
    {
        this.lastUpdateTime = lastUpdateTime;
        onStateUpdated();
    }

    private void onStateUpdated()
    {
        lastUpdateTime = Instant.now();
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof RangeState)) return false;

        RangeState<?, ?> that = (RangeState<?, ?>) o;

        if (locked != that.locked) return false;
        if (!controllerUuid.equals(that.controllerUuid)) return false;
        if (!rangeType.equals(that.rangeType)) return false;
        if (selectedRange != null ? !selectedRange.equals(that.selectedRange) : that.selectedRange != null)
            return false;
        if (!allowedRange.equals(that.allowedRange)) return false;
        return lastUpdateTime.equals(that.lastUpdateTime);
    }

    @Override
    public int hashCode()
    {
        int result = controllerUuid.hashCode();
        result = 31 * result + rangeType.hashCode();
        result = 31 * result + (selectedRange != null ? selectedRange.hashCode() : 0);
        result = 31 * result + allowedRange.hashCode();
        result = 31 * result + (locked ? 1 : 0);
        result = 31 * result + lastUpdateTime.hashCode();
        return result;
    }
}
