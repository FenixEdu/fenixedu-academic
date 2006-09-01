/*
 * Created on Oct 14, 2003
 *  
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.ExternalPerson;

/**
 * @author Shezad Anavarali (sana@mega.ist.utl.pt)
 * @author Nadir Tarmahomed (naat@mega.ist.utl.pt)
 *  
 */
public class InfoExternalPerson extends InfoObject {
    
    private DomainReference<ExternalPerson> externalPersonDomainReference;

    public InfoExternalPerson(final ExternalPerson externalPerson) {
	externalPersonDomainReference = new DomainReference<ExternalPerson>(externalPerson);
    }
    
    public static InfoExternalPerson newInfoFromDomain(final ExternalPerson externalPerson) {
        return externalPerson == null ? null : new InfoExternalPerson(externalPerson);
    }
    
    private ExternalPerson getExternalPerson() {
	return externalPersonDomainReference == null ? null : externalPersonDomainReference.getObject();
    }
    
    public boolean equals(Object obj) {
        return obj instanceof InfoExternalPerson && getExternalPerson() == ((InfoExternalPerson) obj).getExternalPerson();
    }

    @Override
    public Integer getIdInternal() {
	return getExternalPerson().getIdInternal();
    }

    @Override
    public void setIdInternal(Integer integer) {
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
