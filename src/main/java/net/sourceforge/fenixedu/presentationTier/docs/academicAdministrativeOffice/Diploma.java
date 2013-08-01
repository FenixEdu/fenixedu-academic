package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.StringFormatter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
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

        String universityName = getUniversity(diplomaRequest.getRequestDate()).getPartyName().getPreferedContent();

        addParameter("registryCode", diplomaRequest.hasRegistryCode() ? diplomaRequest.getRegistryCode().getCode() : null);
        addParameter("documentNumber", getResourceBundle().getString("label.diploma.documentNumber"));
        addParameter("conclusionDate", diplomaRequest.getConclusionDate().toString(getDatePattern(), getLocale()));
        addParameter("day", MessageFormat.format(getResourceBundle().getString("label.diploma.university.actualDate"),
                universityName, getFormatedCurrentDate()));

        if (diplomaRequest.hasFinalAverageDescription()) {
            addParameter("finalAverageDescription",
                    StringUtils.capitalize(getEnumerationBundle().getString(diplomaRequest.getFinalAverage().toString())));
            addParameter("finalAverageQualified", diplomaRequest.getFinalAverageQualified());
        } else if (diplomaRequest.hasDissertationTitle()) {
            addParameter("dissertationTitle", diplomaRequest.getDissertationThesisTitle());
        }

        String finalAverage = getResourceBundle().getString("diploma.finalAverage");
        addParameter("finalAverageDescription", MessageFormat.format(finalAverage,
                getEnumerationBundle().getString(diplomaRequest.getFinalAverage().toString()), diplomaRequest.getFinalAverage()
                        .toString(), getResourceBundle().getString(getQualifiedAverageGrade(getLocale()))));

        addParameter("conclusionStatus", MessageFormat.format(getResourceBundle().getString("label.diploma.conclusionStatus"),
                getConclusionStatusAndDegreeType(diplomaRequest, getRegistration())));
        addParameter("degreeFilteredName", diplomaRequest.getDegreeFilteredName());

        String graduateTitle = diplomaRequest.getGraduateTitle(getLocale());

        if (graduateTitle.contains("Graduated")) {
            graduateTitle = graduateTitle.replace("Graduated", "Licenciado");
        }

        if (graduateTitle.contains("Master")) {
            graduateTitle = graduateTitle.replace("Master", "Mestre");
        }

        addParameter("graduateTitle", graduateTitle);
        addParameter("message1", getResourceBundle().getString("label.diploma.message1"));
        addParameter("message2", getResourceBundle().getString("label.diploma.message2"));
        addParameter("message3", getResourceBundle().getString("label.diploma.message3"));

    }

    @Override
    protected void addIntroParameters() {
        super.addIntroParameters();

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

        addParameter("universityPrincipal", principal);
        addParameter("universityPrincipalName", principal.getValidatedName());

        if (getUniversity(getDocumentRequest().getRequestDate()) != getUniversity(getDocumentRequest().getConclusionDate()
                .toDateTimeAtCurrentTime())) {
            addParameter("UTLDescription", getResourceBundle().getString("label.diploma.UTLDescription"));
            addParameter("certification", getResourceBundle().getString("label.diploma.certification.UTL"));
        } else {
            addParameter("UTLDescription", StringUtils.EMPTY);
            addParameter("certification", getResourceBundle().getString("label.diploma.certification.UL"));
        }
    }

    @Override
    protected void setPersonFields() {
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

        result.append(degreeType.getPrefix(getLocale())).append(degreeType.getFilteredName(getLocale()));
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

    public String getQualifiedAverageGrade(final Locale locale) {
        Integer finalAverage = getDocumentRequest().getFinalAverage();

        String qualifiedAverageGrade;

        if (finalAverage <= 13) {
            qualifiedAverageGrade = "sufficient";
        } else if (finalAverage <= 15) {
            qualifiedAverageGrade = "good";
        } else if (finalAverage <= 17) {
            qualifiedAverageGrade = "verygood";
        } else {
            qualifiedAverageGrade = "excelent";
        }

        return "diploma.supplement.qualifiedgrade." + qualifiedAverageGrade;
    }

}
