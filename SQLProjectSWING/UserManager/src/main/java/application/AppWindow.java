package application;

import DBOperations.TestOps;
import utils.UiUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AppWindow extends JFrame {

    private TestOps testOps;
    private JPanel topPanel;
    private AppGraph appGraph;

    public AppWindow(final TestOps testOps, final AppGraph appGraph) {
        super("User Management");
        this.testOps = testOps;
        this.appGraph = appGraph;


        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screeHeight = (int) screenSize.getHeight();
        int screeWidth = (int) screenSize.getWidth();

        setExtendedState(Frame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //ADDITION OF TOP PANEL
        getContentPane().add(makeTopPanel(), BorderLayout.NORTH);


        Box fieldDescriptions = Box.createVerticalBox();
        fieldDescriptions.add(new JLabel("login"));
        fieldDescriptions.add(new JLabel("haslo"));

        Box loginFields = Box.createVerticalBox();
        final TextField loginField = new TextField();
        final TextField passwdField = new TextField();

        Font font1 = new Font("SansSerif", Font.BOLD, 8);
        loginField.setFont(font1);
        passwdField.setFont(font1);

        loginFields.add(Box.createHorizontalStrut(GlobalSizes.buttonWidth));
        loginFields.add(loginField);
        loginFields.add(passwdField);

        MyButton loginButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        loginButton.setText("zaloguj");
        final String[] QueryCheckCredentials = new String[1];
        loginButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                Connection con = testOps.getConnection();

                QueryCheckCredentials[0] = "SELECT * FROM logowanie WHERE login = '"
                        + loginField.getText() + "' AND haslo = '"
                        + passwdField.getText() + "'";

                Statement st = null;
                try {
                    st = con.createStatement();
                } catch (SQLException e) {
                    appGraph.getErrorDialog("Błąd podczas łączenia \nz bazą danych");
                    e.printStackTrace();
                }
                ResultSet rs = null;
                try {
                    rs = st.executeQuery(QueryCheckCredentials[0]);
                } catch (SQLException e) {
                    appGraph.getErrorDialog("Błąd podczas \npotwierdzania danych");
                    e.printStackTrace();
                }

                ResultSet rs2 = null;
                try {
                    rs2 = st.executeQuery(QueryCheckCredentials[0]);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    if (rs2.next() && rs2 != null) {
                        testOps.setPerms(Integer.parseInt(rs2.getString("perms")));
                    }
                    else
                        appGraph.getErrorDialog("Bledne dane logowania");
                } catch (SQLException e) {
                    appGraph.getErrorDialog("Blad SQL");
                    e.printStackTrace();
                }
            }
        });
        topPanel.add(loginButton, BorderLayout.EAST);
        topPanel.add(loginFields);
        topPanel.add(fieldDescriptions);

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
//        postalCodes.addActionListener();
        JMenuItem listOfUsers = new JMenuItem("Lista uzytkowników");

        tempMenu.add(postalCodes);
        tempMenu.add(listOfUsers);

        //Make menu options do something
        postalCodes.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() > 0)
                    try {
                        AppGraph.getInstance().getPostCodesDialog().setVisible(true);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                else
                    appGraph.getErrorDialog("Goscie nie maja dostepu do bazy");
            }
        }));

        listOfUsers.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() > 0)
                    try {
                        AppGraph.getInstance().getClientListDialog().setVisible(true);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                else
                    appGraph.getErrorDialog("Gosci nie maja dostepu do bazy ");
            }
        }));


        //filling menuBar
        tempMenuBar.add(tempMenu);

        return tempMenuBar;
    }

    private JPanel makeTopPanel() {
        topPanel = new JPanel();
        UiUtils.minMaxPrefHeight(topPanel, GlobalSizes.menuHeight + 10);
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
