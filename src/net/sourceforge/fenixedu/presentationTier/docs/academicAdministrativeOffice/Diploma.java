package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.LanguageUtils;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class Diploma extends AdministrativeOfficeDocument {

    
    protected Diploma(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	final Unit institutionUnit = RootDomainObject.getInstance().getInstitutionUnit();
	// TODO talk to Manel, change this into call to University Unit
	parameters.put("universityName", "Universidade Técnica de Lisboa");
	parameters.put("universityPrincipalName", "José Dias Lopes da Silva");
	
	final DiplomaRequest diplomaRequest = (DiplomaRequest) getDocumentRequest();
	parameters.put("documentRequest", diplomaRequest);

	final Registration registration = diplomaRequest.getRegistration();
	parameters.put("registration", registration);
	
	final Person person = registration.getPerson();
	parameters.put("name", person.getName());
	parameters.put("nameOfFather", person.getNameOfFather());
	parameters.put("nameOfMother", person.getNameOfMother());
	parameters.put("birthLocale", person.getDistrictOfBirth());

	parameters.put("conclusionDate", registration.getConclusionDate().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
	parameters.put("institutionName", institutionUnit.getName());
	parameters.put("finalAverageDescription", StringUtils.capitalize(registration.getFinalAverageDescription()));
	parameters.put("employeeLocation", AccessControl.getPerson().getEmployee().getCurrentCampus().getLocation());
	parameters.put("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
    }

}
