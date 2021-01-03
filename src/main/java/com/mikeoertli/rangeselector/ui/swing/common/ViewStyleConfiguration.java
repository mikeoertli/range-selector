package com.mikeoertli.rangeselector.ui.swing.common;

import com.mikeoertli.rangeselector.api.IViewStyleProvider;
import com.mikeoertli.rangeselector.ui.swing.ISwingViewController;

import java.awt.Color;

/**
 * Holds a simple configuration for a view's style
 *
 * @since 0.0.2
 */
public class ViewStyleConfiguration implements IViewStyleProvider
{

    private static final int ALPHA = 175; // 0-255
    private static final Color DEFAULT_SELECTED_PRIMARY_COLOR = new Color(Color.CYAN.getRed(), Color.CYAN.getGreen(), Color.CYAN.getBlue(), ALPHA);
    private static final Color DEFAULT_SELECTED_SECONDARY_COLOR = new Color(Color.WHITE.getRed(), Color.WHITE.getGreen(), Color.WHITE.getBlue(), ALPHA);
    private static final Color DEFAULT_UNSELECTED_PRIMARY_COLOR = new Color(Color.BLUE.getRed(), Color.BLUE.getGreen(), Color.BLUE.getBlue(), ALPHA);
    private static final Color DEFAULT_UNSELECTED_SECONDARY_COLOR = new Color(Color.GRAY.getRed(), Color.GRAY.getGreen(), Color.GRAY.getBlue(), ALPHA);

    private static final int DEFAULT_GAP_BETWEEN_BARS_IN_PIXELS = 2;
    private static final boolean DEFAULT_DARK_MODE_ENABLED = false;

    private Color primarySelectedColor = DEFAULT_SELECTED_PRIMARY_COLOR;
    private Color primaryUnselectedColor = DEFAULT_UNSELECTED_PRIMARY_COLOR;
    private Color secondarySelectedColor = DEFAULT_SELECTED_SECONDARY_COLOR;
    private Color secondaryUnselectedColor = DEFAULT_UNSELECTED_SECONDARY_COLOR;
    private int pixelGapBetweenBars = DEFAULT_GAP_BETWEEN_BARS_IN_PIXELS;

    /**
     * Boolean to indicate that views owned by this controller are to use dark mode and dark mode assets
     * Note: Very limited support at this time.
     */
    private boolean darkMode = DEFAULT_DARK_MODE_ENABLED;

    private final ISwingViewController controller;

    public ViewStyleConfiguration(ISwingViewController controller)
    {
        this.controller = controller;
    }

    @Override
    public Color getPrimarySelectedColor()
    {
        return primarySelectedColor;
    }

    @Override
    public Color getPrimaryUnselectedColor()
    {
        return primaryUnselectedColor;
    }

    @Override
    public Color getSecondarySelectedColor()
    {
        return secondarySelectedColor;
    }

    @Override
    public Color getSecondaryUnselectedColor()
    {
        return secondaryUnselectedColor;
    }

    @Override
    public int getPixelGapBetweenBars()
    {
        return pixelGapBetweenBars;
    }

    @Override
    public void setPrimarySelectedColor(Color primarySelectedColor)
    {
        if (!this.primarySelectedColor.equals(primarySelectedColor))
        {
            this.primarySelectedColor = primarySelectedColor;
            notifyViewController();
        }
    }

    @Override
    public void setPrimaryUnselectedColor(Color primaryUnselectedColor)
    {
        if (!this.primaryUnselectedColor.equals(primaryUnselectedColor))
        {
            this.primaryUnselectedColor = primaryUnselectedColor;
            notifyViewController();
        }
    }

    @Override
    public void setSecondarySelectedColor(Color secondarySelectedColor)
    {
        if (!this.secondarySelectedColor.equals(secondarySelectedColor))
        {
            this.secondarySelectedColor = secondarySelectedColor;
            notifyViewController();
        }
    }

    @Override
    public void setSecondaryUnselectedColor(Color secondaryUnselectedColor)
    {
        if (!this.secondaryUnselectedColor.equals(secondaryUnselectedColor))
        {
            this.secondaryUnselectedColor = secondaryUnselectedColor;
            notifyViewController();
        }
    }

    @Override
    public void setPixelGapBetweenBars(int numPixels)
    {
        if (pixelGapBetweenBars != numPixels)
        {
            pixelGapBetweenBars = numPixels;
            notifyViewController();
        }
    }

    @Override
    public void setDarkModeEnabled(boolean enabled)
    {
        if (darkMode != enabled)
        {
            darkMode = enabled;
            notifyViewController();
        }
    }

    @Override
    public boolean isDarkModeEnabled()
    {
        return darkMode;
    }

    @Override
    public void notifyViewController()
    {
        controller.onViewConfigurationChanged();
    }
}
