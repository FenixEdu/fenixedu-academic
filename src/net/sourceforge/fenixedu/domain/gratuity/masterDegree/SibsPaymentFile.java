package net.sourceforge.fenixedu.domain.gratuity.masterDegree;

import net.sourceforge.fenixedu.domain.RootDomainObject;

public class SibsPaymentFile extends SibsPaymentFile_Base {

    public SibsPaymentFile() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public SibsPaymentFile(String filename) {
	this();
	setFilename(filename);
    }

    public static SibsPaymentFile readByFilename(String filename) {
	for (SibsPaymentFile sibsPaymentFile : RootDomainObject.getInstance().getSibsPaymentFiles()) {
	    if (sibsPaymentFile.getFilename().equals(filename)) {
		return sibsPaymentFile;
	    }
	}
	return null;
    }

}
