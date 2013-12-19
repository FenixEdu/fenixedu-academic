package net.sourceforge.fenixedu.util;

import java.sql.Connection;
import java.util.Iterator;
import java.util.ServiceLoader;

public abstract class ConnectionManager {

    private static final ConnectionManager instance;

    static {
        Iterator<ConnectionManager> managers = ServiceLoader.load(ConnectionManager.class).iterator();
        if (!managers.hasNext()) {
            throw new Error("No implementation of ConnectionManager was found in the classpath");
        }
        instance = managers.next();
    }

    public static Connection getCurrentSQLConnection() {
        return instance.getConnection();
    }

    protected abstract Connection getConnection();

}
