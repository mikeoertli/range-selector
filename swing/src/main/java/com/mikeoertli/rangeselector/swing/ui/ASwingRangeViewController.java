package com.mikeoertli.rangeselector.swing.ui;

import com.mikeoertli.rangeselector.api.IRangeSelectionListener;
import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import com.mikeoertli.rangeselector.api.IViewStyleProvider;
import com.mikeoertli.rangeselector.core.RangeController;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.swing.ui.listener.PanelSizeChangeListener;
import com.mikeoertli.rangeselector.swing.ui.listener.RangeSelectionSwingMouseAdapter;
import com.mikeoertli.rangeselector.swing.ui.simple.SimpleRangeSelectionPanel;
import com.mikeoertli.rangeselector.ui.ViewStyleConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * The {@link UUID} of the panel/controller can be used to uniquely identify them.
     * This is auto-generated and can be retrieved via {@link #getUuid()}
     */
    private final UUID uuid;

    /**
     * The range selection view is the {@link java.awt.Panel} which this controller manages
     */
    protected ASwingRangeSelectionPanel panel;

    /**
     * The handler of mouse input, responsible for extracting the selected range so that it can be properly rendered
     */
    protected final RangeSelectionSwingMouseAdapter mouseInputAdapter;

    /**
     * The controller for the selected range, whether selection is armed, and whether it is locked
     */
    protected final RangeController rangeController;

    /**
     * The range configuration that currently represents the state of the {@link ASwingRangeSelectionPanel}.
     * This can be provided when constructed or restored via {@link #restoreState(RangeConfiguration)}
     */
    protected RangeConfiguration rangeConfiguration;

    /**
     * Listens to changes in the size of the view which can then notify this controller that ranges need to be
     * recalculated and the view re-drawn
     */
    protected final PanelSizeChangeListener sizeChangeListener;

    /**
     * Provider of the view style, i.e. colors to be used when creating the view
     */
    protected final IViewStyleProvider styleProvider;

    /**
     * The listeners to be notified of any changes to the selection. This is most commonly the user of the API
     * which is requesting this range selection view in the first place so that it can handle whatever changes
     * it requires when the selection changes.
     */
    protected final List<IRangeSelectionListener> selectionListeners = new ArrayList<>();

    protected ASwingRangeViewController()
    {
        this(new RangeConfiguration(), null);
    }

    protected ASwingRangeViewController(RangeConfiguration rangeConfiguration, IRangeSelectionListener selectionListener)
    {
        uuid = UUID.randomUUID();
        this.rangeConfiguration = rangeConfiguration;
        rangeController = new RangeController(this);
        mouseInputAdapter = new RangeSelectionSwingMouseAdapter(rangeController, this);
        sizeChangeListener = new PanelSizeChangeListener(this);
        styleProvider = new ViewStyleConfiguration(this);
        if (selectionListener != null)
        {
            selectionListeners.add(selectionListener);
        }
    }

    /**
     * Required for implementations to create their own panel since there will be specific initialization
     * necessary in many cases. This also moves the panel construction out of the constructor for a potential
     * performance improvement.
     * <p>
     * It is imperative tha the implementing class performs this, and all Swing rendering functions, on the
     * AWT Event Dispatch Thread (EDT) as is common practice in Swing.
     * <p>
     * Reference: https://docs.oracle.com/javase/tutorial/uiswing/concurrency/dispatch.html
     * Reference: http://www.fredosaurus.com/JavaBasics/gui/gui-commentary/guicom-main-thread.html
     *
     * @return the constructed panel ready for display
     */
    protected abstract ASwingRangeSelectionPanel createPanel();

    @Override
    public GuiFrameworkType getSupportedGuiFramework()
    {
        return GuiFrameworkType.SWING;
    }

    @Override
    public IRangeSelectorView<?> getView()
    {
        if (panel == null)
        {
            panel = createPanel();
            panel.addMouseInputHandler(mouseInputAdapter);
            panel.addComponentListener(sizeChangeListener);
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
        final RangeConfiguration oldConfig = new RangeConfiguration(rangeConfiguration);
        rangeConfiguration = updatedConfiguration;

        rangeController.setSelectedRange(rangeConfiguration.getSelectionMin(), rangeConfiguration.getSelectionMax());

        onViewConfigurationChanged();

        return Optional.of(oldConfig);
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
            panel.removeMouseInputHandler(mouseInputAdapter);
            panel.removeComponentListener(sizeChangeListener);
        }

        rangeConfiguration.clearSelection();
    }

    @Override
    public void onRangeSelectionChanged(int selectionMin, int selectionMax)
    {
        rangeConfiguration.setSelectionMin(selectionMin);
        rangeConfiguration.setSelectionMax(selectionMax);
        onViewConfigurationChanged();
        if (selectionMin == -1 && selectionMax == -1)
        {
            selectionListeners.forEach(IRangeSelectionListener::onRangeSelectionCleared);
        } else
        {
            selectionListeners.forEach(listener ->
                    listener.onRangeSelected(rangeConfiguration.getRangeMin(), rangeConfiguration.getRangeMax(), selectionMin, selectionMax));
        }
    }

    @Override
    public void addRangeSelectionListener(IRangeSelectionListener listener)
    {
        if (listener != null)
        {
            selectionListeners.add(listener);
        }
    }

    @Override
    public void removeRangeSelectionListener(IRangeSelectionListener listener)
    {
        if (listener != null)
        {
            selectionListeners.remove(listener);
        }
    }

    @Override
    public void onViewSizeChanged()
    {
        rangeConfiguration.setRangeMax(getPanel().getWidth());
        onViewConfigurationChanged();
    }

    @Override
    public IViewStyleProvider getViewStyleProvider()
    {
        return styleProvider;
    }

    @Override
    public void onViewConfigurationChanged()
    {
        if (panel != null)
        {
            panel.refreshView();
        }
    }

    @Override
    public void toggleLock()
    {
        final boolean newLockedState = !rangeConfiguration.isLocked();
        logger.trace("Toggling the locked state from {} to {}", newLockedState ? "unlocked" : "locked", newLockedState ? "locked" : "unlocked");
        rangeConfiguration.setLocked(newLockedState);
        rangeController.setLocked(newLockedState);
        if (newLockedState)
        {
            panel.lockPanel();
        } else
        {
            panel.unlockPanel();
        }
    }
}
