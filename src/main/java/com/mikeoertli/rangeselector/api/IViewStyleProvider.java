package com.mikeoertli.rangeselector.api;

import java.awt.Color;

/**
 * Style provider interface is used as the API for obtaining the color scheme associated with a view
 *
 * @since 0.0.2
 */
public interface IViewStyleProvider
{

    /**
     * Defines the primary dataset's color when it is in the selected range.
     *
     * @return the color to use for the primary dataset when rendering something within the selected range
     */
    Color getPrimarySelectedColor();

    /**
     * Defines the primary dataset's color when it is NOT in the selected range.
     *
     * @return the color to use for the primary dataset when rendering something outside the selected range
     */
    Color getPrimaryUnselectedColor();

    /**
     * Defines the secondary dataset's color when it is in the selected range.
     *
     * @return the color to use for the secondary dataset when rendering something within the selected range
     */
    Color getSecondarySelectedColor();

    /**
     * Defines the secondary dataset's color when it is NOT in the selected range.
     *
     * @return the color to use for the secondary dataset when rendering something outside the selected range
     */
    Color getSecondaryUnselectedColor();

    /**
     * The number of pixels to put between each of the data "bars" in histogram style views. Note that not all
     * views support this.
     *
     * @return the number of pixels to place between each bar in histogram style views
     */
    int getPixelGapBetweenBars();

    /**
     * Updates the primary dataset's color when it is in the selected range.
     *
     * @param primarySelectedColor the color to use for the primary dataset when rendering something within the selected range
     */
    void setPrimarySelectedColor(Color primarySelectedColor);

    /**
     * Updates the primary dataset's color when it is NOT in the selected range.
     *
     * @param primaryUnselectedColor the color to use for the primary dataset when rendering something outside the selected range
     */
    void setPrimaryUnselectedColor(Color primaryUnselectedColor);

    /**
     * Updates the secondary dataset's color when it is in the selected range.
     *
     * @param secondarySelectedColor the color to use for the secondary dataset when rendering something within the selected range
     */
    void setSecondarySelectedColor(Color secondarySelectedColor);

    /**
     * Updates the secondary dataset's color when it is NOT in the selected range.
     *
     * @param secondaryUnselectedColor the color to use for the secondary dataset when rendering something outside the selected range
     */
    void setSecondaryUnselectedColor(Color secondaryUnselectedColor);

    void setPixelGapBetweenBars(int numPixels);

    /**
     * Method that should be triggered any time the view configuration changes (ex: a setter above is called)
     */
    void notifyViewController();
}
