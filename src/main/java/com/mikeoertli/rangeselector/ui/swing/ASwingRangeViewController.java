package com.mikeoertli.rangeselector.ui.swing;

import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.ui.swing.listener.RangeSelectionMouseListener;
import com.mikeoertli.rangeselector.ui.swing.simple.SimpleRangeSelectionPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.lang.invoke.MethodHandles;
import java.util.Optional;
import java.util.UUID;

/**
 * The controller for a swing based range selection panel defined by {@link SimpleRangeSelectionPanel}
 *
 * @since 0.0.2
 */
public abstract class ASwingRangeViewController implements ISwingViewController
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final UUID uuid;
    protected IRangeSelectorView panel;
    protected final RangeSelectionMouseListener selectionListener;
    protected RangeConfiguration rangeConfiguration;
    protected final PanelSizeListener sizeChangeListener;

    protected ASwingRangeViewController()
    {
        this(new RangeConfiguration());
    }

    protected ASwingRangeViewController(RangeConfiguration rangeConfiguration)
    {
        uuid = UUID.randomUUID();
        this.rangeConfiguration = rangeConfiguration;
        selectionListener = new RangeSelectionMouseListener(this);
        sizeChangeListener = new PanelSizeListener();
    }

    protected abstract IRangeSelectorView createPanel();

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
            panel = createPanel();
            panel.addRangeSelectionListener(selectionListener);
            ((JPanel) panel).addComponentListener(sizeChangeListener);
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
            panel.reset();
            panel.removeRangeSelectionListener(selectionListener);
            ((JPanel) panel).removeComponentListener(sizeChangeListener);
        }

        rangeConfiguration.clearSelection();
    }

    @Override
    public void onRangeSelectionChanged(int selectionMin, int selectionMax)
    {
        rangeConfiguration.setSelectionMin(selectionMin);
        rangeConfiguration.setSelectionMax(selectionMax);
        panel.refreshView();
    }

    @Override
    public void selectAll()
    {
        onRangeSelectionChanged(getSelectableRangeMinimum(), getSelectableRangeMaximum());
    }

    @Override
    public int getSelectableRangeMinimum()
    {
        return rangeConfiguration.getRangeMin();
    }

    @Override
    public int getSelectableRangeMaximum()
    {
        return rangeConfiguration.getRangeMax();
    }

    protected void refreshPanel()
    {
        if (panel != null)
        {
            panel.refreshView();
        }
    }

    private class PanelSizeListener extends ComponentAdapter
    {

        @Override
        public void componentResized(ComponentEvent e)
        {
            final Dimension size = e.getComponent().getSize();
            rangeConfiguration.setRangeMax((int) size.getWidth());
        }
    }
}
