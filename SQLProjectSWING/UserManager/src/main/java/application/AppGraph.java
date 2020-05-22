package application;

import _throwThisAway.JustDialog;
import model.Database;
import model.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.*;


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


        try
        {
            // create our mysql database connection
            String myDriver = "org.gjt.mm.mysql.Driver";
            String myUrl = "jdbc:mysql://localhost/SQLProject";
            Class.forName(myDriver);
            Connection conn = DriverManager.getConnection(myUrl, "SQLProject", "");

            // our SQL SELECT query.
            // if you only need a few columns, specify them by name instead of using "*"
            String query = "SELECT * FROM users";

            // create the java statement
            Statement st = conn.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(query);

            // iterate through the java resultset
            while (rs.next())
            {
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                Date dateCreated = rs.getDate("date_created");
                boolean isAdmin = rs.getBoolean("is_admin");
                int numPoints = rs.getInt("num_points");

                // print the results
                System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, dateCreated, isAdmin, numPoints);
            }
            st.close();
        }
        catch (Exception e)
        {
            System.err.println("Got an exception! ");
            e.printStackTrace();
        }

        return instance;
    }

    // GETTERS
    public AppWindow getAppWindow() {
        return appWindow;
    }

    public JustDialog getJustDialog() {
        return justDialog;
    }

    public JustDialog getPostCodesDialog() {
        Dimension postCodeDimensions = new Dimension();

        Box mainVBox = Box.createVerticalBox();
        mainVBox.setOpaque(true);
        mainVBox.setBackground(Color.blue);

        //Set height to screen - top bar and width to half the screen width
        postCodeDimensions.setSize(screeWidth / 2, screeHeight / 2);

        JustDialog tempDialog = new JustDialog(appWindow, userRepository);

        //Applaying the dimension to dialog
        tempDialog.setPreferredSize(postCodeDimensions);
        tempDialog.setMaximumSize(postCodeDimensions);
        tempDialog.setMinimumSize(postCodeDimensions);


        tempDialog.add(mainVBox);


        MyButton confirmButton = new MyButton(GlobalSizes.buttonHeight, GlobalSizes.buttonWidth);
        confirmButton.setText("confirm");
        mainVBox.add(confirmButton);


//        Getting screen height without the taskbars:
//        Rectangle screenHeight = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
//        int height = (int)screenHeight.getHeight();


        return tempDialog;
    }
}
