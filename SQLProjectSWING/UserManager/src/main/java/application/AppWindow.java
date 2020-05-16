package application;

import utils.UiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppWindow extends JFrame {

    private JPanel topPanel;

    public AppWindow() {

        super("User Management");

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
        UiUtils.minMaxPrefHeight(topPanel, GlobalSizes.menuHeight);
        UiUtils.border(topPanel, Color.lightGray, 0, 0, 1, 0);
        topPanel.add(makeTmpButton());
        return topPanel;
    }

    private JButton makeTmpButton() {
        JButton butExampleDialog = new JButton();
        UiUtils.minMaxPref(butExampleDialog, GlobalSizes.buttonWidth, GlobalSizes.buttonHeight);
        butExampleDialog.setText("Example dialog");

        butExampleDialog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AppGraph.getInstance().getJustDialog().setVisible(true);
            }
        });
        return butExampleDialog;
    }

    private JButton makeButton(String name) {
        JButton createdButton = new JButton();
        UiUtils.minMaxPref(createdButton, GlobalSizes.buttonWidth, GlobalSizes.buttonHeight);
        createdButton.setText(name);

//        createdButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                AppGraph.getInstance().getJustDialog().setVisible(true);
//            }
//        });
        return createdButton;
    }
}
