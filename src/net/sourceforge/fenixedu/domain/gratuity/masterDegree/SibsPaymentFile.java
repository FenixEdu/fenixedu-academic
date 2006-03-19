/*
 * Created on Apr 22, 2004
 */
package net.sourceforge.fenixedu.domain.gratuity.masterDegree;

import net.sourceforge.fenixedu.domain.RootDomainObject;


/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class SibsPaymentFile extends SibsPaymentFile_Base {

	public SibsPaymentFile() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public SibsPaymentFile(String filename) {
		this();
		setFilename(filename);
	}

}