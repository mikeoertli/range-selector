package com.mikeoertli.rangeselector.core;

import com.mikeoertli.rangeselector.api.IRangeController;
import com.mikeoertli.rangeselector.api.IRangeType;
import com.mikeoertli.rangeselector.api.IRangeViewController;
import com.mikeoertli.rangeselector.data.GuiFrameworkType;
import com.mikeoertli.rangeselector.data.rangestate.ARangeState;
import com.mikeoertli.rangeselector.data.rangetype.FrequencyUnits;
import com.mikeoertli.rangeselector.ui.swing.FrequencyRangeSelectorPanelController;

import javax.naming.OperationNotSupportedException;

/**
 * Manages all things related to a range selection GUI and I/O
 *
 * @since 0.0.1
 */
public class FrequencyRangeController implements IRangeController<FrequencyUnits>
{

    @Override
    public Class<FrequencyUnits> getRangeType()
    {
        return FrequencyUnits.class;
    }

    @Override
    public IRangeViewController<FrequencyUnits> createViewController(GuiFrameworkType guiFramework, ARangeState<FrequencyUnits> rangeState) throws OperationNotSupportedException
    {
        if (GuiFrameworkType.SWING == guiFramework)
        {
            return new FrequencyRangeSelectorPanelController(FrequencyUnits.MHZ);
        } else
        {
            throw new OperationNotSupportedException(guiFramework + " is not a supported GUI framework.");
        }
    }

    @Override
    public boolean isConfigurationSupported(Class<? extends IRangeType<?, ?>> rangeType, GuiFrameworkType guiFrameworkType)
    {
        return FrequencyUnits.class == rangeType && GuiFrameworkType.SWING == guiFrameworkType;
    }
}
