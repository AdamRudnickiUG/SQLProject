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
}
