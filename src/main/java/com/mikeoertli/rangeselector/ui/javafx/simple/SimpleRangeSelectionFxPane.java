package com.mikeoertli.rangeselector.ui.javafx.simple;

import com.mikeoertli.rangeselector.ui.javafx.AFxRangeSelectionPane;
import com.mikeoertli.rangeselector.ui.javafx.IJavaFxViewController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class SimpleRangeSelectionFxPane extends AFxRangeSelectionPane
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public SimpleRangeSelectionFxPane(IJavaFxViewController controller)
    {
        super(controller);
    }

    @Override
    public void refreshView()
    {

    }
}
