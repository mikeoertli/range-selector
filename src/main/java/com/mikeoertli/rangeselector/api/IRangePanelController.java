package com.mikeoertli.rangeselector.api;

import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeState;

import java.util.Optional;
import java.util.UUID;

/**
 * The interface to which all range panel controllers must adhere. A panel controller will control ONE
 * and only ONE {@link IRangeSelectorPanel}.
 *
 * @author Proprietary information subject to the terms of a Non-Disclosure Agreement
 * @since 0.0.1
 */
public interface IRangePanelController<N extends Number, RANGE extends IRangeType<N>>
{
    /**
     * @return the type of range supported by this type of panel and controller
     */
    RANGE getRangeType();

    /**
     * @return the type of GUI framework supported by the panel managed by this controller
     */
    GuiFrameworkType getGuiType();

    /**
     * Retrieves a range selection panel of which this controller maintains control.
     * <p>
     * A controller will only manage a single panel.
     * <p>
     * If the panel does not exist, it is created. This will never return null.
     *
     * @return the panel that is owned/managed by this controller
     */
    IRangeSelectorPanel getPanel();

    /**
     * @return The current range state of the panel
     */
    RangeState<N, RANGE> getRangeState();

    /**
     * Restores a given {@link RangeState} to the given panel
     *
     * @param state the state to restore
     * @return the state that was overwritten, if any, otherwise {@link Optional#empty()}
     */
    Optional<RangeState<N, RANGE>> restoreState(RangeState<N, RANGE> state);

    /**
     * @return the UUID, which MUST be unique to this controller/panel combo
     */
    UUID getUuid();

    /**
     * Shutdown and cleanup, this is a destructive operation and cannot be undone
     */
    void shutdown();
}
