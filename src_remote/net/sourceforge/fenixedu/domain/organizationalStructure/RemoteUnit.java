package net.sourceforge.fenixedu.domain.organizationalStructure;

public class RemoteUnit extends RemoteUnit_Base {

    public RemoteUnit() {
	super();
    }

    public String getName() {
	return readRemoteMethod("getName");
    }
}
