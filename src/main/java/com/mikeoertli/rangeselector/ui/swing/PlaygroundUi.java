package com.mikeoertli.rangeselector.ui.swing;

import com.mikeoertli.rangeselector.ui.swing.simple.SimpleRangeSelectionPanel;

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
        dialog.add(new SimpleRangeSelectionPanel());
        dialog.setVisible(true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }
}
