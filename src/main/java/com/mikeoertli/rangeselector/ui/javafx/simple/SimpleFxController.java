package com.mikeoertli.rangeselector.ui.javafx.simple;

import com.mikeoertli.rangeselector.ui.javafx.AFxRangeSelectionPane;
import com.mikeoertli.rangeselector.ui.javafx.AJavaFxRangeViewController;

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
