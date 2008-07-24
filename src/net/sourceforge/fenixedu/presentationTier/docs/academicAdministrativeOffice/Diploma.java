package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

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
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class Diploma extends AdministrativeOfficeDocument {

    protected Diploma(final DocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected void fillReport() {
	addInstitutionParameters();
	addPersonParameters();

	final DiplomaRequest diplomaRequest = (DiplomaRequest) getDocumentRequest();
	addParameter("documentRequest", diplomaRequest);

	final Registration registration = diplomaRequest.getRegistration();
	addParameter("registration", registration);

	final RegistrationConclusionBean registrationConclusionBean = new RegistrationConclusionBean(registration, diplomaRequest
		.getCycleCurriculumGroup());

	addParameter("conclusionDate", getConclusionDate(diplomaRequest, registrationConclusionBean));
	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("day", new YearMonthDay().toString("dd 'de' MMMM 'de' yyyy", Language.getLocale()));

	if (diplomaRequest.hasFinalAverageDescription()) {
	    addParameter("finalAverageDescription", StringUtils.capitalize(ResourceBundle.getBundle(
		    "resources.EnumerationResources").getString(registrationConclusionBean.getFinalAverage().toString())));
	    addParameter("finalAverageQualified", registration.getDegreeType().getGradeScale().getQualifiedName(
		    registrationConclusionBean.getFinalAverage().toString()));
	} else if (diplomaRequest.hasDissertationTitle()) {
	    addParameter("dissertationTitle", registration.getDissertationThesisTitle());
	}

	addParameter("conclusionStatus", getConclusionStatusAndDegreeType(diplomaRequest, registration));
	addParameter("degreeFilteredName", registration.getDegree().getFilteredName());

	final CycleType cycleToInspect = diplomaRequest.getWhatShouldBeRequestedCycle();
	addParameter("graduateTitle", registration.getGraduateTitle(cycleToInspect));
    }

    private void addInstitutionParameters() {
	final UniversityUnit institutionsUniversityUnit = UniversityUnit.getInstitutionsUniversityUnit();
	addParameter("universityName", institutionsUniversityUnit.getName());
	addParameter("universityPrincipalName", institutionsUniversityUnit.getInstitutionsUniversityPrincipal()
		.getValidatedName());
    }

    private void addPersonParameters() {
	final Person person = getDocumentRequest().getPerson();
	addParameter("name", StringFormatter.prettyPrint(person.getName()));
	addParameter("nameOfFather", StringFormatter.prettyPrint(person.getNameOfFather()));
	addParameter("nameOfMother", StringFormatter.prettyPrint(person.getNameOfMother()));
	addParameter("birthLocale", getBirthLocale(person));
    }

    private String getBirthLocale(final Person person) {
	final StringBuilder result = new StringBuilder();

	result.append(StringFormatter.prettyPrint(person.getParishOfBirth()));
	result.append(", ");
	result.append(StringFormatter.prettyPrint(person.getDistrictSubdivisionOfBirth()));

	return result.toString();
    }

    private String getConclusionDate(final DiplomaRequest diplomaRequest,
	    final RegistrationConclusionBean registrationConclusionBean) {

	final LocalDate result = diplomaRequest.hasDissertationTitle() ? registrationConclusionBean.getRegistration()
		.getDissertationThesisDiscussedDate() : registrationConclusionBean.getConclusionDate().toLocalDate();

	return result.toString("dd 'de' MMMM 'de' yyyy", Language.getLocale());
    }

    final private String getConclusionStatusAndDegreeType(final DiplomaRequest diplomaRequest, final Registration registration) {
	final StringBuilder result = new StringBuilder();

	final ResourceBundle applicationResources = ResourceBundle.getBundle("resources/ApplicationResources", Language
		.getLocale());

	if (registration.getDegreeType() == DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA) {
	    forDFA(result, applicationResources, diplomaRequest, registration);
	} else {
	    forOthers(result, applicationResources, diplomaRequest, registration);
	}

	return result.toString();
    }

    private void forOthers(StringBuilder result, ResourceBundle applicationResources, final DiplomaRequest diplomaRequest,
	    final Registration registration) {
	final DegreeType degreeType = registration.getDegreeType();

	if (degreeType.hasAnyCycleTypes()) {
	    result.append(enumerationBundle.getString(diplomaRequest.getWhatShouldBeRequestedCycle().getQualifiedName()));
	    result.append(" ").append(applicationResources.getString("of.masculine")).append(" ");
	}

	result.append(degreeType.getPrefix()).append(degreeType.getFilteredName());
    }

    private void forDFA(StringBuilder result, ResourceBundle applicationResources, final DiplomaRequest diplomaRequest,
	    final Registration registration) {
	final DegreeType degreeType = registration.getDegreeType();

	result.append(degreeType.getPrefix()).append(degreeType.getFilteredName());
	if (degreeType.hasExactlyOneCycleType()) {
	    result.append(" (").append(enumerationBundle.getString(degreeType.getCycleType().getQualifiedName())).append(")");
	}
    }

}
