/*
 * Created on Oct 14, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 * 
 */
public class InfoExternalPerson extends InfoObject {

    private final ExternalContract externalPersonDomainReference;

    public InfoExternalPerson(final ExternalContract externalPerson) {
        externalPersonDomainReference = externalPerson;
    }

    public static InfoExternalPerson newInfoFromDomain(final ExternalContract externalPerson) {
        return externalPerson == null ? null : new InfoExternalPerson(externalPerson);
    }

    private ExternalContract getExternalPerson() {
        return externalPersonDomainReference;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof InfoExternalPerson && getExternalPerson() == ((InfoExternalPerson) obj).getExternalPerson();
    }

    @Override
    public String getExternalId() {
        return getExternalPerson().getExternalId();
    }

    @Override
    public void setExternalId(String integer) {
        throw new Error("Method should not be called!");
    }

    /**
     * @return Returns the infoPerson.
     */
    public InfoPerson getInfoPerson() {
        return InfoPerson.newInfoFromDomain(getExternalPerson().getPerson());
    }

    /**
     * @return Returns the infoInstitution.
     */
    public InfoInstitution getInfoInstitution() {
        return InfoInstitution.newInfoFromDomain(getExternalPerson().getInstitutionUnit());
    }

}
