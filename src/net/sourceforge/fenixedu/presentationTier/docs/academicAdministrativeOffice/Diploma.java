package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Iterator;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
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
	parameters.put("universityPrincipalName", institutionsUniversityUnit.getInstitutionsUniversityPrincipal().getValidatedName());
	
	final DiplomaRequest diplomaRequest = (DiplomaRequest) getDocumentRequest();
	parameters.put("documentRequest", diplomaRequest);

	final Registration registration = diplomaRequest.getRegistration();
	parameters.put("registration", registration);
	
	final Person person = registration.getPerson();
	parameters.put("name", StringFormatter.prettyPrint(person.getName()));
	parameters.put("nameOfFather", StringFormatter.prettyPrint(person.getNameOfFather()));
	parameters.put("nameOfMother", StringFormatter.prettyPrint(person.getNameOfMother()));
	parameters.put("birthLocale", StringFormatter.prettyPrint(person.getDistrictOfBirth()));

	parameters.put("conclusionDate", registration.getConclusionDate(diplomaRequest.getRequestedCycle()).toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));
	parameters.put("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	parameters.put("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));

	if (diplomaRequest.hasFinalAverageDescription()) {
	    parameters.put("finalAverageDescription", StringUtils.capitalize(registration.getFinalAverageDescription()));
	} else if (diplomaRequest.hasDissertationTitle()) {
	    parameters.put("dissertationTitle", Registration.readByNumberAndDegreeType(40785, DegreeType.BOLONHA_MASTER_DEGREE).get(0).getDissertationEnrolment().getThesis().getFinalFullTitle().getContent());
	}
	
	parameters.put("conclusionStatus", getConclusionStatusAndDegreeType(diplomaRequest, registration));
	parameters.put("graduateTitle", registration.getGraduateTitle(diplomaRequest.getRequestedCycle()));
    }

    final private String getConclusionStatusAndDegreeType(final DiplomaRequest diplomaRequest, final Registration registration) {
	final StringBuilder result = new StringBuilder();

	final ResourceBundle applicationResources = ResourceBundle.getBundle("resources/ApplicationResources", LanguageUtils.getLocale());
	
	final DegreeType degreeType = registration.getDegreeType();
	if (degreeType.isComposite()) {
	    for (final Iterator<CycleType> iter = registration.getConcludedCycles(diplomaRequest.getRequestedCycle()).iterator(); iter.hasNext();) {
		final CycleType cycleType = iter.next();
		result.append(enumerationBundle.getString(cycleType.getQualifiedName()));
		if (iter.hasNext()) {
		    result.append(" ").append(applicationResources.getString("and"));
		    result.append(" ").append(applicationResources.getString("the.masculine")).append(" ");
		}
	    }
	    result.append(" ").append(applicationResources.getString("of.masculine")).append(" ");
	} 
	
	result.append(applicationResources.getString("label.degree"));
	result.append(" ").append(applicationResources.getString("of"));
	result.append(" ").append(degreeType.getFilteredName());
	if (degreeType.hasExactlyOneCycleType()) {
	    result.append(" (").append(enumerationBundle.getString(degreeType.getCycleType().getQualifiedName())).append(")");
	}
	
	return result.toString();
    }

}
