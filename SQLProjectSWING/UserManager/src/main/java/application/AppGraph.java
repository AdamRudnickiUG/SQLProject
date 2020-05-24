package application;

import _throwThisAway.JustDialog;
import model.Database;
import model.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;


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


        try {
            Connection con;

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            //Activate/use JDBC
            con = DriverManager.getConnection("jdbc:sqlserver://" +
                    "localhost:1433;databaseName=SQLProject;" +
                    "user=Java;password=passwd;");
            System.out.println("Po³¹czono z baz¹ danych");
            con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            String querySELECT = "SELECT * FROM urzadzenie";
            String queryINSERT = "INSERT INTO urzadzenie VALUES\n" +
                    "(5, 10)";
            String queryDELETE = "DELETE FROM urzadzenie WHERE id = 5;\n";

//          Creating basic statement
            Statement st = con.createStatement();
            Statement st2 = con.createStatement();


//           Statement st2 = con.createStatement();

            // execute the query, and get a java resultset
            ResultSet rs = st.executeQuery(querySELECT);


            // iterate through the java resultset
            while (rs.next()) {
                String id = rs.getString("id");
                String ip_stacji = rs.getString("ip_stacji");

                // print the results
                System.out.format("%s, %s\n", id, ip_stacji);
            }

//            st.executeQuery(queryDELETE);
//            st.executeQuery(queryINSERT);

            ResultSet rs2 = st2.executeQuery(querySELECT);
            while (rs2.next()) {
                String id = rs2.getString("id");
                String ip_stacji = rs2.getString("ip_stacji");

                // print the results
                System.out.format("%s, %s\n", id, ip_stacji);
            }
            st.close();
            st2.close();
            con.close();
        } catch (Exception e) {
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
