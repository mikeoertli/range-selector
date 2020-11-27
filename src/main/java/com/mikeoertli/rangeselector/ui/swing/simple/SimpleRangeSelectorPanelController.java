package com.mikeoertli.rangeselector.ui.swing.simple;

import com.mikeoertli.rangeselector.ui.swing.ARangeSelectionPanel;
import com.mikeoertli.rangeselector.ui.swing.ASwingRangeViewController;

/**
 * The controller for a swing based range selection panel defined by {@link SimpleRangeSelectionPanel}
 *
 * @since 0.0.1
 */
public class SimpleRangeSelectorPanelController extends ASwingRangeViewController
{
    public SimpleRangeSelectorPanelController()
    {
        super();
    }

    @Override
    protected ARangeSelectionPanel createPanel()
    {
        return new SimpleRangeSelectionPanel(this);
    }

    @Override
    public int getMinimumViewWidth()
    {
        return 200;
    }
}
