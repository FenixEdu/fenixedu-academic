/*
 * Created on Dec 5, 2003
 *  
 */
package net.sourceforge.fenixedu.domain;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author - Nadir Tarmahomed (naat@mega.ist.utl.pt)
 */
public class WorkLocation extends WorkLocation_Base {

	public boolean equals(Object obj) {
		boolean result = false;

		if (obj instanceof IWorkLocation) {
			IWorkLocation workLocation = (IWorkLocation) obj;
			result = getName().equals(workLocation.getName());
		}
		return result;
	}

}