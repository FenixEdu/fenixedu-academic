package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class DegreeDiploma extends DegreeDiploma_Base {
    
    public  DegreeDiploma() {
        super();
    }

    @Override
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();
	
	result.add(AdministrativeOfficeType.DEGREE);
	
	return result;
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.DEGREE_DIPLOMA;
    }

    @Override
    public void conclude() throws DomainException {
    }

    @Override
    public String getDocumentTemplateKey() {
	return null;
    }
    
}
