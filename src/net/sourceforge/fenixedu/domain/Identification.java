package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author mrsp
 */

public abstract class Identification extends Identification_Base {

    public Identification() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
    }

    public void delete() {	
        removeUser();
        removeRootDomainObject();
        deleteDomainObject();
    }

    public boolean isLogin() {
        return false;
    }
    
    public static List<Login> readAllLogins() {
        List<Login> logins = new ArrayList<Login>();
        for (Identification identification : RootDomainObject.getInstance().getIdentifications()) {
            if (identification.isLogin()) {
                logins.add((Login) identification);
            }
        }
        return logins;
    }
}
