package net.sourceforge.fenixedu.domain.assiduousness;

import net.sourceforge.fenixedu.domain.DeleteFileRequest;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

public class ClosedMonthFile extends ClosedMonthFile_Base {

    public ClosedMonthFile() {
	super();
    }

    public ClosedMonthFile(String filename, String displayName, String mimeType, String checksum, String checksumAlgorithm,
	    Integer size, String externalStorageIdentification, Group permittedGroup) {
	this();
	init(filename, displayName, mimeType, checksum, checksumAlgorithm, size, externalStorageIdentification, permittedGroup);
    }

    public void delete() {
	removeRootDomainObject();
	removeClosedMonthDocument();
	setPermittedGroup(null);
	new DeleteFileRequest(AccessControl.getPerson(), getExternalStorageIdentification());
	deleteDomainObject();
    }
}
