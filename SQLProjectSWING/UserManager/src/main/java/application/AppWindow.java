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

//        topPanel.add(makeDefaultButton("Edytuj kody pocztowe"));
//        topPanel.add(makeDefaultButton("Edytuj uzytowników"));
//        topPanel.add(makeDefaultButton("Edytuj kontakt_do_kontrahenta"));


        //CREATION AND ADDITION OF MAIN VBOX

        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.blue);

        getContentPane().add(mainVBox, BorderLayout.CENTER);
        mainVBox.add(makeDefaultButton("Edytuj kody pocztowe"));
        mainVBox.add(makeDefaultButton("Edytuj uzytowników"));
        mainVBox.add(makeDefaultButton("Edytuj kontakt_do_kontrahenta"));


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
//        middlePanel.add(testMenu);

//        getContentPane().add(testMenu, BorderLayout.SOUTH);
//        mainHBox.add(testMenu);

//        JLabel label = new JLabel("Add, edit, delete users");
//        getContentPane().add(label);
    }

    public void showApp() {
        pack();
        setVisible(true);
    }

    public JMenuBar defaultMenu(){
        JMenuBar tempMenuBar = new JMenuBar();

        JMenu tempMenu = new JMenu("Menu");

        JMenuItem item1 = new JMenuItem("example item 1");
        JMenuItem item2 = new JMenuItem("example item 2");
        JMenuItem item3 = new JMenuItem("example item 3");

        tempMenu.add(item1);
        tempMenu.add(item2);
        tempMenu.add(item3);

        tempMenuBar.add(tempMenu);

        return tempMenuBar;
    }

    private JPanel makeTopPanel() {
        topPanel = new JPanel();
        UiUtils.minMaxPrefHeight(topPanel, GlobalSizes.menuHeight);
        UiUtils.border(topPanel, Color.lightGray, 0, 0, 1, 0);
//        topPanel.add(makeTmpButton());
        topPanel.add(defaultMenu());
        return topPanel;
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
