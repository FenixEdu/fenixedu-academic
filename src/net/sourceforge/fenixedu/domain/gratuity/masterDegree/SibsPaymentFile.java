/*
 * Created on Apr 22, 2004
 */
package net.sourceforge.fenixedu.domain.gratuity.masterDegree;

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
		boolean result = false;

		if ((obj instanceof ISibsPaymentFile)) {
			ISibsPaymentFile sibsFile = (ISibsPaymentFile) obj;
			if ((sibsFile.getFilename() != null)
					&& (getFilename().equals(sibsFile.getFilename()))) {
				result = true;
			}
		}

		return result;
	}

	public String toString() {

		String result = "[" + this.getClass().getName() + ": \n";
		result += "idInternal = " + getIdInternal() + "; \n";
		result += "filename = " + getFilename().toString() + "; \n";
		result += "] \n";

		return result;
	}
}