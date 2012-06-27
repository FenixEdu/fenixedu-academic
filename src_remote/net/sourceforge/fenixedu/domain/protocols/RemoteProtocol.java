package net.sourceforge.fenixedu.domain.protocols;

public class RemoteProtocol extends RemoteProtocol_Base {

    public RemoteProtocol() {
	super();
    }

    public String exportProtocolActionAsString() {
	return readRemoteMethod("getProtocolActionString", null);
    }

    public String generateResponsiblesJSON() {
	return readRemoteMethod("generateResponsiblesJSON", null);
    }

    public String getFilesJSON() {
	return readRemoteMethod("getFilesJSON", null);
    }

}
