package net.sourceforge.fenixedu.domain;

public class Login extends Login_Base {

    public Login() {
        super();
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
