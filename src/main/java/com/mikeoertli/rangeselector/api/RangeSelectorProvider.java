package com.mikeoertli.rangeselector.api;

import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Provides instances of selectable range GUI controllers
 *
 * @since 0.0.1
 */
@Component
public class RangeSelectorProvider
{

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final List<IRangeSelectorController<? extends IRangeType<?>>> registeredControllers = new CopyOnWriteArrayList<>();

    public <N extends Number, RANGE extends IRangeType<N>> Optional<IRangeSelectorController<RANGE>> getRangeSelectionController(
            Class<? extends RANGE> rangeType, GuiFrameworkType guiFrameworkType)
    {
        final List<IRangeSelectorController<RANGE>> availableControllers = findCompatibleControllers(rangeType, guiFrameworkType);

        if (availableControllers.isEmpty())
        {
            logger.info("No registered controllers found for range data type: {} and GUI framework: {}.", rangeType, guiFrameworkType);
            return Optional.empty();
        } else if (availableControllers.size() > 1)
        {
            final String compatibleControllerTypes = availableControllers.stream()
                    .map(controller -> controller.getClass().getSimpleName())
                    .collect(Collectors.joining(",\n\t"));

            logger.warn("Multiple ({}) registered controllers exist and can satisfy the requested range type ({}) and " +
                            "GUI framework ({}), consider requesting the desired controller specifically. List of compatible" +
                            "controllers is as follows (will use the first): \n\t{}.", availableControllers.size(), rangeType,
                    guiFrameworkType, compatibleControllerTypes);
        } else
        {
            logger.debug("Requested controller for range type: {} and GUI framework: {}. There is exactly 1 controller that " +
                    "meet this request.", rangeType, guiFrameworkType);
        }

        return Optional.ofNullable(availableControllers.get(0));
    }

    /**
     * Find all compatible controllers that are registered and can support the given types
     *
     * @param rangeType        the type of data used in this range
     * @param guiFrameworkType the GUI framework
     * @param <N>              the type of numbers used for the {@link IRangeType}
     * @param <RANGE>          the {@link IRangeType} for the controller
     * @return the list of all controllers that support the given range data type and GUI framework
     */
    private <N extends Number, RANGE extends IRangeType<N>> List<IRangeSelectorController<RANGE>> findCompatibleControllers(
            Class<? extends RANGE> rangeType, GuiFrameworkType guiFrameworkType)
    {
        //noinspection unchecked
        return registeredControllers.stream()
                .filter(controller -> controller.isConfigurationSupported(rangeType, guiFrameworkType))
                .map(controller -> (IRangeSelectorController<RANGE>) controller)
                .collect(Collectors.toList());
    }

    public <N extends Number, RANGE extends IRangeType<N>> void registerRangeSelectorController(IRangeSelectorController<RANGE> controller)
    {
        registeredControllers.add(controller);

        logger.debug("Registered IRangeSelectionController. Type = {}, range data type = {}, GUI framework = {}. " +
                        "There are now {} registered controllers.", controller.getClass().getSimpleName(),
                controller.getRangeType().getName(), controller.getGuiType(), registeredControllers.size());
    }

    /**
     * Un-registers the given selector controller so it is no longer available
     *
     * @param controller the controller to be unregistered
     * @param <N>        the type of number supported by the range type for this controller
     * @param <RANGE>    the range type supported by this controller
     * @return a boolean to indicate whether the controller was removed (because it was registered) or not (because it was not registered)
     */
    public <N extends Number, RANGE extends IRangeType<N>> boolean unregisterSelectorController(IRangeSelectorController<RANGE> controller)
    {
        final boolean removed = registeredControllers.remove(controller);

        logger.debug("{} IRangeSelectionController. Type = {}, range data type = {}, GUI framework = {}. " +
                        "There are now {} registered controllers.", (removed ? "Removed" : "Tried (but failed) to remove"),
                controller.getClass().getSimpleName(), controller.getRangeType().getName(), controller.getGuiType(),
                registeredControllers.size());

        return removed;
    }
}
