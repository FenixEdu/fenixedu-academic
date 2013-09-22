package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;

import pt.ist.fenixWebFramework.services.Service;

public class AppUserAuthorization extends AppUserAuthorization_Base {
    
    public  AppUserAuthorization(User user, ExternalApplication application) {
        super();
        setUser(user);
        setApplication(application);
    }

    @Service
    public void delete() {
        Set<AppUserSession> sessions = new HashSet<AppUserSession>(getSessionSet());
        for(AppUserSession session : sessions) {
            session.delete();
        }
        deleteDomainObject();
    }
    
}
