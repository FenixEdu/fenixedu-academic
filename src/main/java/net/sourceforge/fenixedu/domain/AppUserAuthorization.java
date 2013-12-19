package net.sourceforge.fenixedu.domain;

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.bennu.core.domain.User;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class AppUserAuthorization extends AppUserAuthorization_Base {

    public AppUserAuthorization(User user, ExternalApplication application) {
        super();
        setUser(user);
        setApplication(application);
    }

    @Atomic(mode = TxMode.WRITE)
    public void delete() {
        Set<AppUserSession> sessions = new HashSet<AppUserSession>(getSessionSet());
        for (AppUserSession session : sessions) {
            session.delete();
        }
        setUser(null);
        setApplication(null);
        deleteDomainObject();
    }

}
