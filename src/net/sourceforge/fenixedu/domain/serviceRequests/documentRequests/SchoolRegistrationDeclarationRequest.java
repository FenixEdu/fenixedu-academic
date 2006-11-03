package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class SchoolRegistrationDeclarationRequest extends SchoolRegistrationDeclarationRequest_Base {
    
    public  SchoolRegistrationDeclarationRequest() {
        super();
    }

    @Override
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();
	
	result.add(AdministrativeOfficeType.DEGREE);
	
	return result;
    }

    @Override
    protected void assertProcessingStatePreConditions() throws DomainException {
	// TODO Auto-generated method stub
	
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.SCHOOL_REGISTRATION_DECLARATION;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

}
