package net.sourceforge.fenixedu.domain;

import org.joda.time.YearMonthDay;

public class Login extends Login_Base {

    public Login() {
        super();
    }
    
    public Login(User user, String username){
        this();
        this.setUser(user);
        this.setUsername(username);
        this.setBeginDate(new YearMonthDay());
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
}
