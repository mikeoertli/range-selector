package com.mikeoertli.rangeselector.core;

import com.mikeoertli.rangeselector.api.IRangeController;
import com.mikeoertli.rangeselector.api.IRangeControllerRegistry;
import com.mikeoertli.rangeselector.api.IRangeType;
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
public class RangeControllerRegistry implements IRangeControllerRegistry
{

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final List<IRangeController<? extends IRangeType<?, ?>>> registeredControllers = new CopyOnWriteArrayList<>();

    @Override
    public <RANGE extends IRangeType<? extends Number, ? extends Number>> Optional<IRangeController<RANGE>> getRangeSelectionController(
            Class<? extends RANGE> rangeType, GuiFrameworkType guiFrameworkType)
    {
        final List<IRangeController<RANGE>> availableControllers = findCompatibleControllers(rangeType, guiFrameworkType);

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

    @Override
    public <RANGE extends IRangeType<? extends Number, ? extends Number>> List<IRangeController<RANGE>> findCompatibleControllers(
            Class<? extends RANGE> rangeType, GuiFrameworkType guiFrameworkType)
    {
        //noinspection unchecked
        return registeredControllers.stream()
                .filter(controller -> controller.isConfigurationSupported(rangeType, guiFrameworkType))
                .map(controller -> (IRangeController<RANGE>) controller)
                .collect(Collectors.toList());
    }

    @Override
    public <RANGE extends IRangeType<? extends Number, ? extends Number>> void registerRangeSelectorController(IRangeController<RANGE> controller)
    {
        registeredControllers.add(controller);

        logger.debug("Registered IRangeSelectionController. Type = {}, range data type = {}. There are now {} registered " +
                "controllers.", controller.getClass().getSimpleName(), controller.getRangeType().getName(), registeredControllers.size());
    }

    @Override
    public <RANGE extends IRangeType<? extends Number, ? extends Number>> boolean unregisterSelectorController(IRangeController<RANGE> controller)
    {
        final boolean removed = registeredControllers.remove(controller);

        logger.debug("{} IRangeSelectionController. Type = {}, range data type = {}. There are now {} registered " +
                        "controllers.", (removed ? "Removed" : "Tried (but failed) to remove"),
                controller.getClass().getSimpleName(), controller.getRangeType().getName(), registeredControllers.size());

        return removed;
    }
}
