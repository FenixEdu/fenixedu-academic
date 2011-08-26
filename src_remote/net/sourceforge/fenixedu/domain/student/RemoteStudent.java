package net.sourceforge.fenixedu.domain.student;

import java.util.Collection;

public class RemoteStudent extends RemoteStudent_Base {

    public RemoteStudent() {
	super();
    }

    public RemoteRegistration getLastRegistration() {
	return (RemoteRegistration) readRemoteDomainObjectByMethod("getLastRegistration", null);
    }

    public Boolean isSeniorForCurrentExecutionYear() {
	return toBoolean(readRemoteMethod("isSeniorForCurrentExecutionYear", null));
    }

    public Boolean hasPersonalDataAuthorizationForProfessionalPurposesAt() {
	return toBoolean(readRemoteMethod("hasPersonalDataAuthorizationForProfessionalPurposesAt", null));
    }

    public Collection<RemoteRegistration> getActiveRegistrationsForCurrentExecutionYear() {
	return (java.util.Collection) readRemoteDomainObjectsByMethod("getActiveRegistrationsForCurrentExecutionYear", null);
    }

    public Collection<RemoteRegistration> getConcludedRegistrationsForPreviousExecutionYear() {
	return (java.util.Collection) readRemoteDomainObjectsByMethod("getConcludedRegistrationsForPreviousExecutionYear", null);
    }

}
