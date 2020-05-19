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


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screeHeight = (int) screenSize.getHeight();
        int screeWidth = (int) screenSize.getWidth();

        setExtendedState(Frame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //ADDITION OF TOP PANEL
        getContentPane().add(makeTopPanel(), BorderLayout.NORTH);


        //CREATION AND ADDITION OF MAIN HBOX

//        Box mainHBox = Box.createHorizontalBox();
//        mainHBox.setOpaque(true);
//        mainHBox.setBackground(Color.blue);
//
//        getContentPane().add(mainHBox, BorderLayout.CENTER);
//        mainHBox.add(makeDefaultButton("exampleButton"));


        //MENU CREATION
        Menu testMenu = new Menu();

        testMenu.add("first string");
        testMenu.add("seconds string");

        MenuItem firstItem = new MenuItem();
        MenuItem secondItem = new MenuItem();

        testMenu.add(firstItem);
        testMenu.add(secondItem);


        //
        JPanel middlePanel = new JPanel();
        middlePanel.add(testMenu);

//        getContentPane().add(testMenu, BorderLayout.SOUTH);
//        mainHBox.add(testMenu);

//        JLabel label = new JLabel("Add, edit, delete users");
//        getContentPane().add(label);
    }

    public void showApp() {
        pack();
        setVisible(true);
    }

    private JPanel makeTopPanel() {
        topPanel = new JPanel();
        UiUtils.minMaxPrefHeight(topPanel, GlobalSizes.menuHeight);
        UiUtils.border(topPanel, Color.lightGray, 0, 0, 1, 0);
//        topPanel.add(makeTmpButton());


        topPanel.add(makeDefaultButton("Edytuj kody pocztowe"));
        topPanel.add(makeDefaultButton("Edytuj uzytowników"));
        topPanel.add(makeDefaultButton("Edytuj kontakt_do_kontrahenta"));
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

    private JButton makeDefaultButton(String displayedText) {
        MyButton tempButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        tempButton.setText(displayedText);

        tempButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AppGraph.getInstance().getJustDialog().setVisible(true);
            }
        });
        return tempButton;
    }
}
