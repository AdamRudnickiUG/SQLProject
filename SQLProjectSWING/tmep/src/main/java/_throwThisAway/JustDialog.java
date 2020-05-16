package _throwThisAway;

import application.Config;
import model.UserRepository;
import utils.UiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JustDialog extends JDialog {

    Config config;
    UiUtils uiUtils;
    UserRepository userRepository;

    public JustDialog(
            JFrame parent,
            Config config,
            UiUtils uiUtils,
            UserRepository userRepository) {

        super(parent);

        this.config = config;
        this.uiUtils = uiUtils;
        this.userRepository = userRepository;

        getContentPane().add(makeTopPanel(), BorderLayout.NORTH);
        getContentPane().add(makeCenterPanel(), BorderLayout.CENTER);
        getContentPane().add(makeBottomPanel(), BorderLayout.SOUTH);

        uiUtils.minMaxPref(this, 600, 400);
        setLocationRelativeTo(null);
    }

    private JPanel makeTopPanel() {
        JPanel topPanel = new JPanel();
        uiUtils.minMaxPrefHeight(topPanel, config.menuHeight);
        uiUtils.border(topPanel, Color.lightGray, 0, 0, 1, 0);
        return topPanel;
    }

    private JPanel makeCenterPanel() {
        JPanel centerPanel = new JPanel();
        return centerPanel;
    }

    private Box makeBottomPanel() {
        Box bottomBox = Box.createHorizontalBox();
        uiUtils.minMaxPrefHeight(bottomBox, config.menuHeight);
        uiUtils.border(bottomBox, Color.lightGray, 1, 0, 0, 0);

        bottomBox.add(Box.createHorizontalGlue());
        bottomBox.add(makeCloseButton());
        bottomBox.add(Box.createHorizontalStrut(config.margin));

        return bottomBox;
    }

    private JButton makeCloseButton() {
        JButton button = new JButton();
        uiUtils.minMaxPref(button, config.buttonWidth, config.buttonHeight);
        button.setText("Close");

        final JustDialog THIS = this;
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                THIS.setVisible(false);
            }
        });

        return button;
    }
}
