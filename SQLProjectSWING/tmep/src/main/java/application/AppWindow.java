package application;

import utils.UiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppWindow extends JFrame {

    private Config config;
    private UiUtils uiUtils;

    private JPanel topPanel;

    public AppWindow(
            UiUtils uiUtils,
            Config config){

        super("User Management");

        this.config = config;
        this.uiUtils = uiUtils;

        setExtendedState(Frame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().add(makeTopPanel(), BorderLayout.NORTH);

        JLabel label = new JLabel("Add, edit, delete users");
        getContentPane().add(label);
    }

    public void showApp() {
        pack();
        setVisible(true);
    }

    private JPanel makeTopPanel() {
        topPanel = new JPanel();
        uiUtils.minMaxPrefHeight(topPanel, config.menuHeight);
        uiUtils.border(topPanel, Color.lightGray, 0, 0, 1, 0);
        topPanel.add(makeTmpButton());
        return topPanel;
    }

    private JButton makeTmpButton() {
        JButton butExampleDialog = new JButton();
        uiUtils.minMaxPref(butExampleDialog, config.buttonWidth, config.buttonHeight);
        butExampleDialog.setText("Example dialog");

        butExampleDialog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AppGraph.getInstance().getJustDialog().setVisible(true);
            }
        });
        return butExampleDialog;
    }
}
