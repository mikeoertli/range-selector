package com.mikeoertli.rangeselector.ui.swing;

import com.mikeoertli.rangeselector.api.IRangeViewController;

import javax.swing.JPanel;

/**
 * Just adds a helpful method for getting the view as a {@link javax.swing.JPanel}
 *
 * @since 0.0.1
 */
public interface ISwingViewController extends IRangeViewController
{
    default JPanel getPanel()
    {
        return (JPanel) getView();
    }
}
