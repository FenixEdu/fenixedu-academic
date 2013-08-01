package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.joda.time.LocalDate;

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
        final UniversityUnit university = getUniversity(getDocumentRequest().getRequestDate());
        String universityName = university.getPartyName().getPreferedContent();

        PhdDiplomaRequest diplomaRequest = getDocumentRequest();
        String phdProgram = getResourceBundle().getString("label.phd.diploma.pdhProgram");
        String phdProgramDescription =
                getDocumentRequest().getPhdIndividualProgramProcess().getPhdProgram().getDegree().getNameI18N()
                        .getContent(getLanguage());

        addParameter(
                "phdProgram",
                MessageFormat.format(phdProgram, phdProgramDescription,
                        diplomaRequest.getConclusionDate().toString(getDatePattern(), getLocale())));

        addParameter("documentNumber", getResourceBundle().getString("label.diploma.documentNumber"));
        addParameter("registryCode", diplomaRequest.hasRegistryCode() ? diplomaRequest.getRegistryCode().getCode() : null);
        addParameter("conclusionDate", diplomaRequest.getConclusionDate().toString(getDatePattern(), getLocale()));
        addParameter("institutionName", RootDomainObject.getInstance().getInstitutionUnit().getName());
        addParameter("day", MessageFormat.format(getResourceBundle().getString("label.diploma.university.actualDate"),
                universityName, getFormatedCurrentDate()));

        addParameter("classificationResult", MessageFormat.format(
                getResourceBundle().getString("label.phd.Diploma.classificationResult"), diplomaRequest.getThesisFinalGrade()
                        .getLocalizedName(getLocale())));
        addParameter("dissertationTitle", diplomaRequest.getDissertationThesisTitle());
        addParameter("graduateTitle", diplomaRequest.getGraduateTitle(getLocale()));

        if (getUniversity(getDocumentRequest().getRequestDate()) != getUniversity(getDocumentRequest().getConclusionDate()
                .toDateTimeAtCurrentTime())) {
            addParameter("UTLDescription", getResourceBundle().getString("label.diploma.UTLDescription"));
            addParameter("certification", getResourceBundle().getString("label.diploma.phd.certification.UTL"));

        } else {
            addParameter("UTLDescription", StringUtils.EMPTY);
            addParameter("certification", getResourceBundle().getString("label.diploma.phd.certification.UL"));
        }

        addParameter("message1", getResourceBundle().getString("label.diploma.message1"));
        addParameter("message3", getResourceBundle().getString("label.diploma.message3"));
        addParameter("phdmessage1", getResourceBundle().getString("label.phd.diploma.message1"));
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

        String country;
        String countryUpperCase;
        if (person.getCountry() != null) {
            countryUpperCase = person.getCountry().getCountryNationality().getContent(getLanguage()).toLowerCase();
            country = WordUtils.capitalize(countryUpperCase);
        } else {
            throw new DomainException("error.personWithoutParishOfBirth");
        }

        String nationality = getResourceBundle().getString("diploma.nationality");
        addParameter("birthLocale", MessageFormat.format(nationality, country));

    }

    private void addInstitutionParameters() {

        addParameter("universityName", getUniversity(getDocumentRequest().getRequestDate()).getName());
        addParameter("universityPrincipal", getUniversity(getDocumentRequest().getRequestDate())
                .getInstitutionsUniversityResponsible(FunctionType.PRINCIPAL));

        final String institutionUnitName = getInstitutionName();

        Person principal =
                getUniversity(getDocumentRequest().getRequestDate()).getInstitutionsUniversityResponsible(FunctionType.PRINCIPAL);
        final Person presidentIst =
                getUniversity(getDocumentRequest().getRequestDate()).getInstitutionsUniversityResponsible(FunctionType.PRESIDENT);

        final UniversityUnit university = getUniversity(getDocumentRequest().getRequestDate());
        String universityName = university.getPartyName().getPreferedContent();

        String rectorGender, rectorGrant, presidentGender;

        if (presidentIst.isMale()) {
            presidentGender = getResourceBundle().getString("label.phd.registryDiploma.presidentMale");
        } else {
            presidentGender = getResourceBundle().getString("label.phd.registryDiploma.presidentFemale");
        }

        if (principal.isMale()) {
            rectorGender = getResourceBundle().getString("label.phd.registryDiploma.rectorMale");
            rectorGrant = getResourceBundle().getString("label.phd.registryDiploma.presidentGrantMale");
        } else {
            rectorGender = getResourceBundle().getString("label.phd.registryDiploma.rectorFemale");
            rectorGrant = getResourceBundle().getString("label.phd.registryDiploma.presidentGrantFemale");
        }
        addParameter("theRector", rectorGender);
        addParameter("president", MessageFormat.format(presidentGender, institutionUnitName));
        String firstParagraph = getResourceBundle().getString("label.diploma.universityPrincipal");
        addParameter("firstParagraph",
                MessageFormat.format(firstParagraph, rectorGender, universityName, rectorGrant, principal.getValidatedName()));
    }

}
