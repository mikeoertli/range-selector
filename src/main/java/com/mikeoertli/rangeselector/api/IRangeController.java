package com.mikeoertli.rangeselector.api;

import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.rangestate.ARangeState;

import javax.naming.OperationNotSupportedException;

/**
 * Common interface for all frequency range selector controllers
 *
 * @since 0.0.1
 */
public interface IRangeController<RANGE extends IRangeType<? extends Number, ? extends Number>>
{
    /**
     * @return the type of range supported by this type of panel and controller
     */
    Class<RANGE> getRangeType();

    /**
     * Creates a range selection view controller which owns and manages a panel/view of the given GUI framework type.
     *
     * @param guiFramework the GUI framework to use for the view
     * @param rangeState   the state/configuration of the range view
     * @return the panel controller for this range type and GUI framework, if supported
     * @throws OperationNotSupportedException if the given GUI framework type is not supported by the available panel controllers
     */
    IRangeViewController<RANGE> createViewController(GuiFrameworkType guiFramework, ARangeState<RANGE> rangeState) throws OperationNotSupportedException;

    /**
     * Query to indicate whether a particular type of ranges and type of GUI framework is supported by
     * this controller.
     *
     * @param rangeType        the type of range data to be used
     * @param guiFrameworkType the GUI framework to be used
     * @return a boolean to indicate whether this range selector controller can support this request by
     * creating a {@link IRangeViewController} and associated panel(s) for this type of data and GUI framework
     */
    boolean isConfigurationSupported(Class<? extends IRangeType<?, ?>> rangeType, GuiFrameworkType guiFrameworkType);
}
