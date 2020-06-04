package application;

import DBOperations.TestOps;
import _throwThisAway.JustDialog;
import model.Database;
import model.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class AppGraph {

    // APP GRAPH SINGLETON
    private static AppGraph instance = null;

    TestOps testOps = new TestOps();
    // APPLICATION OBJECTS DI
    Database database = new Database();
    UserRepository userRepository = new UserRepository(database);
    //-------------------------------------------------------------
    //-------------------------------------------------------------
    AppWindow appWindow = new AppWindow(testOps, this);
    JustDialog justDialog = new JustDialog(appWindow, userRepository);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screeHeight = (int) screenSize.getHeight();
    int screeWidth = (int) screenSize.getWidth();

    public static AppGraph getInstance() {
        if (instance == null) instance = new AppGraph();

        TestOps testOps = new TestOps();
        testOps.testConnection();

        return instance;
    }

    //------------------------------------------------------------------------------------------
    public void getProgramAccountsDialog() {

        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        Box mainHBox = Box.createHorizontalBox();
        mainVBox.add(mainHBox);

        Box smallVbox = Box.createVerticalBox();
        mainHBox.add(smallVbox);

        final JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        tempDialog.add(mainVBox);

        String[] logins = null;
        String[] passwords = null;
        String[] permsArray = null;

        try {
            logins = testOps.getColumnData(testOps.getConnection(), "logowanie", "login");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            passwords = testOps.getColumnData(testOps.getConnection(), "logowanie", "haslo");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            permsArray = testOps.getColumnData(testOps.getConnection(), "logowanie", "perms");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] allInfo = new String[logins.length];


        for (int i = 0; i < logins.length; i++) {
            allInfo[i] = "login:  " + logins[i] +
                    " | haslo: " + passwords[i] +
                    " | poziom przyzwolen: " + permsArray[i];
        }


        final DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < logins.length; i++) {
            model.addElement(allInfo[i]);
        }

        final JList listOfPayments = new JList(model);
        final JScrollPane scrollableList = new JScrollPane(listOfPayments);
        smallVbox.add(scrollableList);


        final String defaultText = "Wprowadz dane";

        JPanel dataPanel = new JPanel();
        JPanel descriptionPanel = new JPanel();

        mainHBox.add(dataPanel);
        mainHBox.add(descriptionPanel);

        Box dataInputVBox = Box.createVerticalBox();
        Box descriptionVBox = Box.createVerticalBox();

        dataPanel.add(dataInputVBox);
        descriptionPanel.add(descriptionVBox);

        //nazwa, cena1, cena2, cena3

        final TextField loginField = new TextField();
        final TextField passwordField = new TextField();
        final TextField permsField = new TextField();

        dataInputVBox.add(loginField);
        dataInputVBox.add(passwordField);
        dataInputVBox.add(permsField);


        JLabel loginLabel = new JLabel("login");
        JLabel passwordLabel = new JLabel("haslo");
        JLabel permsLabel = new JLabel("uprawnienia");

        descriptionVBox.add(loginLabel);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(passwordLabel);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(permsLabel);


        MyButton editButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        editButton.setText("edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else if (Integer.parseInt(permsField.getText()) > 2 || Integer.parseInt(permsField.getText()) < 0) {
                    getErrorDialog("Podaj przyzwolenia od 0 do 2, 2 = admin");
                } else {
                    Connection con = testOps.getConnection();
                    ResultSet rs0 = null;
                    Statement st0 = null;


                    String QueryEDIT = "UPDATE logowanie SET " +
                            "haslo = " + passwordField.getText() +
                            ", perms = " + permsField.getText() +
                            "WHERE login = '" + loginField.getText() + "'";
                    Statement st = null;
                    Connection con2 = testOps.getConnection();
                    try {
                        st = con2.createStatement();
                        try {
                            if (testOps.getPerms() < Integer.parseInt(permsField.getText())) {
                                getErrorDialog("Mozesz nadawac maksymalnie twoje uprawnienia");
                            } else {
                                st.executeUpdate(QueryEDIT);
                                getErrorDialog("Record edition succesfull");
                            }
                        } catch (SQLException e) {
                            getErrorDialog("Can't edit item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                    try {
                        con2.close();
                    } catch (SQLException e) {
                        getErrorDialog("Can't close connection");
                        e.printStackTrace();
                    }
                }
            }

        });
        smallVbox.add(editButton);

        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        addButton.setText("add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else if (Integer.parseInt(permsField.getText()) > 2 || Integer.parseInt(permsField.getText()) < 0) {
                    getErrorDialog("Podaj przyzwolenia od 0 do 2, 2 = admin");

                } else {
                    Connection con = testOps.getConnection();
                    String QueryADD = "INSERT INTO logowanie VALUES ('" + loginField.getText() + "', '" +
                            passwordField.getText() + "', '" +
                            permsField.getText() + "')";

                    Statement st = null;
                    if (testOps.getPerms() < Integer.parseInt(permsField.getText())) {
                        getErrorDialog("Mozesz nadawac maksymalnie swoje uprawnienia!");
                    } else {
                        try {
                            st = con.createStatement();
                            try {
                                st.executeUpdate(QueryADD);
                                getErrorDialog("Record addition succesfull");
                            } catch (SQLException e) {
                                getErrorDialog("Can't add item");
                                e.printStackTrace();
                            }
                        } catch (SQLException e) {
                            getErrorDialog("Can't create statement");
                            e.printStackTrace();
                        }
                    }

                }
                tempDialog.setVisible(false);
            }

        });
        smallVbox.add(addButton);


        MyButton deleteButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        deleteButton.setText("delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

                if (testOps.getPerms() < 2) {
                    getErrorDialog("Tylko admin moze ususwac!");
                } else {

//                    getErrorDialog(listOfPayments.getSelectedIndex() + "");
                    Connection con = testOps.getConnection();

                    String QueryDELETE = "DELETE FROM logowanie WHERE login = '" + loginField.getText() + "'";

                    Statement st = null;
                    try {
                        st = con.createStatement();
                        try {
                            st.executeUpdate(QueryDELETE);
                            getErrorDialog("Record deletion succesfull");
                            listOfPayments.ensureIndexIsVisible(model.getSize());

                        } catch (SQLException e) {
                            getErrorDialog("Can't delete item");
                            e.printStackTrace();
                        }
                        tempDialog.setVisible(false);
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        smallVbox.add(deleteButton);


        tempDialog.setVisible(true);
    }

    // GETTERS
    public AppWindow getAppWindow() {
        return appWindow;
    }


    //------------------------------------------------------------------------------------------
//    public JustDialog getClientListDialog() throws SQLException {
//        Box mainVBox = Box.createVerticalBox();
//        mainVBox.setOpaque(true);
//        mainVBox.setBackground(Color.lightGray);
//
//        Box smallHbox = Box.createHorizontalBox();
//        mainVBox.add(smallHbox);
//
//        JustDialog tempDialog = new JustDialog(appWindow, userRepository);
//
//        tempDialog.add(mainVBox);
//
//        String[] names = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "imie");
//        String[] surnames = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "nazwisko");
//
//        String[] jointNames = new String[names.length];
//        for (int i = 0; i < names.length; i++) {
//            jointNames[i] = surnames[i] + " " + names[i];
//        }
//
//
//        DefaultListModel model = new DefaultListModel();
//        for (String s : jointNames) {
//            model.addElement(s);
//        }
//        final JList list = new JList(model);
//        final JScrollPane scrollableList = new JScrollPane(list);
////        tempDialog.add(scrollableList);
//
//        smallHbox.add(scrollableList);
//        final String defaultText = "Wprowadz dane";
//        final JTextField textfield = new JTextField(defaultText);
//        smallHbox.add(textfield);
//        textfield.setBackground(Color.darkGray);
//
//        MyButton editButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
//        editButton.setText("edit");
//        smallHbox.add(editButton);
//
//        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
//        addButton.setText("add");
//        addButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                if (testOps.getPerms() == 0) {
//                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
//                } else {
//                    String data = textfield.getText();
//                    if (data == defaultText) {
//                        getErrorDialog("Wpisz jakieœ dane!");
//                    } else {
//                        Connection con = testOps.getConnection();
//                        String QueryADD = null;
//
//                        int lastID = 0;
//                        try {
//                            lastID = (int) (Float.valueOf(testOps.getLastItemInColumn(con, "uzytkownik", "id"))).floatValue();
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                        char lastIDChar = (char) lastID;
//
//
//                        QueryADD = "INSERT INTO uzytkownik VALUES (" + lastID + data + ")";
//
//                        Statement st = null;
//                        try {
//                            st = con.createStatement();
//                            try {
//                                st.executeUpdate(QueryADD);
//                                getErrorDialog("Record addition succesfull");
//                            } catch (SQLException e) {
//                                getErrorDialog("Can't add item");
//                                e.printStackTrace();
//                            }
//                        } catch (SQLException e) {
//                            getErrorDialog("Can't create statement");
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//        });
//        smallHbox.add(addButton);
//
//        MyButton deleteButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
//        deleteButton.setText("delete");
//        deleteButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                if (testOps.getPerms() < 2) {
//                    getErrorDialog("Brak uprawnieñ do usuwania - tylko admin mo¿e to zrobiæ");
//                } else {
//                    String temp = (String) list.getSelectedValue();
//                    String arr[] = temp.split(" ", 2);
//
//                    String firstWord = arr[0];   //the
//                    String theRest = arr[1];     //quick brown fox
//
////                getJustDialog().setVisible(true);
//
//
//                    String QueryDELETE = "DELETE FROM uzytkownik WHERE nazwisko =' " + firstWord + "' AND imie = '" + theRest + "'";
//                    Connection con = testOps.getConnection();
//                    Statement st = null;
//                    try {
//                        st = con.createStatement();
//                        try {
//                            st.executeUpdate(QueryDELETE);
//                            getErrorDialog("Record succesfully deleted");
//                        } catch (SQLException e) {
//                            getErrorDialog("Can't delete item");
//                            e.printStackTrace();
//                        }
//                    } catch (SQLException e) {
//                        getErrorDialog("Can't create statement");
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        smallHbox.add(deleteButton);
//
//        return tempDialog;
//    }

    public JustDialog getJustDialog() {
        return justDialog;
    }

    //------------------------------------------------------------------------------------------
    public JustDialog getPostCodesDialog() throws SQLException {
        final int indexInList;
        int postalCodesAmount = testOps.getColumnSize(testOps.getConnection(), "kod_poczt_1", "kp1_kod");

        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        final JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        tempDialog.add(mainVBox);

//        mainVBox.add(mainVBox.createHorizontalStrut(GlobalSizes.buttonHeight * 2));
        Box smallHbox = Box.createHorizontalBox();
        mainVBox.add(smallHbox);


        //Adds List of post codes
        String[] postCodes = testOps.getColumnData(testOps.getConnection(), "kod_poczt_1", "kp1_kod");
        DefaultListModel model2 = new DefaultListModel();
        for (String s : postCodes) {
            model2.addElement(s);
        }
        final JList list = new JList(model2);
        JScrollPane scrollableList = new JScrollPane(list);
        mainVBox.add(scrollableList);


        final TextField countryShortField = new TextField();
        final TextField postalCodeField = new TextField();
        Box textFieldBox = Box.createHorizontalBox();
        mainVBox.add(textFieldBox);

        Box countryVBox = Box.createVerticalBox();
        countryVBox.add(new JLabel("Skrotowiec kraju"));
        countryVBox.add(countryShortField);

        Box postalCodeVBox = Box.createVerticalBox();
        postalCodeVBox.add(new JLabel("Kod pocztowy"));
        postalCodeVBox.add(postalCodeField);

        textFieldBox.add(countryVBox);
        textFieldBox.add(postalCodeVBox);


        //Adds edition button
        MyButton editButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        editButton.setText("edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goscie nie moga edytowac bazy");
                } else {
                    getCodeAdditionDialog().setVisible(true);
                }
            }
        });
        smallHbox.add(editButton);


        //adds addition button
        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        addButton.setText("add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goscie nie moga edytowac bazy.");
                } else {
                    String temp1 = countryShortField.getText();
                    String temp2 = postalCodeField.getText();

                    Connection con = testOps.getConnection();
                    String QueryADD = null;

                    char lastIDChar = '0';

                    String result = null;
                    try {
                        result = (testOps.getLastItemInColumn(con, "kod_poczt_1", "id"));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }


                    if (result == null) {
                        lastIDChar = '1';
                    } else if (Integer.parseInt(result) >= 0)
                        lastIDChar = (char) (Integer.parseInt(result) + 1);


                    QueryADD = "INSERT INTO kod_poczt_1 VALUES ("
                            + "'" + lastIDChar + "'"
                            + ", '" + temp1
                            + "', '" + temp2 + "')";

                    Statement st = null;


                    //TODO: For some obscure reason, ID is not generated properly
                    try {
                        st = con.createStatement();
                        try {
                            st.executeUpdate(QueryADD);
                            getErrorDialog("Record addition succesfull");
                        } catch (SQLException e) {
                            getErrorDialog("Can't add item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        smallHbox.add(addButton);


        //Adds deletion button
        MyButton deleteButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        deleteButton.setText("delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() != 2) {
                    getErrorDialog("Tylko admin mo¿e usuwaæ zawartoœæ bazy.");
                } else {
                    String temp = (String) list.getSelectedValue();
                    String QueryDELETE = "DELETE FROM kod_poczt_1 WHERE kp1_kod = '" + temp + "'";
                    Connection con = testOps.getConnection();
                    Statement st = null;
                    try {
                        st = con.createStatement();
                        try {
                            st.executeUpdate(QueryDELETE);
                            getErrorDialog("Record succesfully deleted");
                        } catch (SQLException e) {
                            getErrorDialog("Can't delete item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        smallHbox.add(deleteButton);
        return tempDialog;
    }

    //------------------------------------------------------------------------------------------
    public JustDialog getCodeAdditionDialog() {
        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        final JustDialog tempDialog = new JustDialog(appWindow, userRepository);
        tempDialog.add(mainVBox);


        final MyButton saveButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        saveButton.setText("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 2)
                    saveButton.setText("Placeholder for saving");
                else
                    getErrorDialog("Nie masz wystarczaj¹cych uprawnieñ");
            }
        });

        MyButton cancelButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                tempDialog.setVisible(false);
            }
        });

        mainVBox.add(saveButton);
        mainVBox.add(cancelButton);

        return tempDialog;
    }

    //------------------------------------------------------------------------------------------
    public void getErrorDialog(String errorMessage) {
        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.darkGray);

        final JustDialog tempDialog = new JustDialog(appWindow, userRepository);
        tempDialog.add(mainVBox);


        Font font1 = new Font("SansSerif", Font.BOLD, 30);

        JTextField errorField = new JTextField();
        errorField.setText(errorMessage);
        mainVBox.add(errorField);
        tempDialog.setVisible(true);
        errorField.setHorizontalAlignment(JTextField.CENTER);
        errorField.setFont(font1);
        errorField.setEditable(false);
//        errorField.
    }

    //------------------------------------------------------------------------------------------
    public void getCarsDialog() throws SQLException {
        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        Box mainHBox = Box.createHorizontalBox();
        mainVBox.add(mainHBox);

        Box smallVbox = Box.createVerticalBox();
        mainHBox.add(smallVbox);

        final JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        tempDialog.add(mainVBox);

        Connection con1, con2, con3, con4;
        con1 = testOps.getConnection();
        con2 = testOps.getConnection();
        con3 = testOps.getConnection();
        con4 = testOps.getConnection();
        String[] modelArray = testOps.getColumnData(con1, "samochody", "model");
        String[] colourArray = testOps.getColumnData(con2, "samochody", "kolor");
        String[] typeArray = testOps.getColumnData(con3, "samochody", "typ");
        final String[] ids = testOps.getColumnData(con4, "samochody", "id");

        String[] allInfo = new String[modelArray.length];

        for (int i = 0; i < modelArray.length; i++) {
            allInfo[i] = "Typ: " + typeArray[i] +
                    "  kolor: " + colourArray[i] +
                    "  model: " + modelArray[i];
        }


        final DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < modelArray.length; i++) {
            model.addElement(allInfo[i]);
        }

        final JList listOfCars = new JList(model);
        final JScrollPane scrollableList = new JScrollPane(listOfCars);
        smallVbox.add(scrollableList);


        JPanel dataPanel = new JPanel();
        JPanel descriptionPanel = new JPanel();

        mainHBox.add(dataPanel);
        mainHBox.add(descriptionPanel);

        Box dataInputVBox = Box.createVerticalBox();
        Box descriptionVBox = Box.createVerticalBox();

        dataPanel.add(dataInputVBox);
        descriptionPanel.add(descriptionVBox);

        final TextField modelField = new TextField();
        final TextField colourField = new TextField();
        final TextField typeField = new TextField();

        dataInputVBox.add(modelField);
        dataInputVBox.add(colourField);
        dataInputVBox.add(typeField);

        descriptionVBox.add(new JLabel("model"));
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(new JLabel("colour"));
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(new JLabel("type"));

        tempDialog.setVisible(true);

        Box buttonBox = Box.createHorizontalBox();
        mainVBox.add(buttonBox);

        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        addButton.setText("add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else {
                    Connection con = testOps.getConnection();

                    Statement st = null;
                    try {
                        st = con.createStatement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String QueryADD = "INSERT INTO samochody VALUES ('" +
                            typeField.getText() + "', '" +
                            colourField.getText() + "', '" +
                            modelField.getText() + "')";
                    try {
                        st.executeUpdate(QueryADD);
                        getErrorDialog("Dodano samochod");
                        tempDialog.setVisible(false);
                    } catch (SQLException e) {
                        getErrorDialog("Can't add the car");
                        e.printStackTrace();
                    }
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        getErrorDialog("Can't close connection");
                        ex.printStackTrace();
                    }
                }
            }
        });
        buttonBox.add(addButton);

        MyButton deleteButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        deleteButton.setText("delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() < 2) {
                    getErrorDialog("Tylko admin moze ususwac!");
                } else {
                    Connection con = testOps.getConnection();

                    Statement st = null;
                    try {
                        st = con.createStatement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String QueryDELETE = "DELETE FROM samochody WHERE id = " + testOps.getID(listOfCars.getMinSelectionIndex(), ids);
                    ids[listOfCars.getMinSelectionIndex()] = null;
                    try {
                        st.executeUpdate(QueryDELETE);
                        getErrorDialog("Udane ususniêcie");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                tempDialog.setVisible(false);
            }
        });
        buttonBox.add(deleteButton);

        con1.close();
        con2.close();
        con3.close();
        con4.close();
    }

    //------------------------------------------------------------------------------------------
    public void getPaymentDialog() throws SQLException {

        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        Box mainHBox = Box.createHorizontalBox();
        mainVBox.add(mainHBox);

        Box smallVbox = Box.createVerticalBox();
        mainHBox.add(smallVbox);

        final JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        tempDialog.add(mainVBox);

        String[] baseValues = testOps.getColumnData(testOps.getConnection(), "detaleOplat", "kwotaBazowa");
        String[] multipliers = testOps.getColumnData(testOps.getConnection(), "detaleOplat", "przelicznik");
        String[] kontrahents = testOps.getColumnData(testOps.getConnection(), "detaleOplat", "id_kontrahenta");
        String[] clients = testOps.getColumnData(testOps.getConnection(), "detaleOplat", "id_uzytkownika");

        String[] allInfo = new String[baseValues.length];


        float[] realValues = new float[baseValues.length];

        for (int i = 0; i < baseValues.length; i++) {
            realValues[i] = Integer.parseInt(baseValues[i]) * Float.parseFloat(multipliers[i]);
        }


        for (int i = 0; i < baseValues.length; i++) {
            allInfo[i] = "kontrahent: " + kontrahents[i] +
                    "  uzytkownik: " + clients[i] +
                    " przelicznik: " + multipliers[i] +
                    " cena bazowa: " + baseValues[i] +
                    " cena koncowa: " + realValues[i];
        }


        final DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < baseValues.length; i++) {
            model.addElement(allInfo[i]);
        }

        final JList listOfPayments = new JList(model);
        final JScrollPane scrollableList = new JScrollPane(listOfPayments);
        smallVbox.add(scrollableList);


        final String defaultText = "Wprowadz dane";
        final JTextField textfield = new JTextField(defaultText);

        JPanel dataPanel = new JPanel();
        JPanel descriptionPanel = new JPanel();

        mainHBox.add(dataPanel);
        mainHBox.add(descriptionPanel);

        Box dataInputVBox = Box.createVerticalBox();
        Box descriptionVBox = Box.createVerticalBox();

        dataPanel.add(dataInputVBox);
        descriptionPanel.add(descriptionVBox);

        //id_kontrahenta, przelicznik, id_uzytownika, kwotaBazowa

        final TextField kontrahentField = new TextField();
        final TextField przelicznikField = new TextField();
        final TextField userNameField = new TextField();
        final TextField userSurnameField = new TextField();
        final TextField kwotaField = new TextField();

        dataInputVBox.add(kontrahentField);
        dataInputVBox.add(przelicznikField);
        dataInputVBox.add(userNameField);
        dataInputVBox.add(userSurnameField);
        dataInputVBox.add(kwotaField);


        JLabel kontrahent = new JLabel("id_kontrahenta");
        JLabel przelicznik = new JLabel("przelicznik");
        JLabel uzytkownikName = new JLabel("imie uzytkownika");
        JLabel uzytkownikSurname = new JLabel("nazwisko uzytkownika");
        JLabel kwota = new JLabel("kwota podstawowa");

        descriptionVBox.add(kontrahent);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(przelicznik);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(uzytkownikName);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(uzytkownikSurname);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(kwota);


        MyButton editButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        editButton.setText("edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else {
                    Connection con = testOps.getConnection();
                    ResultSet rs0 = null;
                    Statement st0 = null;
                    try {
                        st0 = con.createStatement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String userID = null;
                    String QueryGETUSERID = "SELECT id FROM uzytkownik WHERE imie = '" + userNameField.getText()
                            + "' AND nazwisko = '" + userSurnameField.getText() + "'";
                    try {
                        rs0 = st0.executeQuery(QueryGETUSERID);
//                        if()
                        userID = rs0.getString("id");
                    } catch (SQLException e) {
                        getErrorDialog("Can't get user_id");
                        e.printStackTrace();
                    }
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        getErrorDialog("Can't close connection");
                        ex.printStackTrace();
                    }


                    String QueryEDIT = "UPDATE detaleOplat SET przelicznik = " + przelicznikField.getText() +
                            ",  id_uzytkownika = " + userID +
                            ", kwotaBazowa = " + kwotaField.getText() +
                            "WHERE kontrahent_id = " + kontrahentField.getText();
                    Statement st = null;
                    Connection con2 = testOps.getConnection();
                    try {
                        st = con2.createStatement();
                        try {
                            st.executeUpdate(QueryEDIT);
                            getErrorDialog("Record edition succesfull");
                        } catch (SQLException e) {
                            getErrorDialog("Can't edit item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                    try {
                        con2.close();
                    } catch (SQLException e) {
                        getErrorDialog("Can't close connection");
                        e.printStackTrace();
                    }
                }
            }

        });
        smallVbox.add(editButton);

        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        addButton.setText("add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else {
                    Connection con = testOps.getConnection();
                    String QueryADD = "INSERT INTO detaleOplat VALUES ('" + kontrahentField.getText() + "', '" +
                            przelicznikField.getText() + "', " +
                            "(SELECT id FROM uzytkownik WHERE imie = '" + userNameField.getText() +
                            "' AND nazwisko = '" + userSurnameField.getText() + "'), " +
                            kwotaField.getText() + ")";

                    Statement st = null;
                    try {
                        st = con.createStatement();
                        try {
                            st.executeUpdate(QueryADD);
                            getErrorDialog("Record addition succesfull");
                        } catch (SQLException e) {
                            getErrorDialog("Can't add item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                }
            }

        });
        smallVbox.add(addButton);


        MyButton deleteButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        deleteButton.setText("delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

                if (testOps.getPerms() < 2) {
                    getErrorDialog("Tylko admin moze ususwac!");
                } else {

//                    getErrorDialog(listOfPayments.getSelectedIndex() + "");
                    Connection con = testOps.getConnection();

                    String QueryDELETE = "DELETE FROM detaleOplat WHERE id = " + (listOfPayments.getSelectedIndex() + 1);

                    Statement st = null;
                    try {
                        st = con.createStatement();
                        try {
                            st.executeUpdate(QueryDELETE);
                            getErrorDialog("Record deletion succesfull");
                            listOfPayments.ensureIndexIsVisible(model.getSize());


                        } catch (SQLException e) {
                            getErrorDialog("Can't delete item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        smallVbox.add(deleteButton);


        tempDialog.setVisible(true);
    }

    //------------------------------------------------------------------------------------------
    public void getClientsDialog() throws SQLException {

        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        Box mainHBox = Box.createHorizontalBox();
        mainVBox.add(mainHBox);

        Box smallVbox = Box.createVerticalBox();
        mainHBox.add(smallVbox);

        final JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        tempDialog.add(mainVBox);

        String[] deleted = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "skasowany");
        String[] names = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "imie");
        String[] surnames = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "nazwisko");
        String[] logins = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "login");
        String[] passwords = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "haslo");
        String[] group_ids = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "grupa_id");
        String[] device_ids = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "urzadzenie_id");
        String[] userGroup_ids = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "grupauzytkownikow_id");
        String[] speditors = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "spedytor");

        String[] allInfo = new String[deleted.length];


        for (int i = 0; i < deleted.length; i++) {
            allInfo[i] = "usuniety: " + deleted[i] +
                    "  imie: " + names[i] +
                    " nazwisko: " + surnames[i] +
                    " nazwisko: " + surnames[i] +
                    " grupa_id: " + group_ids[i] +
                    " urzadzenie: " + device_ids[i] +
                    " id_grupy: " + userGroup_ids[i] +
                    " spedytor: " + speditors[i];
        }


        final DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < deleted.length; i++) {
            model.addElement(allInfo[i]);
        }

        final JList listOfPayments = new JList(model);
        final JScrollPane scrollableList = new JScrollPane(listOfPayments);
        smallVbox.add(scrollableList);


        final String defaultText = "Wprowadz dane";
        final JTextField textfield = new JTextField(defaultText);

        JPanel dataPanel = new JPanel();
        JPanel descriptionPanel = new JPanel();

        mainHBox.add(dataPanel);
        mainHBox.add(descriptionPanel);

        Box dataInputVBox = Box.createVerticalBox();
        Box descriptionVBox = Box.createVerticalBox();

        dataPanel.add(dataInputVBox);
        descriptionPanel.add(descriptionVBox);

        //id_kontrahenta, przelicznik, id_uzytownika, kwotaBazowa

        final TextField deletedField = new TextField();
        final TextField nameField = new TextField();
        final TextField surnameField = new TextField();
        final TextField loginField = new TextField();
        final TextField passwordField = new TextField();
        final TextField groupIdField = new TextField();
        final TextField deviceIdField = new TextField();
        final TextField userGroupField = new TextField();
        final TextField speditorField = new TextField();

        dataInputVBox.add(deletedField);
        dataInputVBox.add(nameField);
        dataInputVBox.add(surnameField);
        dataInputVBox.add(loginField);
        dataInputVBox.add(passwordField);
        dataInputVBox.add(groupIdField);
        dataInputVBox.add(deviceIdField);
        dataInputVBox.add(userGroupField);
        dataInputVBox.add(speditorField);


        JLabel usuniety = new JLabel("czy jest usuniety");
        JLabel imie = new JLabel("imie uzytkownika");
        JLabel nazwisko = new JLabel("nazwisko uzytkownika");
        JLabel login = new JLabel("login");
        JLabel haslo = new JLabel("haslo");
        JLabel id_grupy = new JLabel("id grupy");
        JLabel id_urzadzenia = new JLabel("id urzadzenia");
        JLabel grupa_uzytkownika = new JLabel("grupa uzytkownikow");
        JLabel spedytor = new JLabel("czy jest spedytorem");

        descriptionVBox.add(usuniety);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(imie);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(nazwisko);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(login);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(haslo);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(id_grupy);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(id_urzadzenia);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(grupa_uzytkownika);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(spedytor);


        MyButton editButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        editButton.setText("edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else {/*
                    Connection con = testOps.getConnection();
                    ResultSet rs0 = null;
                    Statement st0 = null;
                    try {
                        st0 = con.createStatement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String userID = null;
                    String QueryGETUSERID = "SELECT id FROM uzytkownik WHERE imie = '" + userNameField.getText()
                            + "' AND nazwisko = '" + userSurnameField.getText() + "'";
                    try {
                        rs0 = st0.executeQuery(QueryGETUSERID);
//                        if()
                        userID = rs0.getString("id");
                    } catch (SQLException e) {
                        getErrorDialog("Can't get user_id");
                        e.printStackTrace();
                    }
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        getErrorDialog("Can't close connection");
                        ex.printStackTrace();
                    }


                    String QueryEDIT = "UPDATE detaleOplat SET przelicznik = " + przelicznikField.getText() +
                            ",  id_uzytkownika = " + userID +
                            ", kwotaBazowa = " + kwotaField.getText() +
                            "WHERE kontrahent_id = " + kontrahentField.getText();
                    Statement st = null;
                    Connection con2 = testOps.getConnection();
                    try {
                        st = con2.createStatement();
                        try {
                            st.executeUpdate(QueryEDIT);
                            getErrorDialog("Record edition succesfull");
                        } catch (SQLException e) {
                            getErrorDialog("Can't edit item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                    try {
                        con2.close();
                    } catch (SQLException e) {
                        getErrorDialog("Can't close connection");
                        e.printStackTrace();
                    }
                }*/
                }
            }

        });
        smallVbox.add(editButton);

        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        addButton.setText("add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else {
                    Connection con = testOps.getConnection();
                    String QueryADD = "INSERT INTO uzytkownik VALUES ('" + deletedField.getText() + "', '" +
                            nameField.getText() + "', '" +
                            surnameField.getText() + "', '" +
                            loginField.getText() + "', '" +
                            passwordField.getText() + "', '" +
                            groupIdField.getText() + "', '" +
                            deviceIdField.getText() + "', '" +
                            userGroupField.getText() + "', '" +
                            speditorField.getText() + "')";

                    Statement st = null;
                    try {
                        st = con.createStatement();
                        try {
                            st.executeUpdate(QueryADD);
                            getErrorDialog("Record addition succesfull");
                        } catch (SQLException e) {
                            getErrorDialog("Can't add item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                }
            }

        });
        smallVbox.add(addButton);


        MyButton deleteButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        deleteButton.setText("delete");
        /*
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

                if (testOps.getPerms() < 2) {
                    if (testOps.getPerms() < 2) {
                        getErrorDialog("Tylko admin moze ususwac!");
                    } else {

                        Connection con = testOps.getConnection();

                        String QueryREMOVEREFERENCES = "DELETE FROM kontakt_do_kontrahenta WHERE uzytkownik_id = " + (listOfPayments.getMinSelectionIndex() + 1) +
                                "DELETE FROM detaleOplat WHERE id_uzytkownika = " + (listOfPayments.getMinSelectionIndex() + 1);
                        String QueryDELETE = "DELETE FROM uzytkownik WHERE id = " + (listOfPayments.getSelectedIndex() + 1);

                        Statement st = null;
                        try {
                            st = con.createStatement();
                            try {
                                st.executeUpdate(QueryREMOVEREFERENCES);
                                getErrorDialog("Reference deletion succesfull");
                                listOfPayments.ensureIndexIsVisible(model.getSize());
                            } catch (SQLException e) {
                                getErrorDialog("Can't delete references");
                                e.printStackTrace();
                            }


                            try {
                                st.executeUpdate(QueryDELETE);
                                listOfPayments.ensureIndexIsVisible(model.getSize());
                                getErrorDialog("Record deletion succesfull");
                                tempDialog.setVisible(false);
                            } catch (SQLException e) {
                                getErrorDialog("Can't delete item");
                                e.printStackTrace();
                            }
                        } catch (SQLException e) {
                            getErrorDialog("Can't create statement");
                            e.printStackTrace();
                        }
                        try {
                            con.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
        smallVbox.add(deleteButton);*/


        tempDialog.setVisible(true);
    }

    //------------------------------------------------------------------------------------------
    public void getUsersCarsDialog() throws SQLException {/*
        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        Box mainHBox = Box.createHorizontalBox();
        mainVBox.add(mainHBox);

        Box smallVbox = Box.createVerticalBox();
        mainHBox.add(smallVbox);

        final JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        tempDialog.add(mainVBox);

        Connection con1, con2, con3, con4, con5, con6, con7, con8;
        con1 = testOps.getConnection();
        con2 = testOps.getConnection();
        con3 = testOps.getConnection();
        con4 = testOps.getConnection();
        con5 = testOps.getConnection();
        con6 = testOps.getConnection();
        con7 = testOps.getConnection();
        con8 = testOps.getConnection();

        String[] carIdArray = testOps.getColumnData(con1, "samochodyUzytkownikow", "id_samochodu");
        String[] userIdsArray = testOps.getColumnData(con2, "samochodyUzytkownikow", "id_uzytkownika");
        final String[] yearsArray = testOps.getColumnData(con3, "samochodyUzytkownikow", "lataPosiadania");
        final String[] accidentArray = testOps.getColumnData(con4, "samochodyUzytkownikow", "wypadkowy");
        String[] modelArray = testOps.getColumnData(con5, "samochody", "model");
        String[] colourArray = testOps.getColumnData(con6, "samochody", "kolor");
        String[] namesArray = testOps.getColumnData(con6, "uzytkownik", "imie");
        String[] surnamesArray = testOps.getColumnData(con6, "uzytkownik", "nazwisko");
        String[] users = new String[namesArray.length];

        for (int i = 0; i < namesArray.length; i++) {
            users[i] = namesArray[i] + " " + surnamesArray[i];
        }

        final String[] wypadkowyTlumaczony = new String[accidentArray.length];
        for (int i = 0; i < accidentArray.length; i++) {
            if (accidentArray[i] == "1") {
                wypadkowyTlumaczony[i] = "Tak";
            } else
                wypadkowyTlumaczony[i] = "Nie";
        }

        String[] allInfo = new String[carIdArray.length];

        for (int i = 0; i < carIdArray.length; i++) {
            allInfo[i] = "Wlasciciel: " + users[Integer.parseInt(userIdsArray[i])] +
                    ",  posiadany przez lat: " + yearsArray[i] +
                    ",  samochod: " + modelArray[i] + " " + colourArray[i] +
                    ", wypadkowy: " + wypadkowyTlumaczony[i];
        }


        final DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < modelArray.length; i++) {
            model.addElement(allInfo[i]);
        }

        final JList listOfUserCars = new JList(model);
        final JScrollPane scrollableList = new JScrollPane(listOfUserCars);
        smallVbox.add(scrollableList);


        JPanel dataPanel = new JPanel();
        JPanel descriptionPanel = new JPanel();

        mainHBox.add(dataPanel);
        mainHBox.add(descriptionPanel);

        Box dataInputVBox = Box.createVerticalBox();
        Box descriptionVBox = Box.createVerticalBox();

        dataPanel.add(dataInputVBox);
        descriptionPanel.add(descriptionVBox);

        final TextField modelField = new TextField();
        final TextField colourField = new TextField();
        final TextField nameField = new TextField();
        final TextField surnameField = new TextField();
        final TextField yearsField = new TextField();
        final TextField accidentField = new TextField();

        dataInputVBox.add(modelField);
        dataInputVBox.add(colourField);
        dataInputVBox.add(nameField);
        dataInputVBox.add(surnameField);
        dataInputVBox.add(yearsField);
        dataInputVBox.add(accidentField);

        descriptionVBox.add(new JLabel("model"));
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(new JLabel("colour"));
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(new JLabel("users name"));
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(new JLabel("users surname"));
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(new JLabel("Years owned:"));
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(new JLabel("Was in accident:"));

        Box buttonBox = Box.createHorizontalBox();
        mainVBox.add(buttonBox);

        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        addButton.setText("add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else {
                    Connection con1 = testOps.getConnection();
                    Connection con0 = testOps.getConnection();
                    Connection con = testOps.getConnection();

                    ResultSet rs = null;
                    ResultSet rs0 = null;
                    Statement st0 = null;
                    Statement st1 = null;
                    try {
                        st0 = con0.createStatement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String QueryGETCARID = "SELECT id FROM samochody WHERE kolor = '" +
                            colourField.getText() + "' AND model = '" +
                            modelField.getText() + "'";
                    try {
                        rs = st0.executeQuery(QueryGETCARID);
                    } catch (SQLException e) {
                        getErrorDialog("Can't get car ID");
                        e.printStackTrace();
                    }
                    try {
                        con0.close();
                    } catch (SQLException ex) {
                        getErrorDialog("Can't close connection");
                        ex.printStackTrace();
                    }


                    try {
                        st1 = con1.createStatement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String QueryGETUSERID = "SELECT id FROM uzytkownicy WHERE imie = '" +
                            nameField.getText() + "' AND nazwisko = '" +
                            surnameField.getText() + "'";
                    try {
                        rs0 = st1.executeQuery(QueryGETUSERID);
                    } catch (SQLException e) {
                        getErrorDialog("Can't get user ID");
                        e.printStackTrace();
                    }
                    try {
                        con1.close();
                    } catch (SQLException ex) {
                        getErrorDialog("Can't close connection");
                        ex.printStackTrace();
                    }


                    String id = null;
                    String userId = null;

                    Statement st = null;
                    try {
                        st = con.createStatement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String QueryADD = null;
                    try {

                        id = rs.getString("id");
                        userId = rs0.getString("id");

                        QueryADD = "INSERT INTO samochodyUzytkownikow VALUES ('" +
                                id + "', '" +
                                userId + "', '" +
                                yearsField.getText() + "', '" +
                                wypadkowyTlumaczony[Integer.parseInt(id)] + "')";
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        st.executeUpdate(QueryADD);
                        getErrorDialog("Dodano samochod");
                        tempDialog.setVisible(false);
                    } catch (SQLException e) {
                        getErrorDialog("Can't add the car");
                        e.printStackTrace();
                    }
                    try {
                        con.close();
                    } catch (SQLException ex) {
                        getErrorDialog("Can't close connection");
                        ex.printStackTrace();
                    }
                }
            }
        });
        buttonBox.add(addButton);

        MyButton deleteButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        deleteButton.setText("delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Connection con0 = testOps.getConnection();
                String[] ids = new String[0];
                try {
                    ids = testOps.getColumnData(con0, "samochodyUzytkownikow", "id");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    con0.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (testOps.getPerms() < 2) {
                    getErrorDialog("Tylko admin moze ususwac!");
                } else {
                    Connection con = testOps.getConnection();

                    Statement st = null;
                    try {
                        st = con.createStatement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String QueryDELETE = "DELETE FROM samochodyUztywkonikow WHERE id = " + testOps.getID(listOfUserCars.getMinSelectionIndex(), ids);
                    ids[listOfUserCars.getMinSelectionIndex()] = null;
                    try {
                        st.executeUpdate(QueryDELETE);
                        getErrorDialog("Udane ususniêcie");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                tempDialog.setVisible(false);
            }
        });
        buttonBox.add(deleteButton);


        tempDialog.setVisible(true);
        con1.close();
        con2.close();
        con3.close();
        con4.close();
        con5.close();
        con6.close();*/
    }

    //------------------------------------------------------------------------------------------
    public void getRefactorsDialog() throws SQLException {

        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        Box mainHBox = Box.createHorizontalBox();
        mainVBox.add(mainHBox);

        Box smallVbox = Box.createVerticalBox();
        mainHBox.add(smallVbox);

        final JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        tempDialog.add(mainVBox);

        String[] nazwaArray = testOps.getColumnData(testOps.getConnection(), "przelicznikiOplat", "nazwa");
        final String[] cena1Array = testOps.getColumnData(testOps.getConnection(), "przelicznikiOplat", "cena1");
        String[] cena2Array = testOps.getColumnData(testOps.getConnection(), "przelicznikiOplat", "cena2");
        String[] cena3Array = testOps.getColumnData(testOps.getConnection(), "przelicznikiOplat", "cena3");

        String[] allInfo = new String[nazwaArray.length];


        for (int i = 0; i < nazwaArray.length; i++) {
            allInfo[i] = "nazwa: " + nazwaArray[i] +
                    "| usluga1: " + cena1Array[i] +
                    "| usluga2: " + cena2Array[i] +
                    "| usluga3: " + cena3Array[i];
        }


        final DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < nazwaArray.length; i++) {
            model.addElement(allInfo[i]);
        }

        final JList listOfPayments = new JList(model);
        final JScrollPane scrollableList = new JScrollPane(listOfPayments);
        smallVbox.add(scrollableList);


        final String defaultText = "Wprowadz dane";

        JPanel dataPanel = new JPanel();
        JPanel descriptionPanel = new JPanel();

        mainHBox.add(dataPanel);
        mainHBox.add(descriptionPanel);

        Box dataInputVBox = Box.createVerticalBox();
        Box descriptionVBox = Box.createVerticalBox();

        dataPanel.add(dataInputVBox);
        descriptionPanel.add(descriptionVBox);

        //nazwa, cena1, cena2, cena3

        final TextField nazwaField = new TextField();
        final TextField cena1Field = new TextField();
        final TextField cena2Field = new TextField();
        final TextField cena3Field = new TextField();

        dataInputVBox.add(nazwaField);
        dataInputVBox.add(cena1Field);
        dataInputVBox.add(cena2Field);
        dataInputVBox.add(cena3Field);


        JLabel kontrahent = new JLabel("nazwa modelu oplat");
        JLabel przelicznik = new JLabel("cena usulugi1");
        JLabel uzytkownikName = new JLabel("cena usulugi2");
        JLabel uzytkownikSurname = new JLabel("cena usulugi3");

        descriptionVBox.add(kontrahent);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(przelicznik);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(uzytkownikName);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(uzytkownikSurname);


        MyButton editButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        editButton.setText("edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else {
                    Connection con = testOps.getConnection();
                    ResultSet rs0 = null;
                    Statement st0 = null;


                    String QueryEDIT = "UPDATE przelicznikiOplat SET " +
                            "cena1 = " + cena1Field.getText() +
                            ", cena3 = " + cena2Field.getText() +
                            ", cena2 = " + cena3Field.getText() +
                            "WHERE nazwa = '" + nazwaField.getText() + "'";
                    Statement st = null;
                    Connection con2 = testOps.getConnection();
                    try {
                        st = con2.createStatement();
                        try {
                            st.executeUpdate(QueryEDIT);
                            getErrorDialog("Record edition succesfull");
                        } catch (SQLException e) {
                            getErrorDialog("Can't edit item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                    try {
                        con2.close();
                    } catch (SQLException e) {
                        getErrorDialog("Can't close connection");
                        e.printStackTrace();
                    }
                }
            }

        });
        smallVbox.add(editButton);

        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        addButton.setText("add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else {
                    Connection con = testOps.getConnection();
                    String QueryADD = "INSERT INTO przelicznikiOplat VALUES ('" + nazwaField.getText() + "', '" +
                            cena1Field.getText() + "', '" +
                            cena2Field.getText() + "', '" +
                            cena3Field.getText() + "')";

                    Statement st = null;
                    try {
                        st = con.createStatement();
                        try {
                            st.executeUpdate(QueryADD);
                            getErrorDialog("Record addition succesfull");
                        } catch (SQLException e) {
                            getErrorDialog("Can't add item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                }
                tempDialog.setVisible(false);
            }

        });
        smallVbox.add(addButton);


        MyButton deleteButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        deleteButton.setText("delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

                if (testOps.getPerms() < 2) {
                    getErrorDialog("Tylko admin moze ususwac!");
                } else {

//                    getErrorDialog(listOfPayments.getSelectedIndex() + "");
                    Connection con = testOps.getConnection();

                    String QueryDELETE = "DELETE FROM przelicznikiOplat WHERE nazwa = '" + nazwaField.getText() + "'";

                    Statement st = null;
                    try {
                        st = con.createStatement();
                        try {
                            st.executeUpdate(QueryDELETE);
                            getErrorDialog("Record deletion succesfull");
                            listOfPayments.ensureIndexIsVisible(model.getSize());


                        } catch (SQLException e) {
                            getErrorDialog("Can't delete item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        smallVbox.add(deleteButton);


        tempDialog.setVisible(true);
    }

    //------------------------------------------------------------------------------------------
    public void getDriversDialog() throws SQLException {

        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        Box mainHBox = Box.createHorizontalBox();
        mainVBox.add(mainHBox);

        Box smallVbox = Box.createVerticalBox();
        mainHBox.add(smallVbox);

        final JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        tempDialog.add(mainVBox);

        String[] imieArray = testOps.getColumnData(testOps.getConnection(), "kierowcy", "imie");
        String[] nazwiskoArray = testOps.getColumnData(testOps.getConnection(), "kierowcy", "nazwisko");
        String[] samochodIdArray = testOps.getColumnData(testOps.getConnection(), "kierowcy", "samochod_id");

        String[] samochodModelArray = testOps.getColumnData(testOps.getConnection(), "samochody", "model");
        String[] samochodKolorArray = testOps.getColumnData(testOps.getConnection(), "samochody", "kolor");
        String[] allInfo = new String[imieArray.length];


        int tempIndex = 0;
        for (int i = 0; i < imieArray.length; i++) {
            tempIndex = Integer.parseInt(samochodIdArray[i]);
            allInfo[i] = "imie: " + imieArray[i] +
                    "| nazwisko: " + nazwiskoArray[i] +
                    "| samochod: " + samochodModelArray[i] + " " + samochodKolorArray[tempIndex];
        }


        final DefaultListModel model = new DefaultListModel();
        for (int i = 0; i < allInfo.length; i++) {
            model.addElement(allInfo[i]);
        }

        final JList listOfPayments = new JList(model);
        final JScrollPane scrollableList = new JScrollPane(listOfPayments);
        smallVbox.add(scrollableList);


        final String defaultText = "Wprowadz dane";

        JPanel dataPanel = new JPanel();
        JPanel descriptionPanel = new JPanel();

        mainHBox.add(dataPanel);
        mainHBox.add(descriptionPanel);

        Box dataInputVBox = Box.createVerticalBox();
        Box descriptionVBox = Box.createVerticalBox();

        dataPanel.add(dataInputVBox);
        descriptionPanel.add(descriptionVBox);

        //nazwa, cena1, cena2, cena3

        final TextField imieField = new TextField();
        final TextField nazwiskoField = new TextField();
        final TextField samochodIDField = new TextField();
//        final TextField cena3Field = new TextField();

        dataInputVBox.add(imieField);
        dataInputVBox.add(nazwiskoField);
        dataInputVBox.add(samochodIDField);


        JLabel kontrahent = new JLabel("imie kierwocy");
        JLabel przelicznik = new JLabel("nazwisko kierowcy");
        JLabel uzytkownikName = new JLabel("rejestracja");

        descriptionVBox.add(kontrahent);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(przelicznik);
        descriptionVBox.add(Box.createVerticalStrut(10));
        descriptionVBox.add(uzytkownikName);


        MyButton editButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        editButton.setText("edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else {
                    Connection con = testOps.getConnection();
                    ResultSet rs0 = null;
                    Statement st0 = null;


                    String QueryEDIT = "UPDATE kierowcy SET " +
                            "imie = '" + imieField.getText() +
                            "', nazwisko = '" + nazwiskoField.getText() +
                            "' WHERE id = '" + samochodIDField.getText() + "'";
                    Statement st = null;
                    Connection con2 = testOps.getConnection();
                    try {
                        st = con2.createStatement();
                        try {
                            st.executeUpdate(QueryEDIT);
                            getErrorDialog("Record edition succesfull");
                        } catch (SQLException e) {
                            getErrorDialog("Can't edit item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                    try {
                        con2.close();
                    } catch (SQLException e) {
                        getErrorDialog("Can't close connection");
                        e.printStackTrace();
                    }
                }
            }

        });
        smallVbox.add(editButton);

        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        addButton.setText("add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else {
                    Connection con = testOps.getConnection();
                    String QueryADD = "INSERT INTO kierowcy VALUES ('" + imieField.getText() + "', '" +
                            nazwiskoField.getText() + "', '" +
                            samochodIDField.getText() + "')";

                    Statement st = null;
                    try {
                        st = con.createStatement();
                        try {
                            st.executeUpdate(QueryADD);
                            getErrorDialog("Record addition succesfull");
                        } catch (SQLException e) {
                            getErrorDialog("Can't add item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                }
                tempDialog.setVisible(false);
            }

        });
        smallVbox.add(addButton);


        MyButton deleteButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        deleteButton.setText("delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                Connection con4 = testOps.getConnection();
                String[] ids = null;
                try {
                    ids = testOps.getColumnData(con4, "samochody", "id");
                } catch (SQLException e) {
                    e.printStackTrace();
                }


                if (testOps.getPerms() < 2) {
                    getErrorDialog("Tylko admin moze ususwac!");
                } else {

//                    getErrorDialog(listOfPayments.getSelectedIndex() + "");
                    Connection con = testOps.getConnection();

                    String QueryDELETE = "DELETE FROM kierowcy WHERE id = '" + testOps.getID(listOfPayments.getSelectedIndex(), ids) + "'";
                    ids[listOfPayments.getMinSelectionIndex()] = null;


                    Statement st = null;
                    try {
                        st = con.createStatement();
                        try {
                            st.executeUpdate(QueryDELETE);
                            getErrorDialog("Record deletion succesfull");
                            listOfPayments.ensureIndexIsVisible(model.getSize());


                        } catch (SQLException e) {
                            getErrorDialog("Can't delete item");
                            e.printStackTrace();
                        }
                    } catch (SQLException e) {
                        getErrorDialog("Can't create statement");
                        e.printStackTrace();
                    }
                    try {
                        con.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }

        });
        smallVbox.add(deleteButton);


        tempDialog.setVisible(true);
    }
}
