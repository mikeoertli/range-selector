package com.mikeoertli.rangeselector.api;

import com.mikeoertli.rangeselector.data.GuiFrameworkType;

/**
 * Common interface for all frequency range selector controllers
 *
 * @since 0.0.1
 */
public interface IRangeSelectorController<RANGE extends IRangeType<?>>
{
    /**
     * @return the type of range supported by this type of panel and controller
     */
    RANGE getRangeType();

    /**
     * @return the type of GUI framework supported by this controller
     */
    GuiFrameworkType getGuiType();

    /**
     * Creates a range selection panel of which this controller maintains control.
     * A controller will only manage a single panel.
     *
     * @return the panel that is owned/managed by this controller
     */
    IRangeSelectorPanel getPanel();

    /**
     * Query to indicate whether a particular type of ranges and type of GUI framework is supported by
     * this controller.
     *
     * @param rangeType the type of range data to be used
     * @param guiFrameworkType   the GUI framework to be used
     * @return a boolean to indicate whether this range selector controller can support this request by
     * creating a {@link IRangePanelController} and associated panel(s) for this type of data and GUI framework
     */
    boolean isConfigurationSupported(Class<? extends IRangeType<?>> rangeType, GuiFrameworkType guiFrameworkType);
}
