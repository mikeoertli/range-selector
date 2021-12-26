package com.mikeoertli.rangeselector.javafx.ui;

import com.mikeoertli.rangeselector.api.IRangeSelectionListener;
import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import com.mikeoertli.rangeselector.api.IViewStyleProvider;
import com.mikeoertli.rangeselector.core.RangeController;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.javafx.ui.common.RightClickMenuFxController;
import com.mikeoertli.rangeselector.javafx.ui.listener.RangeSelectionFxMouseAdapter;
import com.mikeoertli.rangeselector.javafx.ui.listener.StageSizeChangeListener;
import com.mikeoertli.rangeselector.ui.common.ViewStyleConfiguration;
import com.mikeoertli.rangeselector.ui.swing.ASwingRangeSelectionPanel;
import javafx.scene.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Base range view controller class for JavaFX GUIs
 *
 * @since 0.1.0
 */
public abstract class AJavaFxRangeViewController implements IJavaFxViewController
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    public static final int NO_SELECTION = -1;

    /**
     * The {@link UUID} of the panel/controller can be used to uniquely identify them.
     * This is auto-generated and can be retrieved via {@link #getUuid()}
     */
    private final UUID uuid;

    /**
     * The range selection view is the {@link javafx.scene.layout.Pane} which this controller manages
     */
    protected AFxRangeSelectionPane pane;

    /**
     * The handler of mouse input, responsible for extracting the selected range so that it can be properly rendered
     */
    protected final RangeSelectionFxMouseAdapter mouseInputAdapter;

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
    protected final StageSizeChangeListener sizeChangeListener;

    /**
     * Provider of the view style, i.e. colors to be used when creating the view
     */
    protected final IViewStyleProvider styleProvider;

    /**
     * Manages right click input for the panes
     */
    protected final RightClickMenuFxController rightClickController;

    /**
     * The listeners to be notified of any changes to the selection. This is most commonly the user of the API
     * which is requesting this range selection view in the first place so that it can handle whatever changes
     * it requires when the selection changes.
     */
    protected final List<IRangeSelectionListener> selectionListeners = new ArrayList<>();

    protected AJavaFxRangeViewController()
    {
        this(new RangeConfiguration(), null);
    }

    protected AJavaFxRangeViewController(RangeConfiguration rangeConfiguration, IRangeSelectionListener selectionListener)
    {
        uuid = UUID.randomUUID();
        this.rangeConfiguration = rangeConfiguration;
        rangeController = new RangeController(this);
        mouseInputAdapter = new RangeSelectionFxMouseAdapter(rangeController, this);
        sizeChangeListener = new StageSizeChangeListener(this);
        styleProvider = new ViewStyleConfiguration(this);
        rightClickController = new RightClickMenuFxController(this);
        if (selectionListener != null)
        {
            selectionListeners.add(selectionListener);
        }
    }

    /**
     * Create the scene containing the pane
     *
     * @param widthPixels  the width of the scene to display in pixels
     * @param heightPixels the height of the scene to display in pixels
     * @return a scene containing the content pane
     */
    public Scene createScene(double widthPixels, double heightPixels)
    {
        return new Scene(pane, widthPixels, heightPixels);
    }

    /**
     * Create the range selection top level pane. This will be placed inside a scene and displayed.
     *
     * @return the top level range selection pane for display
     * @see #createScene(double, double)
     */
    protected abstract AFxRangeSelectionPane createPane();

    @Override
    public GuiFrameworkType getSupportedGuiFramework()
    {
        return GuiFrameworkType.JAVA_FX;
    }

    @Override
    public IRangeSelectorView<?> getView()
    {
        if (pane == null)
        {
            pane = createPane();
            pane.addMouseInputHandler(mouseInputAdapter);

            pane.widthProperty().addListener(sizeChangeListener);
            pane.heightProperty().addListener(sizeChangeListener);

            pane.setOnContextMenuRequested(rightClickController.getContextMenuRequestedListener());
        }
        return pane;
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
        if (pane != null)
        {
            pane.reset();
            pane.removeMouseInputHandler(mouseInputAdapter);
            pane.widthProperty().removeListener(sizeChangeListener);
            pane.heightProperty().removeListener(sizeChangeListener);
        }

        rangeConfiguration.clearSelection();
    }

    @Override
    public void onRangeSelectionChanged(int selectionMin, int selectionMax)
    {
        rangeConfiguration.setSelectionMin(selectionMin);
        rangeConfiguration.setSelectionMax(selectionMax);
        onViewConfigurationChanged();
        if (selectionMin == NO_SELECTION && selectionMax == NO_SELECTION)
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
        rangeConfiguration.setRangeMax((int) getScene().getWidth());
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
        if (pane != null)
        {
            pane.refreshView();
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
            pane.lockPanel();
        } else
        {
            pane.unlockPanel();
        }
    }
}
