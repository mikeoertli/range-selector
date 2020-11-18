package com.mikeoertli.rangeselector.ui.swing.simple;

import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.data.rangetype.SimpleCount;
import com.mikeoertli.rangeselector.ui.swing.ISwingViewController;
import com.mikeoertli.rangeselector.ui.swing.listener.RangeSelectionMouseListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPanel;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.UUID;

/**
 * The controller for a swing based range selection panel defined by {@link SimpleRangeSelectionPanel}
 *
 * @since 0.0.1
 */
public class SimpleRangeSelectorPanelController implements ISwingViewController
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UUID uuid;
    private final SimpleRangeSelectionPanel panel;
    private final RangeSelectionMouseListener selectionListener;
    private RangeConfiguration rangeConfiguration;


    public SimpleRangeSelectorPanelController()
    {
        uuid = UUID.randomUUID();
        rangeConfiguration = new RangeConfiguration();
        panel = new SimpleRangeSelectionPanel(this);
        selectionListener = new RangeSelectionMouseListener(this);
        panel.addMouseListener(selectionListener);
        panel.addMouseMotionListener(selectionListener);
    }

    @Override
    public GuiFrameworkType getSupportedGuiFramework()
    {
        return GuiFrameworkType.SWING;
    }

    @Override
    public IRangeSelectorView getView()
    {
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
        panel.repaint();
    }

    @Override
    public JPanel getPanel()
    {
        return panel;
    }
}
