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
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
