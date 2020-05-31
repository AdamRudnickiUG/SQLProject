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
    AppWindow appWindow = new AppWindow(testOps, this);
    //-------------------------------------------------------------

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

    // GETTERS
    public AppWindow getAppWindow() {
        return appWindow;
    }

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


        final TextField textFieldLeft = new TextField();
        final TextField textFieldRight = new TextField();
        Box smallerHbox = Box.createHorizontalBox();
        mainVBox.add(smallerHbox);
        smallerHbox.add(textFieldLeft);
        smallerHbox.add(textFieldRight);


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
                    String temp1 = textFieldLeft.getText();
                    String temp2 = textFieldRight.getText();

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
                }
            }
        });
        smallHbox.add(deleteButton);
        return tempDialog;
    }


    //------------------------------------------------------------------------------------------
    public JustDialog getClientListDialog() throws SQLException {
        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        Box smallHbox = Box.createHorizontalBox();
        mainVBox.add(smallHbox);

        JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        tempDialog.add(mainVBox);

        String[] names = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "imie");
        String[] surnames = testOps.getColumnData(testOps.getConnection(), "uzytkownik", "nazwisko");

        String[] jointNames = new String[names.length];
        for (int i = 0; i < names.length; i++) {
            jointNames[i] = surnames[i] + " " + names[i];
        }


        DefaultListModel model = new DefaultListModel();
        for (String s : jointNames) {
            model.addElement(s);
        }
        final JList list = new JList(model);
        final JScrollPane scrollableList = new JScrollPane(list);
//        tempDialog.add(scrollableList);

        smallHbox.add(scrollableList);
        final String defaultText = "Wprowadz dane";
        final JTextField textfield = new JTextField(defaultText);
        smallHbox.add(textfield);
        textfield.setBackground(Color.darkGray);

        MyButton editButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        editButton.setText("edit");
        smallHbox.add(editButton);

        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        addButton.setText("add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() == 0) {
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else {
                    String data = textfield.getText();
                    if (data == defaultText) {
                        getErrorDialog("Wpisz jakieœ dane!");
                    } else {
                        Connection con = testOps.getConnection();
                        String QueryADD = null;

                        int lastID = 0;
                        try {
                            lastID = (int) (Float.valueOf(testOps.getLastItemInColumn(con, "uzytkownik", "id"))).floatValue();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        char lastIDChar = (char) lastID;


                        QueryADD = "INSERT INTO uzytkownik VALUES (" + lastID + data + ")";

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
            }
        });
        smallHbox.add(addButton);

        MyButton deleteButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        deleteButton.setText("delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (testOps.getPerms() < 2) {
                    getErrorDialog("Brak uprawnieñ do usuwania - tylko admin mo¿e to zrobiæ");
                } else {
                    String temp = (String) list.getSelectedValue();
                    String arr[] = temp.split(" ", 2);

                    String firstWord = arr[0];   //the
                    String theRest = arr[1];     //quick brown fox

//                getJustDialog().setVisible(true);


                    String QueryDELETE = "DELETE FROM uzytkownik WHERE nazwisko =' " + firstWord + "' AND imie = '" + theRest + "'";
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

        String[] modelArray = testOps.getColumnData(testOps.getConnection(), "samochody", "model");
        String[] colourArray = testOps.getColumnData(testOps.getConnection(), "samochody", "kolor");
        String[] typeArray = testOps.getColumnData(testOps.getConnection(), "samochody", "typ");
        String[] ids = testOps.getColumnData(testOps.getConnection(), "samochody", "id");

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
                    getErrorDialog("Goœcie nie mog¹ zmieniaæ bazy!");
                } else {
                    Connection con = testOps.getConnection();

                    Statement st = null;
                    try {
                        st = con.createStatement();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    String QueryDELETE = "DELETE FROM samochody WHERE id = " + listOfCars.getMinSelectionIndex() + 1;
                    //TODO: Remove items by their id (from String table ids) rather than like now
                    try {
                        st.executeUpdate(QueryDELETE);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
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
}
