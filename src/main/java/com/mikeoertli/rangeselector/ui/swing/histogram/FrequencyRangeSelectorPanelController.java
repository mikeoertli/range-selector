package com.mikeoertli.rangeselector.ui.swing.histogram;

import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.ui.swing.ISwingViewController;
import com.mikeoertli.rangeselector.ui.swing.listener.RangeSelectionMouseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.UUID;

/**
 * The controller for a swing based range selection panel
 *
 * @since 0.0.1
 */
public class FrequencyRangeSelectorPanelController implements ISwingViewController
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UUID uuid;
    private RangeConfiguration rangeConfiguration;
    private HistogramSelectionPanel panel;
    private final RangeSelectionMouseListener selectionListener;

    public FrequencyRangeSelectorPanelController(RangeConfiguration rangeConfiguration)
    {
        uuid = UUID.randomUUID();
        this.rangeConfiguration = rangeConfiguration;
        selectionListener = new RangeSelectionMouseListener(this);
    }

    @Override
    public GuiFrameworkType getSupportedGuiFramework()
    {
        return GuiFrameworkType.SWING;
    }

    @Override
    public IRangeSelectorView getView()
    {
        if (panel == null)
        {
            panel = new HistogramSelectionPanel(this);
        }
        return panel;
    }

    @Override
    public RangeConfiguration getRangeConfiguration()
    {
        return rangeConfiguration;
    }

    @Override
    public Optional<RangeConfiguration> restoreState(RangeConfiguration updatedConfiguration)
    {
        final RangeConfiguration cloneConfig = new RangeConfiguration(rangeConfiguration);
        rangeConfiguration = updatedConfiguration;

        selectionListener.setSelectedRange(rangeConfiguration.getSelectionMin(), rangeConfiguration.getSelectionMax());
        return Optional.of(cloneConfig);
    }

    @Override
    public UUID getUuid()
    {
        return uuid;
    }

    @Override
    public void shutdown()
    {
        if (panel != null)
        {
            panel.removeMouseListener(selectionListener);
            panel.removeMouseMotionListener(selectionListener);
        }
        // TODO
    }

    @Override
    public void onRangeSelectionChanged(int selectionMin, int selectionMax)
    {
        rangeConfiguration.setSelectionMin(selectionMin);
        rangeConfiguration.setSelectionMax(selectionMax);
    }
}
