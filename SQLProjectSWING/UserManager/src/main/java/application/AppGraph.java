package application;

import DBOperations.TestOps;
import _throwThisAway.JustDialog;
import model.Database;
import model.UserRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


public class AppGraph {

    // APP GRAPH SINGLETON
    private static AppGraph instance = null;
    TestOps testOps = new TestOps();
    // APPLICATION OBJECTS DI
    Database database = new Database();
    UserRepository userRepository = new UserRepository(database);
    AppWindow appWindow = new AppWindow();
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

    public JustDialog getPostCodesDialog() throws SQLException {
        int postalCodesAmount = testOps.getColumnSize(testOps.getConnection(), "kod_poczt_1", "kp1_kod");

        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        tempDialog.add(mainVBox);

//        mainVBox.add(mainVBox.createHorizontalStrut(GlobalSizes.buttonHeight * 2));
        Box smallHbox = Box.createHorizontalBox();
        mainVBox.add(smallHbox);

        JScrollPane scrollPane = new JScrollPane();
        Font fontBig = new Font("SansSerif", Font.BOLD, 20);
        String[] postCodes = testOps.getColumnData(testOps.getConnection(), "kod_poczt_1", "kp1_kod");
        String[] header = new String[]{"kod_poczt_1"};

        //TODO: https://stackoverflow.com/questions/28128035/how-to-add-table-header-and-scrollbar-in-jtable-java Make this work!!!
        DefaultTableModel model = new DefaultTableModel(postCodes, header);

        //TODO: Does not display values for some reason
        JTable table = new JTable();
        JLabel[] areas = new JLabel[postalCodesAmount];
        for (int i = 0; i < postalCodesAmount; i++) {
            areas[i] = new JLabel();
            areas[i].setFont(fontBig);
            areas[i].setText(postCodes[i]);
            areas[i].setSize(10, 10);
//            scrollPane.add(areas[i]);
            table.add(areas[i]);
//            System.out.println(areas[i].getText());
        }

        scrollPane.add(table);

        mainVBox.add(scrollPane);

        //Adds edition button
        MyButton editButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        editButton.setText("edit");
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                getCodeAdditionDialog().setVisible(true);
            }
        });
        smallHbox.add(editButton);


        //adds addition button
        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        addButton.setText("add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                getCodeAdditionDialog().setVisible(true);
            }
        });
        smallHbox.add(addButton);


        //Adds deletion button
        MyButton deleteButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        deleteButton.setText("delete");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                getJustDialog().setVisible(true);
            }
        });
        smallHbox.add(deleteButton);


        return tempDialog;
    }

    public JustDialog getClientListDialog() {
        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        tempDialog.add(mainVBox);

        mainVBox.add(mainVBox.createHorizontalStrut(GlobalSizes.buttonHeight * 2));
        Box smallHbox = Box.createHorizontalBox();
        mainVBox.add(smallHbox);

        MyButton editButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        editButton.setText("edit");
        smallHbox.add(editButton);

        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        addButton.setText("add");
        smallHbox.add(addButton);

        return tempDialog;
    }


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
                saveButton.setText("Placeholder for saving");
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

//        mainVBox.add(mainVBox.createHorizontalStrut(GlobalSizes.buttonHeight * 2));
//        Box smallHbox = Box.createHorizontalBox();
//        mainVBox.add(smallHbox);
//
//        MyButton editButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
//        editButton.setText("edit");
//        smallHbox.add(editButton);
//
//        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
//        addButton.setText("add");
//        smallHbox.add(addButton);

        return tempDialog;
    }

}
