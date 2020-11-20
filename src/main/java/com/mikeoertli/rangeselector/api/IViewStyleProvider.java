package com.mikeoertli.rangeselector.api;

import java.awt.Color;

/**
 * Style provider interface is used as the API for obtaining the color scheme associated with a view
 *
 * @since 0.0.2
 */
public interface IViewStyleProvider
{

    Color getPrimarySelectedColor();

    Color getPrimaryUnselectedColor();

    Color getSecondarySelectedColor();

    Color getSecondaryUnselectedColor();

    int getPixelGapBetweenBars();

    void setPrimarySelectedColor(Color primarySelectedColor);

    void setPrimaryUnselectedColor(Color primaryUnselectedColor);

    void setSecondarySelectedColor(Color secondarySelectedColor);

    void setSecondaryUnselectedColor(Color secondaryUnselectedColor);

    void setPixelGapBetweenBars(int numPixels);
}
