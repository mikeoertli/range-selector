package com.mikeoertli.rangeselector.api;

import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.rangestate.ARangeState;

import java.util.Optional;
import java.util.UUID;

/**
 * The interface to which all range view/panel controllers must adhere. A view controller will control ONE
 * and only ONE {@link IRangeSelectorView}.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 * @since 0.0.1
 */
public interface IRangeViewController<RANGE extends IRangeType<? extends Number, ? extends Number>>
{
    /**
     * @return the type of range supported by this type of view and controller
     */
    RANGE getRangeType();

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
    ARangeState<RANGE> getRangeState();

    /**
     * Restores a given {@link ARangeState} to the given view/panel
     *
     * @param state the state to restore
     * @return the state that was overwritten, if any, otherwise {@link Optional#empty()}
     */
    Optional<ARangeState<RANGE>> restoreState(ARangeState<RANGE> state);

    /**
     * @return the UUID, which MUST be unique to this controller/view combo
     */
    UUID getUuid();

    /**
     * Shutdown and cleanup, this is a destructive operation and cannot be undone
     */
    void shutdown();
}
