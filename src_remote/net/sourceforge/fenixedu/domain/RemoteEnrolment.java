package net.sourceforge.fenixedu.domain;


public class RemoteEnrolment extends RemoteEnrolment_Base {

    public RemoteEnrolment() {
	super();
    }

    public Boolean isApproved() {
	return toBoolean(readRemoteMethod("isApproved"));
    }

    public RemoteExecutionYear getExecutionYear() {
	return (RemoteExecutionYear) readRemoteDomainObjectByMethod("getExecutionYear");
    }

    public Double getEctsCredits() {
	return toDouble(readRemoteMethod("getEctsCredits"));
    }
}
