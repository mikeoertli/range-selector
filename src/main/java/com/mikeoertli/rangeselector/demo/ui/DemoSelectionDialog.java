package com.mikeoertli.rangeselector.demo.ui;

import javax.swing.*;
import com.mikeoertli.rangeselector.Constants;
import com.mikeoertli.rangeselector.core.util.ResourceUtilities;
import com.mikeoertli.rangeselector.demo.ThirdPartyApp;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

/**
 * Simple GUI for deciding which demo app to run
 *
 * @since 0.0.2
 */
public class DemoSelectionDialog extends JDialog
{
    private final ThirdPartyApp controller;

    public DemoSelectionDialog(ThirdPartyApp controller)
    {
        super();
        this.controller = controller;

        ResourceUtilities.getResourceAsImage(Constants.RANGE_SELECTOR_ICON_32X32_PATH).ifPresent(image -> getOwner().setIconImage(image));

        initComponents();
    }

    private void cancelButtonActionPerformed()
    {
        dispose();
    }

    private void okButtonActionPerformed()
    {
        if (simpleDemoCheckBox.isSelected())
        {
            controller.showSimpleDemo();
        }
        if (histogramSmallCheckBox.isSelected())
        {
            controller.showSmallHistogramDemo();
        }
        if (histogramLargeCheckBox.isSelected())
        {
            controller.showLargeHistogramDemo();
        }
    }

    private void initComponents()
    {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        dialogPane = new JPanel();
        contentPanel = new JPanel();
        instructionsLabel = new JLabel();
        vSpacer1 = new JPanel(null);
        simpleDemoCheckBox = new JCheckBox();
        histogramSmallCheckBox = new JCheckBox();
        histogramLargeCheckBox = new JCheckBox();
        buttonBar = new JPanel();
        okButton = new JButton();
        cancelButton = new JButton();

        //======== this ========
        setTitle("Demo Selection");
        setMinimumSize(new Dimension(210, 200));
        setName("this");
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== dialogPane ========
        {
            dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
            dialogPane.setMinimumSize(new Dimension(273, 215));
            dialogPane.setPreferredSize(new Dimension(273, 220));
            dialogPane.setName("dialogPane");
            dialogPane.setLayout(new BorderLayout());

            //======== contentPanel ========
            {
                contentPanel.setName("contentPanel");
                contentPanel.setLayout(new GridBagLayout());
                ((GridBagLayout)contentPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).rowHeights = new int[] {52, 0, 0, 0, 0, 0};
                ((GridBagLayout)contentPanel.getLayout()).columnWeights = new double[] {0.0, 0.0, 0.0, 0.0, 1.0E-4};
                ((GridBagLayout)contentPanel.getLayout()).rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, 1.0E-4};

                //---- instructionsLabel ----
                instructionsLabel.setText("Select which demo GUI(s) to run...");
                instructionsLabel.setIcon(new ImageIcon(getClass().getResource("/icon/range-selection-icon-32.png")));
                instructionsLabel.setName("instructionsLabel");
                contentPanel.add(instructionsLabel, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 0), 0, 0));

                //---- vSpacer1 ----
                vSpacer1.setName("vSpacer1");
                contentPanel.add(vSpacer1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- simpleDemoCheckBox ----
                simpleDemoCheckBox.setText("Simple");
                simpleDemoCheckBox.setName("simpleDemoCheckBox");
                contentPanel.add(simpleDemoCheckBox, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- histogramSmallCheckBox ----
                histogramSmallCheckBox.setText("Histogram (small)");
                histogramSmallCheckBox.setName("histogramSmallCheckBox");
                contentPanel.add(histogramSmallCheckBox, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 5, 5), 0, 0));

                //---- histogramLargeCheckBox ----
                histogramLargeCheckBox.setText("Histogram (large)");
                histogramLargeCheckBox.setSelected(true);
                histogramLargeCheckBox.setName("histogramLargeCheckBox");
                contentPanel.add(histogramLargeCheckBox, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));
            }
            dialogPane.add(contentPanel, BorderLayout.CENTER);

            //======== buttonBar ========
            {
                buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
                buttonBar.setName("buttonBar");
                buttonBar.setLayout(new GridBagLayout());
                ((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
                ((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

                //---- okButton ----
                okButton.setText("Start");
                okButton.setName("okButton");
                okButton.addActionListener(e -> okButtonActionPerformed());
                buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 5), 0, 0));

                //---- cancelButton ----
                cancelButton.setText("Quit");
                cancelButton.setName("cancelButton");
                cancelButton.addActionListener(e -> cancelButtonActionPerformed());
                buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
                    GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                    new Insets(0, 0, 0, 0), 0, 0));
            }
            dialogPane.add(buttonBar, BorderLayout.SOUTH);
        }
        contentPane.add(dialogPane, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel instructionsLabel;
    private JPanel vSpacer1;
    private JCheckBox simpleDemoCheckBox;
    private JCheckBox histogramSmallCheckBox;
    private JCheckBox histogramLargeCheckBox;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
