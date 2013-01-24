package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.lang.StringUtils;
import org.joda.time.LocalDate;

public class Diploma extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 1L;

    protected Diploma(final IDocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected DiplomaRequest getDocumentRequest() {
	return (DiplomaRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
	super.fillReport();
	final DiplomaRequest diplomaRequest = getDocumentRequest();

	addParameter("registryCode", diplomaRequest.hasRegistryCode() ? diplomaRequest.getRegistryCode().getCode() : null);

	addParameter("conclusionDate", diplomaRequest.getConclusionDate().toString(getDatePattern(), getLocale()));
	addParameter("day", getFormatedCurrentDate());

	if (diplomaRequest.hasFinalAverageDescription()) {
	    addParameter("finalAverageDescription",
		    StringUtils.capitalize(getEnumerationBundle().getString(diplomaRequest.getFinalAverage().toString())));
	    addParameter("finalAverageQualified", diplomaRequest.getFinalAverageQualified());
	} else if (diplomaRequest.hasDissertationTitle()) {
	    addParameter("dissertationTitle", diplomaRequest.getDissertationThesisTitle());
	}

	addParameter("conclusionStatus", getConclusionStatusAndDegreeType(diplomaRequest, getRegistration()));
	addParameter("degreeFilteredName", diplomaRequest.getDegreeFilteredName());

	addParameter("graduateTitle", diplomaRequest.getGraduateTitle(getLocale()));
    }

    @Override
    protected void addIntroParameters() {
	super.addIntroParameters();
	Person principal = UniversityUnit.getInstitutionsUniversityUnit().getInstitutionsUniversityPrincipal();
	addParameter("universityPrincipal", principal);
	addParameter("universityPrincipalName", principal.getValidatedName());
    }

    @Override
    protected void setPersonFields() {
	final Person person = getDocumentRequest().getPerson();
	addParameter("name", StringFormatter.prettyPrint(person.getName()));
	addParameter("nameOfFather", StringFormatter.prettyPrint(person.getNameOfFather()));
	addParameter("nameOfMother", StringFormatter.prettyPrint(person.getNameOfMother()));
	addParameter("birthLocale", getBirthLocale(person, true));
    }

    private String getFormatedCurrentDate() {
	return new LocalDate().toString(getDatePattern(), getLocale());
    }

    private String getDatePattern() {
	final StringBuilder result = new StringBuilder();
	result.append("dd '");
	result.append(getApplicationBundle().getString("label.of"));
	result.append("' MMMM '");
	result.append(getApplicationBundle().getString("label.of"));
	result.append("' yyyy");
	return result.toString();
    }

    final private String getConclusionStatusAndDegreeType(final DiplomaRequest diplomaRequest, final Registration registration) {
	final StringBuilder result = new StringBuilder();

	if (registration.getDegreeType() == DegreeType.BOLONHA_ADVANCED_FORMATION_DIPLOMA
		|| registration.getDegreeType() == DegreeType.BOLONHA_ADVANCED_SPECIALIZATION_DIPLOMA) {
	    forDFA(result, getApplicationBundle(), diplomaRequest, registration);
	} else {
	    forOthers(result, getApplicationBundle(), diplomaRequest, registration);
	}

	return result.toString();
    }

    private void forOthers(StringBuilder result, ResourceBundle applicationResources, final DiplomaRequest diplomaRequest,
	    final Registration registration) {
	final DegreeType degreeType = registration.getDegreeType();

	if (degreeType.hasAnyCycleTypes()) {
	    result.append(getEnumerationBundle().getString(diplomaRequest.getWhatShouldBeRequestedCycle().getQualifiedName()));
	    result.append(SINGLE_SPACE).append(applicationResources.getString("of.masculine")).append(SINGLE_SPACE);
	}

	result.append(degreeType.getPrefix()).append(degreeType.getFilteredName());
    }

    private void forDFA(StringBuilder result, ResourceBundle applicationResources, final DiplomaRequest diplomaRequest,
	    final Registration registration) {
	final DegreeType degreeType = registration.getDegreeType();

	result.append(degreeType.getPrefix()).append(degreeType.getFilteredName());
	if (degreeType.hasExactlyOneCycleType()) {
	    result.append(" (").append(getEnumerationBundle().getString(degreeType.getCycleType().getQualifiedName()))
		    .append(")");
	}
    }

}
