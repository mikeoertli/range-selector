package com.mikeoertli.rangeselector.api;

import com.mikeoertli.rangeselector.data.GuiFrameworkType;

import java.util.List;
import java.util.Optional;

/**
 * Defines the contract for a provider and/or registry of {@link IRangeController}s.
 *
 * @since 0.0.1
 */
public interface IRangeControllerRegistry
{

    /**
     * Retrieve an {@link IRangeController} based on the given range type ({@link IRangeType}) and GUI framework
     * type ({@link GuiFrameworkType}).
     *
     * @param rangeType        the type of data used in this range
     * @param guiFrameworkType the GUI framework
     * @param <RANGE>          the {@link IRangeType} for the controller
     * @return a matching range selection controller if one can be found, otherwise {@link Optional#empty()}
     */
    <RANGE extends IRangeType<? extends Number, ? extends Number>> Optional<IRangeController<RANGE>> getRangeSelectionController(
            Class<? extends RANGE> rangeType, GuiFrameworkType guiFrameworkType);

    /**
     * Find all compatible controllers that are registered and can support the given types
     *
     * @param rangeType        the type of data used in this range
     * @param guiFrameworkType the GUI framework
     * @param <RANGE>          the {@link IRangeType} for the controller
     * @return the list of all controllers that support the given range data type and GUI framework
     */
    <RANGE extends IRangeType<? extends Number, ? extends Number>> List<IRangeController<RANGE>> findCompatibleControllers(
            Class<? extends RANGE> rangeType, GuiFrameworkType guiFrameworkType);

    /**
     * Register an {@link IRangeController} to be available when someone requests a range controller
     *
     * @param controller the controller to register
     * @param <RANGE>    the {@link IRangeType} for the controller
     */
    <RANGE extends IRangeType<? extends Number, ? extends Number>> void registerRangeSelectorController(IRangeController<RANGE> controller);

    /**
     * Un-registers the given selector controller so it is no longer available
     *
     * @param controller the controller to be unregistered
     * @param <RANGE>    the {@link IRangeType} for the controller
     * @return a boolean to indicate whether the controller was removed (because it was registered) or not (because it was not registered)
     */
    <RANGE extends IRangeType<? extends Number, ? extends Number>> boolean unregisterSelectorController(IRangeController<RANGE> controller);
}
