package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Iterator;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.LanguageUtils;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

public class Diploma extends AdministrativeOfficeDocument {

    protected Diploma(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	final UniversityUnit institutionsUniversityUnit = UniversityUnit.getInstitutionsUniversityUnit();
	parameters.put("universityName", institutionsUniversityUnit.getName());
	parameters.put("universityPrincipalName", institutionsUniversityUnit.getInstitutionsUniversityPrincipal().getName());
	
	final DiplomaRequest diplomaRequest = (DiplomaRequest) getDocumentRequest();
	parameters.put("documentRequest", diplomaRequest);

	final Registration registration = diplomaRequest.getRegistration();
	parameters.put("registration", registration);
	
	final Person person = registration.getPerson();
	parameters.put("name", StringFormatter.prettyPrint(person.getName()));
	parameters.put("nameOfFather", person.getNameOfFather());
	parameters.put("nameOfMother", person.getNameOfMother());
	parameters.put("birthLocale", person.getDistrictOfBirth());

	parameters.put("conclusionDate", registration.getConclusionDate().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
	parameters.put("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	parameters.put("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));

	if (diplomaRequest.hasFinalAverageDescription()) {
	    parameters.put("finalAverageDescription", StringUtils.capitalize(registration.getFinalAverageDescription()));
	} else if (diplomaRequest.hasDissertationTitle()) {
	    parameters.put("dissertationTitle", registration.getDissertationEnrolment().getThesis().getFinalFullTitle().getContent());
	}
	
	setConclusionStatus(diplomaRequest, registration);
    }

    private void setConclusionStatus(final DiplomaRequest diplomaRequest, final Registration registration) {
	final ResourceBundle applicationResources = ResourceBundle.getBundle("resources/ApplicationResources");
	String conclusionStatus = "";
	
	if (registration.getDegreeType().isComposite()) {
	    final ResourceBundle enumerationResources = ResourceBundle.getBundle("resources/EnumerationResources");
	    
	    for (final Iterator<CycleType> iter = registration.getConcludedCycles().iterator(); iter.hasNext();) {
		final CycleType cycleType = iter.next();
		conclusionStatus += enumerationResources.getString(cycleType.getQualifiedName());
		if (iter.hasNext()) {
		    conclusionStatus += " e ";
		}
	    }
	} else {
	    conclusionStatus = applicationResources.getString("label.degree");
	}
	
	parameters.put("conclusionStatus",  conclusionStatus);
    }

}
