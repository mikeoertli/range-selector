package com.mikeoertli.rangeselector.javafx.ui;

import com.mikeoertli.rangeselector.api.IRangeViewController;
import javafx.scene.Scene;

/**
 * Standard interface for range view controller classes for JavaFX GUIs.
 * It is strongly recommended to extend {@link AJavaFxRangeViewController} instead of implementing this class.
 *
 * @since 0.1.0
 */
public interface IJavaFxViewController extends IRangeViewController
{
    /**
     * Helper method to make getting the view as a {@link Scene} easier.
     *
     * @return the view as a {@link Scene}
     * @see #getView()
     */
    default Scene getScene()
    {
        return (Scene) getView();
    }
}
