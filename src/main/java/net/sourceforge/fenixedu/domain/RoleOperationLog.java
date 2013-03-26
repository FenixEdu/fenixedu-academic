package net.sourceforge.fenixedu.domain;

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

    protected RootDomainObject getRootDomainObject() {
        return hasRole() ? getRole().getRootDomainObject() : null;
    }
}
