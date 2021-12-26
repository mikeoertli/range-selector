package com.mikeoertli.rangeselector.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.util.Optional;

/**
 * Utility functions related to resources
 *
 * @since 0.0.2
 */
public final class ResourceUtilities
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ResourceUtilities()
    {
        // prevent instantiation
    }

    public static Optional<ImageIcon> getResourceAsIcon(String resourcePath)
    {
        try
        {
            final URL resource = ResourceUtilities.class.getClassLoader().getResource(resourcePath);
            if (resource == null)
            {
                logger.error("Unable to locate the given resource: {}", resourcePath);
            } else
            {
                return Optional.of(new ImageIcon(resource));
            }
        } catch (Exception e)
        {
            logger.error("Unable to locate the given resource: {}", resourcePath, e);
        }
        return Optional.empty();
    }

    public static Optional<Image> getResourceAsImage(String resourcePath)
    {
        try (final InputStream resourceInputStream = ResourceUtilities.class.getClassLoader().getResourceAsStream(resourcePath);)
        {
            if (resourceInputStream == null)
            {
                logger.error("Unable to locate the given resource: {}", resourcePath);
            } else
            {
                return Optional.of(ImageIO.read(resourceInputStream));
            }
        } catch (Exception e)
        {
            logger.error("Unable to locate the given resource: {}", resourcePath, e);
        }
        return Optional.empty();
    }
}
