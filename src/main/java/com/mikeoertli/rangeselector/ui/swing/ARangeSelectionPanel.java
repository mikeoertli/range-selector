package com.mikeoertli.rangeselector.ui.swing;

import com.mikeoertli.rangeselector.api.IRangeSelectorView;
import com.mikeoertli.rangeselector.data.RangeConfiguration;
import com.mikeoertli.rangeselector.ui.common.IMouseInputHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.invoke.MethodHandles;

/**
 * Basic range selection panel abstract class. This handles a lot of the setup, listeners, etc.
 * A swing range selection panel is recommended to extend this rather than starting from scratch.
 * Note that any controller that wishes to use this will need to implement {@link ISwingViewController}
 * instead of the standard {@link com.mikeoertli.rangeselector.api.IRangeViewController}.
 *
 * @since 0.0.2
 */
public abstract class ARangeSelectionPanel extends JPanel implements IRangeSelectorView
{
    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    protected final ISwingViewController controller;

    /**
     * Constructor
     *
     * @param controller the controller that owns thia penal
     */
    protected ARangeSelectionPanel(ISwingViewController controller)
    {
        this.controller = controller;

        configureMinimumSize();
    }

    private void configureMinimumSize()
    {
        final Dimension minimumSize = getMinimumSize();
        final int controllerMinWidth = controller.getMinimumViewWidth();
        if (controllerMinWidth != (int) minimumSize.getWidth())
        {
            minimumSize.setSize(controllerMinWidth, minimumSize.getHeight());
            setMinimumSize(minimumSize);
        }
    }

    @Override
    public void paintComponent(Graphics graphics)
    {
        super.paintComponent(graphics);

        final RangeConfiguration rangeState = controller.getRangeConfiguration();

        if (rangeState.hasSelection())
        {
            final int startOfSelectedRange = rangeState.getSelectionMin();
            final int endOfSelectedRange = rangeState.getSelectionMax();

            paintRegionBeforeSelection(graphics, startOfSelectedRange);

            paintSelectedRegion(graphics, startOfSelectedRange, endOfSelectedRange);

            paintRegionAfterSelection(graphics, endOfSelectedRange);
        } else
        {
            paintUnselected(graphics);
        }
    }

    /**
     * As part of the {@link #paintComponent(Graphics)} method, the rendering is broken down into the region before
     * the selected range (this), the selected region, and the region after the selected region.
     *
     * @param graphics             the graphics instance used for rendering this panel
     * @param startOfSelectedRange the start of the selected range (i.e. the end of the unselected range). It is assumed
     *                             that the range's unselected region starts at the {@link RangeConfiguration#getRangeMin()}
     */
    protected abstract void paintRegionBeforeSelection(Graphics graphics, int startOfSelectedRange);

    /**
     * As part of the {@link #paintComponent(Graphics)} method, the rendering is broken down into the region before
     * the selected range, the selected region (this), and the region after the selected region.
     *
     * @param graphics             the graphics instance used for rendering this panel
     * @param startOfSelectedRange the start of the selected range. It is assumed that the range selection starts
     *                             at the {@link RangeConfiguration#getRangeMin()}
     * @param endOfSelectedRange   the end of the selected range, this will be less than or equal to
     *                             {@link RangeConfiguration#getRangeMax()}
     */
    protected abstract void paintSelectedRegion(Graphics graphics, int startOfSelectedRange, int endOfSelectedRange);

    /**
     * As part of the {@link #paintComponent(Graphics)} method, the rendering is broken down into the region before
     * the selected range, the selected region, and the region after the selected region (this).
     *
     * @param graphics           the graphics instance used for rendering this panel
     * @param endOfSelectedRange the end of the selected range (i.e. the start of the unselected range). It is assumed
     *                           that the range's unselected region ends at the {@link RangeConfiguration#getRangeMax()}
     */
    protected abstract void paintRegionAfterSelection(Graphics graphics, int endOfSelectedRange);

    /**
     * As part of the {@link #paintComponent(Graphics)} method, if there is any selected region, this is not called.
     * However, when there is no selected region, this is called to render the entire range as unselected.
     * This will use {@link RangeConfiguration#getRangeMin()} to {@link RangeConfiguration#getRangeMax()}
     * as the bounds.
     *
     * @param graphics the graphics instance used for rendering this panel
     */
    protected abstract void paintUnselected(Graphics graphics);

    @Override
    public boolean isLocked()
    {
        return isEnabled();
    }

    @Override
    public void lockPanel()
    {
        setEnabled(false);
    }

    @Override
    public void unlockPanel()
    {
        setEnabled(true);
    }

    @Override
    public void addMouseInputHandler(IMouseInputHandler handler)
    {
        if (handler instanceof MouseListener)
        {
            addMouseListener((MouseListener) handler);
        } else
        {
            logger.error("Illegal range selection listener. A Swing panel requires an IMouseInputHandler that is a " +
                    "MouseListener and the given listener ({}) is not.", handler.getClass().getSimpleName());
        }

        if (handler instanceof MouseMotionListener)
        {
            addMouseMotionListener((MouseMotionListener) handler);
        } else
        {
            logger.error("Illegal range selection listener. A Swing panel requires an IMouseInputHandler that is a " +
                    "MouseMotionListener and the given listener ({}) is not.", handler.getClass().getSimpleName());
        }
    }

    @Override
    public void removeMouseInputHandler(IMouseInputHandler handler)
    {
        if (handler instanceof MouseListener)
        {
            removeMouseListener((MouseListener) handler);
        }

        if (handler instanceof MouseMotionListener)
        {
            removeMouseMotionListener((MouseMotionListener) handler);
        }
    }

    @Override
    public void refreshView()
    {
        configureMinimumSize();
        revalidate();
        repaint();
    }
}
