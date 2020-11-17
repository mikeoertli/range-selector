package com.mikeoertli.rangeselector.ui.swing;

import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.rangestate.ARangeState;
import com.mikeoertli.rangeselector.data.rangestate.FrequencyRangeState;
import com.mikeoertli.rangeselector.data.rangetype.FrequencyUnits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.UUID;

/**
 * The controller for a swing based range selection panel defined by {@link RangeSelectorPanel}
 *
 * @since 0.0.1
 */
public class FrequencyRangeSelectorPanelController implements IRangeViewController<FrequencyUnits>
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UUID uuid;
    private final FrequencyUnits rangeUnits;
    private FrequencyRangeState rangeState;

    public FrequencyRangeSelectorPanelController(FrequencyUnits rangeUnits)
    {
        uuid = UUID.randomUUID();
        this.rangeUnits = rangeUnits;
    }

    @Override
    public FrequencyUnits getRangeType()
    {
        return rangeUnits;
    }

    @Override
    public GuiFrameworkType getSupportedGuiFramework()
    {
        return GuiFrameworkType.SWING;
    }

    @Override
    public IRangeSelectorView getView()
    {
        return null;
    }

    @Override
    public ARangeState<FrequencyUnits> getRangeState()
    {
        return rangeState;
    }

    @Override
    public Optional<ARangeState<FrequencyUnits>> restoreState(ARangeState<FrequencyUnits> state)
    {
        // TODO
        return Optional.empty();
    }

    @Override
    public UUID getUuid()
    {
        return uuid;
    }

    @Override
    public void shutdown()
    {
        // TODO
    }
}
