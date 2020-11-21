package com.mikeoertli.rangeselector.api;

import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;

import java.util.Optional;
import java.util.UUID;

/**
 * The interface to which all range view/panel controllers must adhere. A view controller will control ONE
 * and only ONE {@link IRangeSelectorView}.
 *
 * @since 0.0.1
 */
public interface IRangeViewController
{

    /**
     * @return the type of GUI framework supported by the view managed by this controller
     */
    GuiFrameworkType getSupportedGuiFramework();

    /**
     * Retrieves a range selection view/panel over which this controller maintains control.
     * <p>
     * A controller will only manage a single view/panel.
     * <p>
     * If the view/panel does not exist, it is created. This will never return null.
     *
     * @return the view that is owned/managed by this controller
     */
    IRangeSelectorView getView();

    /**
     * @return The current range state of the view/panel
     */
    RangeConfiguration getRangeConfiguration();

    /**
     * Restores a given {@link RangeConfiguration} to the given view/panel
     *
     * @param state the state to restore
     * @return the state that was overwritten, if any, otherwise {@link Optional#empty()}
     */
    Optional<RangeConfiguration> restoreState(RangeConfiguration state);

    /**
     * @return the UUID, which MUST be unique to this controller/view combo
     */
    UUID getUuid();

    /**
     * Shutdown and cleanup, this is a destructive operation and cannot be undone
     */
    void shutdown();

    /**
     * The range selection has changed.
     *
     * @param selectionMin the minimum of the selected range
     * @param selectionMax the maximum of the selected range
     */
    void onRangeSelectionChanged(int selectionMin, int selectionMax);

    /**
     * Select the full range
     */
    void selectAll();

    /**
     * Adds a range selection listener to be notified of changes to the state of the selected region of the view.
     *
     * @param listener the listener to be notified of range selection changes
     */
    void addRangeSelectionListener(IRangeSelectionListener listener);

    /**
     * Removes a range selection listener to be notified of changes to the state of the selected region of the view.
     *
     * @param listener the listener to be removed from teh list of those to whom notification is sent when range selection changes
     */
    void removeRangeSelectionListener(IRangeSelectionListener listener);

    /**
     * Handle a resize event, generally triggered by a component/size listener on the parent view container
     */
    void onViewSizeChanged();

    /**
     * @return the absolute minimum width that can be supported by the view
     */
    int getMinimumViewWidth();
}
