/*
 * Created on Oct 14, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 */
public class InfoInstitution extends InfoObject {
    private String name;

    public InfoInstitution() {
    }

    public InfoInstitution(String name) {
	setName(name);
    }

    /**
     * @return Returns the name.
     */
    public String getName() {
	return name;
    }

    /**
     * @param name
     *            The name to set.
     */
    public void setName(String name) {
	this.name = name;
    }

    /**
     * @param institution
     * @return
     */
    public static InfoInstitution newInfoFromDomain(Unit institution) {
	InfoInstitution infoInstitution = null;
	if (institution != null) {
	    infoInstitution = new InfoInstitution();
	    infoInstitution.copyFromDomain(institution);
	}

	return infoInstitution;
    }

    public void copyFromDomain(Unit institution) {
	super.copyFromDomain(institution);
	if (institution != null) {
	    setName(institution.getName());
	}
    }
}