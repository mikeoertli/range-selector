package com.mikeoertli.rangeselector.core;

import com.mikeoertli.rangeselector.api.IRangeType;
import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.api.IRangeViewControllerProvider;
import com.mikeoertli.rangeselector.api.IRangeViewProviderRegistry;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RangeViewProviderRegistry implements IRangeViewProviderRegistry
{

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final List<IRangeViewControllerProvider<? extends IRangeViewController>> registeredControllers;

    @Autowired
    public RangeViewProviderRegistry(List<IRangeViewControllerProvider<? extends IRangeViewController>> registeredControllers)
    {
        this.registeredControllers = new CopyOnWriteArrayList<>(registeredControllers);
    }

    @Override
    public Optional<IRangeViewControllerProvider<? extends IRangeViewController>> getRangeViewControlProvider(IRangeType rangeType, GuiFrameworkType guiFrameworkType)
    {
        final List<IRangeViewControllerProvider<? extends IRangeViewController>> availableControllers = findCompatibleViewControlProviders(rangeType, guiFrameworkType);

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
    public List<IRangeViewControllerProvider<? extends IRangeViewController>> findCompatibleViewControlProviders(IRangeType rangeType, GuiFrameworkType guiFrameworkType)
    {
        return registeredControllers.stream()
                .filter(controller -> controller.isConfigurationSupported(rangeType, guiFrameworkType))
                .collect(Collectors.toList());
    }

    @Override
    public void registerRangeViewControlProvider(IRangeViewControllerProvider<? extends IRangeViewController> provider)
    {
        registeredControllers.add(provider);

        logger.debug("Registered IRangeSelectionController. Type = {}, range data type = {}. There are now {} registered " +
                "controllers.", provider.getClass().getSimpleName(), provider.getDescription(), registeredControllers.size());
    }

    @Override
    public boolean unregisterRangeViewControlProvider(IRangeViewControllerProvider<? extends IRangeViewController> provider)
    {
        final boolean removed = registeredControllers.remove(provider);

        logger.debug("{} IRangeSelectionController. Type = {}, range data type = {}. There are now {} registered " +
                        "controllers.", (removed ? "Removed" : "Tried (but failed) to remove"),
                provider.getClass().getSimpleName(), provider.getDescription(), registeredControllers.size());

        return removed;
    }
}
