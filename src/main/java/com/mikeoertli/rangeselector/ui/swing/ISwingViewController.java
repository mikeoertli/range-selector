package com.mikeoertli.rangeselector.ui.swing;

import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.api.IViewStyleProvider;

import javax.swing.JPanel;

/**
 * Just adds a helpful method for getting the view as a {@link javax.swing.JPanel}
 *
 * @since 0.0.1
 */
public interface ISwingViewController extends IRangeViewController
{
    /**
     * Helper method to make getting the view as a {@link JPanel} easier.
     *
     * @return the view as a {@link JPanel}
     * @see #getView()
     */
    default JPanel getPanel()
    {
        return (JPanel) getView();
    }

    /**
     * @return the provider for information regarding how to style the view
     */
    IViewStyleProvider getViewStyleProvider();

    /**
     * Trigger the view controller to refresh the view
     */
    void onViewConfigurationChanged();
}
