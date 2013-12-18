package net.sourceforge.fenixedu.domain;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

public class RoleOperationLog extends RoleOperationLog_Base {

    public RoleOperationLog() {
        super();
    }

    public RoleOperationLog(Role role, Person person, Person whoGranted, RoleOperationType operationType) {
        setOperationType(operationType);
        setLogDate(new DateTime());
        setRole(role);
        setIstUsername(person.getIstUsername());
        setPerson(person);
        if (whoGranted != null) {
            setWhoGrantedIstUsername(whoGranted.getIstUsername());
            setPersonWhoGranted(whoGranted);
        } else {
            setWhoGrantedIstUsername("No user");
        }
    }

    protected Bennu getRootDomainObject() {
        return hasRole() ? getRole().getRootDomainObject() : null;
    }

    @Deprecated
    public boolean hasIstUsername() {
        return getIstUsername() != null;
    }

    @Deprecated
    public boolean hasWhoGrantedIstUsername() {
        return getWhoGrantedIstUsername() != null;
    }

    @Deprecated
    public boolean hasRole() {
        return getRole() != null;
    }

    @Deprecated
    public boolean hasPersonWhoGranted() {
        return getPersonWhoGranted() != null;
    }

    @Deprecated
    public boolean hasOperationType() {
        return getOperationType() != null;
    }

    @Deprecated
    public boolean hasPerson() {
        return getPerson() != null;
    }

    @Deprecated
    public boolean hasLogDate() {
        return getLogDate() != null;
    }

}
