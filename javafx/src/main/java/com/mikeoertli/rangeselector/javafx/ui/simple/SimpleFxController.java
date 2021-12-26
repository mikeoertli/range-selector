package com.mikeoertli.rangeselector.javafx.ui.simple;

import com.mikeoertli.rangeselector.javafx.ui.AFxRangeSelectionPane;
import com.mikeoertli.rangeselector.javafx.ui.AJavaFxRangeViewController;

public class SimpleFxController extends AJavaFxRangeViewController
{

    public SimpleFxController()
    {

    }

    @Override
    public int getMinimumViewWidth()
    {
        return 100;
    }

    @Override
    protected AFxRangeSelectionPane createPane()
    {
        return new SimpleRangeSelectionFxPane(this);
    }
}
