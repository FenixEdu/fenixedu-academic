package net.sourceforge.fenixedu.domain;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.joda.time.DateTime;

public class Login extends Login_Base {

    public Login() {
        super();
    }

    public Login(User user, String username) {
        this();
        if (username == null || username.equals("") || user == null) {
            throw new DomainException("error.empty.username.or.user");
        }
        if (checkIfUsernameExists(username, this)) {
            throw new DomainException("error.existent.username");
        }
        this.setUser(user);
        this.setUsername(username);
        this.setBeginDateDateTime(new DateTime());
    }

    /**
     * This map is a temporary solution until DML provides indexed relations.
     * 
     */
    private static final Map<String, SoftReference<Login>> loginMap = new HashMap<String, SoftReference<Login>>();

    public static Login readLoginByUsername(String username) {
        // Temporary solution until DML provides indexed relations.
        final String lowerCaseUsername = username.toLowerCase();
        final SoftReference<Login> loginReference = loginMap.get(lowerCaseUsername);
        if (loginReference != null) {
            final Login login = loginReference.get();
            if (login != null && login.getRootDomainObject() == RootDomainObject.getInstance()
                    && login.getUsername().equalsIgnoreCase(lowerCaseUsername)) {
                return login;
            } else {
                loginMap.remove(lowerCaseUsername);
            }
        }
        // *** end of hack

        for (final Identification identification : RootDomainObject.getInstance().getIdentifications()) {
            if (identification instanceof Login) {
                final Login login = (Login) identification;
                // Temporary solution until DML provides indexed relations.
                loginMap.put(login.getUsername().toLowerCase(), new SoftReference<Login>(login));
                // *** end of hack
                if (login.getUsername().equalsIgnoreCase(username)) {
                    return login;
                }
            }
        }
        return null;
    }
    
    public static boolean checkIfUsernameExists(String username, Login thisLogin) {
        for (Login login : Identification.readAllLogins()) {
            if (!login.equals(thisLogin) && username.equalsIgnoreCase(login.getUsername())) {
                return true;
            }            
        }
        return false;
    }
}
