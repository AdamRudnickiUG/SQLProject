package application;

import utils.UiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

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


        //CREATION AND ADDITION OF MAIN VBOX
        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        getContentPane().add(mainVBox, BorderLayout.CENTER);
    }

    public void showApp() {
        pack();
        setVisible(true);
    }

    //MENU CREATION
    public JMenuBar defaultMenu() {
        JMenuBar tempMenuBar = new JMenuBar();

        JMenu tempMenu = new JMenu("Menu");

        JMenuItem postalCodes = new JMenuItem("Lista kodów pocztowych");
        JMenuItem listOfUsers = new JMenuItem("Lista uzytkowników");

        tempMenu.add(postalCodes);
        tempMenu.add(listOfUsers);

        //Make menu options do something
        postalCodes.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    AppGraph.getInstance().getPostCodesDialog().setVisible(true);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }));

        listOfUsers.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AppGraph.getInstance().getClientListDialog().setVisible(true);
            }
        }));


        //filling menuBar
        tempMenuBar.add(tempMenu);

        return tempMenuBar;
    }

    private JPanel makeTopPanel() {
        topPanel = new JPanel();
        UiUtils.minMaxPrefHeight(topPanel, GlobalSizes.menuHeight);
        UiUtils.border(topPanel, Color.lightGray, 0, 0, 1, 0);
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
