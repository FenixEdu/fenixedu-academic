/*
 * Created on May 3, 2005
 *
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IInstitution;
import net.sourceforge.fenixedu.domain.Institution;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class InfoInstitution extends InfoObject {

    protected String name;

    public InfoInstitution() {
    }

    public InfoInstitution(String name) {
        setName(name);
    }

    public void copyFromDomain(IInstitution institution) {
        super.copyFromDomain(institution);
        if (institution != null) {
            setName(institution.getName());
        }
    }

    public static IInstitution newDomainFromInfo(InfoInstitution infoInstitution){
        IInstitution institution = null;
        if(infoInstitution != null){
            institution = new Institution();
            infoInstitution.copyToDomain(infoInstitution, institution);
        }
        return institution;
    }
    
    public void copyToDomain(InfoInstitution infoInstitution, IInstitution institution){
        if (infoInstitution != null && institution != null){
            super.copyToDomain(infoInstitution, institution);
            institution.setName(infoInstitution.getName());
        }
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
