package model;

import com.sun.jndi.toolkit.url.Uri;

public class UserRepository {

    private Database database;

    public UserRepository(Database database) {
        this.database = database;
    }
}
