package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.organizationalStructure.RemoteUnit;

public class RemoteEmployee extends RemoteEmployee_Base {
    public RemoteEmployee() {
	super();
    }

    public Collection<RemoteUnit> getCurrentWorkingPlacePath() {
	return (Collection<RemoteUnit>) readRemoteDomainObjectsByMethod("getCurrentWorkingPlacePath");
    }
}
