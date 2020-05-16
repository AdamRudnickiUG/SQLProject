package _throwThisAway;

import application.GlobalSizes;
import model.UserRepository;
import utils.UiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class JustDialog extends JDialog {

    UserRepository userRepository;

    public JustDialog(
            JFrame parent,
            UserRepository userRepository) {

        super(parent);

        this.userRepository = userRepository;

        getContentPane().add(makeTopPanel(), BorderLayout.NORTH);
        getContentPane().add(makeCenterPanel(), BorderLayout.CENTER);
        getContentPane().add(makeBottomPanel(), BorderLayout.SOUTH);

        UiUtils.minMaxPref(this, 600, 400);
        setLocationRelativeTo(null);
    }

    private JPanel makeTopPanel() {
        JPanel topPanel = new JPanel();
        UiUtils.minMaxPrefHeight(topPanel, GlobalSizes.menuHeight);
        UiUtils.border(topPanel, Color.lightGray, 0, 0, 1, 0);
        return topPanel;
    }

    private JPanel makeCenterPanel() {
        JPanel centerPanel = new JPanel();
        return centerPanel;
    }

    private Box makeBottomPanel() {
        Box bottomBox = Box.createHorizontalBox();
        UiUtils.minMaxPrefHeight(bottomBox, GlobalSizes.menuHeight);
        UiUtils.border(bottomBox, Color.lightGray, 1, 0, 0, 0);

        bottomBox.add(Box.createHorizontalGlue());
        bottomBox.add(makeCloseButton());
        bottomBox.add(Box.createHorizontalStrut(GlobalSizes.margin));

        return bottomBox;
    }

    private JButton makeCloseButton() {
        JButton button = new JButton();
        UiUtils.minMaxPref(button, GlobalSizes.buttonWidth, GlobalSizes.buttonHeight);
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
