package com.mikeoertli.rangeselector.ui.swing;

import com.mikeoertli.rangeselector.ui.swing.simple.SimpleRangeSelectorPanelController;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

/**
 * Playground for experimenting with UIs
 *
 * @since 0.0.1
 */
public class PlaygroundUi
{

    public PlaygroundUi()
    {
        JDialog dialog = new JDialog();
        dialog.setSize(400, 100);
        SimpleRangeSelectorPanelController controller = new SimpleRangeSelectorPanelController();
        dialog.add(controller.getPanel());
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
