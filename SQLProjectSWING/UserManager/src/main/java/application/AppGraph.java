package application;

import DBOperations.TestOps;
import _throwThisAway.JustDialog;
import model.Database;
import model.UserRepository;

import javax.swing.*;
import java.awt.*;


public class AppGraph {

    // APP GRAPH SINGLETON
    private static AppGraph instance = null;


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

//    public JustDialog getPostCodeEdition(){
//
//    }

    public JustDialog getPostCodesDialog() {
        Dimension postCodeDimensions = new Dimension();

        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.lightGray);

        //Set height to screen - top bar and width to half the screen width
        postCodeDimensions.setSize(screeWidth / 2, screeHeight / 2);

        JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        //Applaying the dimension to dialog
        tempDialog.setPreferredSize(postCodeDimensions);
        tempDialog.setMaximumSize(postCodeDimensions);
        tempDialog.setMinimumSize(postCodeDimensions);


        tempDialog.add(mainVBox);

//        tempDialog.setLocation(screeWidth / 2 - tempDialog.getHeight() / 2, screeHeight / 2 - tempDialog.getHeight() / 2);

        mainVBox.add(mainVBox.createHorizontalStrut(GlobalSizes.buttonHeight * 2));
        Box smallHbox = Box.createHorizontalBox();
        mainVBox.add(smallHbox);

        MyButton editButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        editButton.setText("edit");
        smallHbox.add(editButton);

        MyButton addButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        addButton.setText("add");
        smallHbox.add(addButton);


//        JTextArea scrolable = new JTextArea(20, 60);
//        Getting screen height without the taskbars:
//        Rectangle screenHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
//        int height = (int)screenHeight.getHeight();


        return tempDialog;
    }
}
