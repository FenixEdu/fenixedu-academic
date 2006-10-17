package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOfficeType;

public class SchoolRegistrationDeclarationRequest extends SchoolRegistrationDeclarationRequest_Base {
    
    public  SchoolRegistrationDeclarationRequest() {
        super();
    }

    @Override
    public String getDocumentTemplateKey() {
	return null;
    }
 
    @Override
    public Set<AdministrativeOfficeType> getPossibleAdministrativeOffices() {
	final Set<AdministrativeOfficeType> result = new HashSet<AdministrativeOfficeType>();
	
	result.add(AdministrativeOfficeType.DEGREE);
	
	return result;
    }

}
