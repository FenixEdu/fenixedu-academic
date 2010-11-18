package net.sourceforge.fenixedu.domain;

import org.joda.time.DateTime;

public class RoleOperationLog extends RoleOperationLog_Base {
    
    public  RoleOperationLog() {
        super();
    }

    public RoleOperationLog(Role role, Person person, Person whoGranted, RoleOperationType operationType) {
	this.setOperationType(operationType);
	this.setLogDate(new DateTime()); // current time

	this.setIstUsername(person.getIstUsername());
	this.setWhoGrantedIstUsername(whoGranted.getIstUsername());

	this.setRole(role);
	this.setPerson(person);
	this.setPersonWhoGranted(whoGranted);

	person.addPersonRoleOperationLog(this);
	whoGranted.addGivenRoleOperationLog(this);
    }

    @Override
    protected RootDomainObject getRootDomainObject() {
	return this.getRole().getRootDomainObject();
    }
}
