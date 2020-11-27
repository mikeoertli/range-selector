package com.mikeoertli.rangeselector.demo.ui;

import java.awt.Window;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple window listener to make the enter key listener on the demo window launching GUI work more intuitively
 *
 * @since 0.0.2
 */
public class DemoWindowListener extends WindowAdapter
{
    private final List<Window> observedWindows = new ArrayList<>();
    private boolean enabled = true;
    private final DemoSelectionDialog demoSelectionDialog;

    public DemoWindowListener(DemoSelectionDialog demoSelectionDialog)
    {
        this.demoSelectionDialog = demoSelectionDialog;
    }

    @Override
    public void windowClosing(WindowEvent e)
    {
        observedWindows.remove(e.getWindow());

        if (!enabled && observedWindows.isEmpty())
        {
            demoSelectionDialog.initializeKeyListeners();
            enabled = true;
        }
    }

    @Override
    public void windowOpened(WindowEvent e)
    {
        if (enabled)
        {
            demoSelectionDialog.removeKeyListeners();
            enabled = false;
        }
    }

    public void addObservedWindow(Window window)
    {
        window.addWindowListener(this);
        observedWindows.add(window);
    }
}
