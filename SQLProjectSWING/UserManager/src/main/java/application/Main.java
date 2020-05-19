package application;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                // Application looks
                setAppTheme();
                JFrame.setDefaultLookAndFeelDecorated(true);

                // Create and start the app
                AppGraph.getInstance().getAppWindow().showApp();
            }
        });
    }

    private static void setAppTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            System.out.println("UnsupportedLookAndFeelException");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("ClassNotFoundException");
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.out.println("InstantiationException");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("IllegalAccessException");
            e.printStackTrace();
        }
    }
}
