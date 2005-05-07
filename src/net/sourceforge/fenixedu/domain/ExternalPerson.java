/*
 * Created on 9/Out/2003
 * 
 *  
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author: - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *          (naat@mega.ist.utl.pt)
 */
public class ExternalPerson extends ExternalPerson_Base {

	/**
	 * Creates a new instance of ExternalPerson
	 */
	public ExternalPerson() {
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof IExternalPerson) {
			IExternalPerson externalPerson = (IExternalPerson) obj;
			result = this.getPerson().equals(externalPerson.getPerson());
		}
		return result;
	}

	/**
	 * Creates a new instance of ExternalPerson
	 */
	public ExternalPerson(Integer idInternal) {
		setIdInternal(idInternal);
	}

}