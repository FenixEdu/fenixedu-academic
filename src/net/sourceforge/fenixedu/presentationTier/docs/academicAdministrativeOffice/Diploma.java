package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Iterator;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.dataTransferObject.student.RegistrationConclusionBean;
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
	addParameter("universityName", institutionsUniversityUnit.getName());
	addParameter("universityPrincipalName", institutionsUniversityUnit.getInstitutionsUniversityPrincipal()
		.getValidatedName());

	final DiplomaRequest diplomaRequest = (DiplomaRequest) getDocumentRequest();
	addParameter("documentRequest", diplomaRequest);

	final Registration registration = diplomaRequest.getRegistration();
	addParameter("registration", registration);

	final Person person = registration.getPerson();
	addParameter("name", StringFormatter.prettyPrint(person.getName()));
	addParameter("nameOfFather", StringFormatter.prettyPrint(person.getNameOfFather()));
	addParameter("nameOfMother", StringFormatter.prettyPrint(person.getNameOfMother()));
	addParameter("birthLocale", getBirthLocale(person));

	final RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration, diplomaRequest
		.getCycleCurriculumGroup());

	addParameter("conclusionDate", registrationConclusionBean.getConclusionDate().toString("dd 'de' MMMM 'de' yyyy",
		LanguageUtils.getLocale()));
	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", LanguageUtils.getLocale()));

	if (diplomaRequest.hasFinalAverageDescription()) {
	    addParameter("finalAverageDescription", StringUtils.capitalize(ResourceBundle.getBundle(
		    "resources.EnumerationResources").getString(registrationConclusionBean.getFinalAverage().toString())));
	    addParameter("finalAverageQualified", registration.getDegreeType().getGradeScale().getQualifiedName(
		    registrationConclusionBean.getFinalAverage().toString()));
	} else if (diplomaRequest.hasDissertationTitle()) {
	    addParameter("dissertationTitle", registration.getDissertationEnrolment().getThesis().getFinalFullTitle()
		    .getContent());
	}

	addParameter("conclusionStatus", getConclusionStatusAndDegreeType(diplomaRequest, registration));
	addParameter("degreeFilteredName", registration.getDegree().getFilteredName());

	final CycleType cycleToInspect = diplomaRequest.getWhatShouldBeRequestedCycle();
	addParameter("graduateTitle", registration.getGraduateTitle(cycleToInspect));
	addParameter("degreeDescription", registration.getDegreeDescription(cycleToInspect));
    }

    private String getBirthLocale(final Person person) {
	final StringBuilder result = new StringBuilder();

	result.append(StringFormatter.prettyPrint(person.getParishOfBirth()));
	result.append(", ");
	result.append(StringFormatter.prettyPrint(person.getDistrictSubdivisionOfBirth()));

	return result.toString();
    }

    private Registration getRegistration() {
	return getDocumentRequest().getRegistration();
    }

    final private String getConclusionStatusAndDegreeType(final DiplomaRequest diplomaRequest, final Registration registration) {
	final StringBuilder result = new StringBuilder();

	final ResourceBundle applicationResources = ResourceBundle.getBundle("resources/ApplicationResources", LanguageUtils
		.getLocale());

	final DegreeType degreeType = registration.getDegreeType();
	// reportConcludedCycles(result, applicationResources,
	// diplomaRequest, registration);

	result.append(enumerationBundle.getString(diplomaRequest.getWhatShouldBeRequestedCycle().getQualifiedName()));
	result.append(" ").append(applicationResources.getString("of.masculine")).append(" ");

	result.append(degreeType.getPrefix()).append(degreeType.getFilteredName());

	return result.toString();
    }

    private void reportConcludedCycles(final StringBuilder result, final ResourceBundle applicationResources,
	    final DiplomaRequest diplomaRequest, final Registration registration) {
	for (final Iterator<CycleType> iter = registration.getConcludedCycles(diplomaRequest.getRequestedCycle()).iterator(); iter
		.hasNext();) {
	    final CycleType cycleType = iter.next();
	    result.append(enumerationBundle.getString(cycleType.getQualifiedName()));
	    if (iter.hasNext()) {
		result.append(" ").append(applicationResources.getString("and"));
		result.append(" ").append(applicationResources.getString("the.masculine")).append(" ");
	    }
	}
    }

}
