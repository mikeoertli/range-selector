package com.mikeoertli.rangeselector.swing.ui.common;

import com.mikeoertli.rangeselector.ui.ILockListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import java.awt.event.MouseEvent;

/**
 * Simple manager for handling right click events, it presents a context menu that shows available menu options.
 * The built-in supported menu option is for locking/unlocking the selection range, but in future versions it
 * stands to reason that additional context menu items may be supported.
 *
 * @since 0.0.3
 */
public class RightClickMenuSwingController
{
    private static final String LOCKED_TEXT = "Unlock";
    private static final String UNLOCKED_TEXT = "Lock";

    private final JPopupMenu menu;
    private final JMenuItem lockMenuItem;
    private final ILockListener lockListener;
    private boolean locked;

    public RightClickMenuSwingController(ILockListener lockListener)
    {
        this.lockListener = lockListener;
        menu = new JPopupMenu();

        lockMenuItem = new JMenuItem(getLockMenuText());
        lockMenuItem.addActionListener(e -> toggleLock());

        menu.add(lockMenuItem);
    }

    private void toggleLock()
    {
        lockListener.toggleLock();

        SwingUtilities.invokeLater(() -> {
            locked = !locked;
            lockMenuItem.setText(getLockMenuText());
        });
    }

    private String getLockMenuText()
    {
        return locked ? LOCKED_TEXT : UNLOCKED_TEXT;
    }

    public void processEventShowPopup(MouseEvent event)
    {
        if (event.isPopupTrigger())
        {
            SwingUtilities.invokeLater(() -> menu.show(event.getComponent(), event.getX(), event.getY()));
        }
    }
}
