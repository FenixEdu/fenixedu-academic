/*
 * Created on Apr 22, 2004
 */
package net.sourceforge.fenixedu.domain.gratuity.masterDegree;

import net.sourceforge.fenixedu.domain.grant.owner.IGrantOwner;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class SibsPaymentFile extends SibsPaymentFile_Base {

	public SibsPaymentFile() {
	}

	/**
	 * @param filename
	 */
	public SibsPaymentFile(String filename) {
		super();
		setFilename(filename);
	}

	public boolean equals(Object obj) {
        if (obj instanceof ISibsPaymentFile) {
            final ISibsPaymentFile sibsPaymentFile = (ISibsPaymentFile) obj;
            return this.getIdInternal().equals(sibsPaymentFile.getIdInternal());
        }
        return false;
    }

	public String toString() {

		String result = "[" + this.getClass().getName() + ": \n";
		result += "idInternal = " + getIdInternal() + "; \n";
		result += "filename = " + getFilename().toString() + "; \n";
		result += "] \n";

		return result;
	}
}