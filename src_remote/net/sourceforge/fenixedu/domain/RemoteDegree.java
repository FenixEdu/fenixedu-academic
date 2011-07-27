package net.sourceforge.fenixedu.domain;

public class RemoteDegree extends RemoteDegree_Base {

    public RemoteDegree() {
	super();
    }

    public String getPresentationName() {
	return readRemoteMethod("getPresentationName", null);
    }

    public String getDegreeTypeName() {
	return readRemoteMethod("getDegreeTypeName", null);
    }

}
