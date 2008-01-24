package net.sourceforge.fenixedu.stm;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.server.UID;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sourceforge.fenixedu._development.PropertiesManager;
import pt.utl.ist.fenix.tools.util.StringAppender;


class Util {

    private static final Iterator EMPTY_ITER = new Iterator() {
        public boolean hasNext() { return false; }
        public Object next() { throw new NoSuchElementException(); }
        public void remove() { throw new UnsupportedOperationException(); }
    };

    public static <T> Iterator<T> emptyIterator() {
        return (Iterator<T>)EMPTY_ITER;
    }


    private static final String uidString = (new UID()).toString();

    public static String getServerName() {
        try {
            final String hostAddress = InetAddress.getLocalHost().getHostAddress();
            final String username = getUsername();
            final String appName = getAppName();
            return StringAppender.append(username, "@", hostAddress, ":", appName, ":", uidString);
        } catch (UnknownHostException uhe) {
            throw new Error("Couldn't get this host address, which is needed to register in the database");
        }
    }
	
    public static String getAppName() {
        return PropertiesManager.getProperty("app.name");
    }

    public static String getUsername() {
        final String user = System.getenv("USER");
        final String username = System.getenv("USERNAME");
        
        return (user != null) ? user : (username != null) ? username : "unknown"; 
    }
}
