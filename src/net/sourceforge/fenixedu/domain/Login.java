package net.sourceforge.fenixedu.domain;

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

    public static Login readLoginByUsername(String username) {
        for (Identification identification : RootDomainObject.getInstance().getIdentifications()) {
            if (identification instanceof Login) {
                Login login = (Login) identification;
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
