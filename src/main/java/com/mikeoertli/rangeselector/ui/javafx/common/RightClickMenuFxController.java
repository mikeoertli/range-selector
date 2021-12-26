package com.mikeoertli.rangeselector.ui.javafx.common;

import com.mikeoertli.rangeselector.ui.common.ILockListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;

/**
 * Handles creating and showing the right click menu for JavaFX GUIs
 *
 * @since 0.1.0
 */
public class RightClickMenuFxController
{
    private static final String LOCKED_TEXT = "Unlock";
    private static final String UNLOCKED_TEXT = "Lock";

    private final ILockListener lockListener;
    private boolean locked;

    private final ContextMenu menu;
    private final MenuItem lockMenuItem;

    public RightClickMenuFxController(ILockListener lockListener)
    {
        this.lockListener = lockListener;
        menu = new ContextMenu();

        lockMenuItem = new MenuItem(getLockMenuText());
        lockMenuItem.setOnAction(e -> toggleLock());

        menu.getItems().add(lockMenuItem);
    }

    private void toggleLock()
    {
        lockListener.toggleLock();

        locked = !locked;
        lockMenuItem.setText(getLockMenuText());
    }

    private String getLockMenuText()
    {
        return locked ? LOCKED_TEXT : UNLOCKED_TEXT;
    }

    public EventHandler<? super ContextMenuEvent> getContextMenuRequestedListener()
    {
        return (EventHandler<ContextMenuEvent>) event -> {
            menu.show((Node) event.getSource(), event.getScreenX(), event.getScreenY());
        };
    }
}
