package com.mikeoertli.rangeselector.api;

import com.mikeoertli.rangeselector.ui.swing.RangeSelectorPanelController;

/**
 * Manages all things related to a range selection GUI and I/O
 *
 * @since 0.0.1
 */
public class RangeSelectorController
{
    public RangeSelectorPanelController createRangeSelectorSwingPanelController()
    {
        return new RangeSelectorPanelController();
    }
}
