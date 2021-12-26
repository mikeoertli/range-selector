package com.mikeoertli.rangeselector.swing.ui.simple;

import com.mikeoertli.rangeselector.swing.ui.ASwingRangeSelectionPanel;
import com.mikeoertli.rangeselector.swing.ui.ASwingRangeViewController;

/**
 * The controller for a swing based range selection panel defined by {@link SimpleRangeSelectionPanel}
 *
 * @since 0.0.1
 */
public class SimpleRangeSelectionPanelController extends ASwingRangeViewController
{
    public SimpleRangeSelectionPanelController()
    {
        super();
    }

    @Override
    protected ASwingRangeSelectionPanel createPanel()
    {
        return new SimpleRangeSelectionPanel(this);
    }

    @Override
    public int getMinimumViewWidth()
    {
        return 200;
    }
}
