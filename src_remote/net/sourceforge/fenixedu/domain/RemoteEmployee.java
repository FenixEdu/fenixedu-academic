package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.organizationalStructure.RemoteUnit;

public class RemoteEmployee extends RemoteEmployee_Base {
    public RemoteEmployee() {
	super();
    }

    public RemoteUnit getCurrentWorkingPlace() {
	return (RemoteUnit) readRemoteDomainObjectByMethod("getCurrentWorkingPlace");
    }
}
