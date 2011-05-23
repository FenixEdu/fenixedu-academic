package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.joda.time.LocalDate;

import com.lowagie.text.Document;

public class PhdDiploma extends AdministrativeOfficeDocument {

    private static final long serialVersionUID = 1L;

    protected PhdDiploma(IDocumentRequest documentRequest) {
	super(documentRequest);
    }

    @Override
    protected PhdDiplomaRequest getDocumentRequest() {
	return (PhdDiplomaRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
	addInstitutionParameters();
	addPersonParameters();

	PhdDiplomaRequest diplomaRequest = getDocumentRequest();

	addParameter("registryCode", diplomaRequest.hasRegistryCode() ? diplomaRequest.getRegistryCode().getCode() : null);
	addParameter("conclusionDate", diplomaRequest.getConclusionDate().toString(getDatePattern(), getLocale()));
	addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
	addParameter("day", getFormatedCurrentDate());

	addParameter("classificationResult", diplomaRequest.getThesisFinalGrade().getLocalizedName(getLocale()));
	addParameter("dissertationTitle", diplomaRequest.getDissertationThesisTitle());
	addParameter("graduateTitle", diplomaRequest.getGraduateTitle(getLocale()));
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

    private void addPersonParameters() {
	final Person person = getDocumentRequest().getPerson();
	addParameter("name", StringFormatter.prettyPrint(person.getName()));
	addParameter("nameOfFather", StringFormatter.prettyPrint(person.getNameOfFather()));
	addParameter("nameOfMother", StringFormatter.prettyPrint(person.getNameOfMother()));
	addParameter("birthLocale", getBirthLocale(person, true));
    }

    private void addInstitutionParameters() {
	final UniversityUnit institutionsUniversityUnit = UniversityUnit.getInstitutionsUniversityUnit();
	addParameter("universityName", institutionsUniversityUnit.getName());
	addParameter("universityPrincipalName", institutionsUniversityUnit.getInstitutionsUniversityPrincipal()
		.getValidatedName());
    }

    private void createPhd() {
	Document document = new Document();
	try {

	} catch (Exception e) {

	}
    }

}
