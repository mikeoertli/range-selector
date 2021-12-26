package com.mikeoertli.rangeselector.api;

import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.RangeConfiguration;

import javax.naming.OperationNotSupportedException;

/**
 * Common interface for all frequency range selector controllers
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
     * Creates a range selection view controller which owns and manages a panel/view of the given GUI framework type.
     *
     * @param rangeConfiguration the state/configuration of the range view
     * @param selectionListener  the listener to be notified when a range is selected or removed
     * @return the panel controller for this range type and GUI framework, if supported
     * @throws OperationNotSupportedException if the controller doesn't support JavaFX (which none do, as of now)
     */
    default CONTROL createJavaFxViewController(RangeConfiguration rangeConfiguration, IRangeSelectionListener selectionListener) throws OperationNotSupportedException
    {
        throw new OperationNotSupportedException("JavaFX is not supported");
    }

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
