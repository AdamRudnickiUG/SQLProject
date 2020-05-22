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
                AppGraph.getInstance().getPostCodesDialog().setVisible(true);
            }
        }));

        listOfUsers.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                AppGraph.getInstance().getJustDialog().setVisible(true);
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
