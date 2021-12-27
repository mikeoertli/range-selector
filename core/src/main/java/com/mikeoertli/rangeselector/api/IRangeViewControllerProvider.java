package com.mikeoertli.rangeselector.api;

import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;

/**
 * Common interface for all range selector controller provider. This is effectively a factory interface
 * for a view controller factory.
 *
 * @since 0.0.1
 */
public interface IRangeViewControllerProvider<CONTROL extends IRangeViewController>
{
    /**
     * Creates a range selection view controller which owns and manages a panel/view of the given GUI framework type.
     *
     * @param rangeConfiguration the state/configuration of the range view
     * @param selectionListener  the listener to be notified when a range is selected or removed
     * @return the panel controller for this range type and GUI framework, if supported
     */
    CONTROL createViewController(RangeConfiguration rangeConfiguration, IRangeSelectionListener selectionListener);

    /**
     * Query to indicate whether a particular type of ranges and type of GUI framework is supported by
     * this controller.
     *
     * @param rangeType        the type of range data to be used
     * @param guiFrameworkType the GUI framework to be used
     * @return a boolean to indicate whether this range selector controller can support this request by
     * creating a {@link IRangeViewController} and associated panel(s) for this type of data and GUI framework
     */
    boolean isConfigurationSupported(IRangeType rangeType, GuiFrameworkType guiFrameworkType);

    /**
     * @return a description of the type of view provided
     */
    String getDescription();
}
