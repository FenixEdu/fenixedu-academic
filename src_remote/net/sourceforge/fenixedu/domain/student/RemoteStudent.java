package net.sourceforge.fenixedu.domain.student;

public class RemoteStudent extends RemoteStudent_Base {

    public RemoteStudent() {
	super();
    }

    public RemoteRegistration getLastRegistration() {
	return (RemoteRegistration) readRemoteDomainObjectByMethod("getLastRegistration");

    }

    public Boolean isSeniorForCurrentExecutionYear() {
	return toBoolean(readRemoteMethod("isSeniorForCurrentExecutionYear"));
    }

}
