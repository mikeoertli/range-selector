package com.mikeoertli.rangeselector.ui.swing.simple;

import com.mikeoertli.rangeselector.api.IRangeSelectorView;
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
    protected IRangeSelectorView createPanel()
    {
        return new SimpleRangeSelectionPanel(this);
    }
}
