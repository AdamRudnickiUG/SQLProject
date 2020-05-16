package application;

import model.Database;
import model.UserRepository;
import _throwThisAway.JustDialog;
import utils.UiUtils;

public class AppGraph {

    // APPLICATION OBJECTS DI
    Database database = new Database();
    UserRepository userRepository = new UserRepository(database);
    AppWindow appWindow = new AppWindow();
    JustDialog justDialog = new JustDialog(appWindow, userRepository);

    // GETTERS
    public AppWindow getAppWindow() {
        return appWindow;
    }
    public JustDialog getJustDialog() {
        return justDialog;
    }

    // APP GRAPH SINGLETON
    private static AppGraph instance = null;
    public static AppGraph getInstance() {
        if (instance == null) instance = new AppGraph();
        return instance;
    }
}
