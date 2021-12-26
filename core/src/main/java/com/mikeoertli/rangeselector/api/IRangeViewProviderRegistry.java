package com.mikeoertli.rangeselector.api;

import com.mikeoertli.rangeselector.data.GuiFrameworkType;

import java.util.List;
import java.util.Optional;

/**
 * Defines the contract for a provider and/or registry of {@link IRangeViewControllerProvider}s.
 *
 * @since 0.0.1
 */
public interface IRangeViewProviderRegistry
{

    /**
     * Retrieve an {@link IRangeViewControllerProvider} based on the given range type ({@link IRangeType}) and GUI framework
     * type ({@link GuiFrameworkType}).
     *
     * @param rangeType        the type of data used in this range
     * @param guiFrameworkType the GUI framework
     * @return a matching range selection controller if one can be found, otherwise {@link Optional#empty()}
     */
    Optional<IRangeViewControllerProvider<? extends IRangeViewController>> getRangeViewControlProvider(IRangeType rangeType, GuiFrameworkType guiFrameworkType);

    /**
     * Find all compatible controllers that are registered and can support the given types
     *
     * @param rangeType        the type of data used in this range
     * @param guiFrameworkType the GUI framework
     * @return the list of all controllers that support the given range data type and GUI framework
     */
    List<IRangeViewControllerProvider<? extends IRangeViewController>> findCompatibleViewControlProviders(IRangeType rangeType, GuiFrameworkType guiFrameworkType);

    /**
     * Register an {@link IRangeViewControllerProvider} to be available when someone requests a range provider
     *
     * @param provider the provider to register
     */
    void registerRangeViewControlProvider(IRangeViewControllerProvider<? extends IRangeViewController> provider);

    /**
     * Un-registers the given provider so it is no longer available
     *
     * @param provider the provider to be unregistered
     * @return a boolean to indicate whether the controller was removed (because it was registered) or not (because it was not registered)
     */
    boolean unregisterRangeViewControlProvider(IRangeViewControllerProvider<? extends IRangeViewController> provider);
}
