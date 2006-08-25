/*
 * Created on Oct 14, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.ExternalPerson;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *  
 */
public class InfoExternalPerson extends InfoObject {
    
    private InfoPerson infoPerson;

    private InfoInstitution infoInstitution;

    /**
     * @return Returns the infoPerson.
     */
    public InfoPerson getInfoPerson() {
        return infoPerson;
    }

    /**
     * @param infoPerson
     *            The infoPerson to set.
     */
    public void setInfoPerson(InfoPerson infoPerson) {
        this.infoPerson = infoPerson;
    }

    /**
     * @return Returns the infoInstitution.
     */
    public InfoInstitution getInfoInstitution() {
        return infoInstitution;
    }

    /**
     * @param infoInstitution
     *            The infoInstitution to set.
     */
    public void setInfoInstitution(InfoInstitution infoInstitution) {
        this.infoInstitution = infoInstitution;
    }

    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof InfoExternalPerson) {
            InfoExternalPerson infoExternalPerson = (InfoExternalPerson) obj;
            result = this.getInfoPerson().equals(infoExternalPerson.getInfoPerson());
        }
        return result;
    }

    public static InfoExternalPerson newInfoFromDomain(ExternalPerson externalPerson) {
        InfoExternalPerson infoExternalPerson = null;
        if (externalPerson != null) {
            infoExternalPerson = new InfoExternalPerson();
            infoExternalPerson.copyFromDomain(externalPerson);
        }
        return infoExternalPerson;
    }
    
    public void copyFromDomain(ExternalPerson externalPerson) {
        super.copyFromDomain(externalPerson);
        
        setInfoPerson(InfoPerson.newInfoFromDomain(externalPerson.getPerson()));
        setInfoInstitution(InfoInstitution.newInfoFromDomain(externalPerson.getInstitutionUnit()));
        
    }
    
    /*
     * Temporary solution to remove InfoPerson
     */
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}