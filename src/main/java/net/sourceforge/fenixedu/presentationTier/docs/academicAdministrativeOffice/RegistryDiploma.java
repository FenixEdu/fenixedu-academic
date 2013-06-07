package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.IRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;

public class RegistryDiploma extends AdministrativeOfficeDocument {
    private static final long serialVersionUID = 7788392282506503345L;

    protected RegistryDiploma(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected IRegistryDiplomaRequest getDocumentRequest() {
        return (IRegistryDiplomaRequest) super.getDocumentRequest();
    }

    @Override
    protected void fillReport() {
        super.fillReport();
        IRegistryDiplomaRequest request = getDocumentRequest();
        Person person = request.getPerson();

        setHeader();

        final Unit institutionUnit = RootDomainObject.getInstance().getInstitutionUnit();
        String institutionUnitName = getMLSTextContent(institutionUnit.getPartyName());
        addParameter("institution", getMLSTextContent(RootDomainObject.getInstance().getInstitutionUnit().getPartyName()));

        final Person presidentIst =
                UniversityUnit.getInstitutionsUniversityUnit().getInstitutionsUniversityResponsible(FunctionType.PRESIDENT);

        final Person rectorIst =
                UniversityUnit.getInstitutionsUniversityUnit().getInstitutionsUniversityResponsible(FunctionType.PRINCIPAL);

        String presidentGender;

        if (presidentIst.isMale()) {
            presidentGender = getResourceBundle().getString("label.phd.registryDiploma.presidentMale");
        } else {
            presidentGender = getResourceBundle().getString("label.phd.registryDiploma.presidentFemale");
        }

        String rectorGender;
        if (rectorIst.isMale()) {
            rectorGender = getResourceBundle().getString("label.phd.registryDiploma.rectorMale");
        } else {
            rectorGender = getResourceBundle().getString("label.phd.registryDiploma.rectorFemale");
        }

        setFirstParagraph(request);
        setSecondParagraph(person, request);
        String thirdParagraph = getResourceBundle().getString("label.phd.registryDiploma.thirdParagraph");

        String dateWord[] = getDateByWords(request.getConclusionDate());

        addParameter("thirdParagraph", MessageFormat.format(thirdParagraph, dateWord[0], dateWord[1], dateWord[2]));

        String fifthParagraph = getResourceBundle().getString("label.phd.registryDiploma.fifthParagraph");
        addParameter("fifthParagraph", MessageFormat.format(fifthParagraph, getDocumentRequest().getFinalAverage(getLocale()),
                getResourceBundle().getString(getDocumentRequest().getQualifiedAverageGrade(getLocale()))));

        addParameter("studentName", person.getValidatedName());

        addParameter("graduateTitle", request.getGraduateTitle(getLocale()));

        setFooter(institutionUnitName, rectorGender, presidentGender);
    }

    protected void setHeader() {

        addParameter("degreeRegistrationDiploma",
                getResourceBundle().getString("label.phd.registryDiploma.degreeRegistrationDiploma"));
        addParameter("portugueseRepublic_1", getResourceBundle().getString("label.phd.registryDiploma.portugueseRepublic.part1"));
        addParameter("portugueseRepublic_2", getResourceBundle().getString("label.phd.registryDiploma.portugueseRepublic.part2"));
    }

    protected void setFirstParagraph(IRegistryDiplomaRequest request) {

        final UniversityUnit university = UniversityUnit.getInstitutionsUniversityUnit();
        String universityName = university.getPartyName().getPreferedContent();

        final Person rectorIst =
                UniversityUnit.getInstitutionsUniversityUnit().getInstitutionsUniversityResponsible(FunctionType.PRINCIPAL);

        String rectorGender, rectorGrant;

        if (rectorIst.isMale()) {
            rectorGender = getResourceBundle().getString("label.phd.registryDiploma.rectorMale");
            rectorGrant = getResourceBundle().getString("label.phd.registryDiploma.presidentGrantMale");
        } else {
            rectorGender = getResourceBundle().getString("label.phd.registryDiploma.rectorFemale");
            rectorGrant = getResourceBundle().getString("label.phd.registryDiploma.presidentGrantFemale");
        }

        String firstParagraph = getResourceBundle().getString("label.phd.registryDiploma.firstParagraph");
        addParameter("firstParagraph", MessageFormat.format(firstParagraph, rectorGender, universityName, rectorGrant,
                rectorIst.getValidatedName(), request.getRegistryCode().getCode()));
    }

    void setSecondParagraph(Person person, IRegistryDiplomaRequest request) {

        String studentGender;
        if (person.isMale()) {
            studentGender = getResourceBundle().getString("label.phd.registryDiploma.studentHolderMale");
        } else {
            studentGender = getResourceBundle().getString("label.phd.registryDiploma.studentHolderFemale");
        }

        String secondParagraph = getResourceBundle().getString("label.phd.registryDiploma.secondParagraph");

        String parishOfBirth;
        if (person.getParishOfBirth() != null && !person.getParishOfBirth().isEmpty()) {
            parishOfBirth = person.getParishOfBirth();
        } else {
            throw new DomainException("error.personWithoutParishOfBirth");
        }

        addParameter("secondParagraph", MessageFormat.format(secondParagraph, studentGender,
                getEnumerationBundle().getString(person.getIdDocumentType().getName()), person.getDocumentIdNumber(),
                parishOfBirth, getDegreeDescription()));
    }

    protected void setFooter(String institutionUnitName, String rectorGender, String presidentGender) {

        final UniversityUnit university = UniversityUnit.getInstitutionsUniversityUnit();
        String universityName = university.getPartyName().getPreferedContent();

        addParameter("dateParagraph", getFormatedCurrentDate(universityName));
        addParameter("rector", rectorGender);
        addParameter("president", MessageFormat.format(presidentGender, institutionUnitName));
    }

    private String getFormatedCurrentDate(String universityName) {
        final StringBuilder result = new StringBuilder();
        result.append(universityName);
        result.append(", ");
        result.append(new LocalDate().toString(getDatePattern(), getLocale()));
        result.append(".");
        return result.toString();
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

    protected String[] getDateByWords(LocalDate date) {

        String dayOrdinal = Integer.toString(date.getDayOfMonth()) + ".ordinal";
        String day = getEnumerationBundle().getString(dayOrdinal);
        String month = date.toString("MMMM", getLocale());
        String year = getEnumerationBundle().getString(Integer.toString(date.getYear()));
        String finalDate[] = new String[] { day, month, year };
        return finalDate;

    }

    @Override
    protected String getDegreeDescription() {

        if (getDocumentRequest().isRequestForRegistration()) {
            final StringBuilder res = new StringBuilder();
            RegistryDiplomaRequest request = (RegistryDiplomaRequest) getDocumentRequest();
            CycleType cycle = request.getRequestedCycle();
            Degree degree = request.getDegree();
            final DegreeType degreeType = request.getDegreeType();
            if (degreeType.hasAnyCycleTypes()) {
                res.append(cycle.getDescription(getLocale()));
                res.append(StringUtils.SINGLE_SPACE).append(getResourceBundle().getString("label.of.both"))
                        .append(StringUtils.SINGLE_SPACE);
            }

            if (!degree.isEmpty()) {
                res.append(degreeType.getFilteredName(getLocale()));
                res.append(StringUtils.SINGLE_SPACE).append(getResourceBundle().getString("label.in"))
                        .append(StringUtils.SINGLE_SPACE);
            }

            res.append(degree.getNameI18N(request.getConclusionYear()).getContent(getLanguage()));
            return res.toString();
        } else if (getDocumentRequest().isRequestForPhd()) {
            PhdRegistryDiplomaRequest request = (PhdRegistryDiplomaRequest) getDocumentRequest();
            final StringBuilder res = new StringBuilder();
            res.append(getResourceBundle().getString("label.phd.doctoral.program.designation"));
            res.append(StringUtils.SINGLE_SPACE).append(getResourceBundle().getString("label.in"));
            res.append(StringUtils.SINGLE_SPACE).append(
                    request.getPhdIndividualProgramProcess().getPhdProgram().getName().getContent(getLanguage()));

            return res.toString();
        }

        throw new DomainException("docs.academicAdministrativeOffice.RegistryDiploma.degreeDescription.invalid.request");
    }
}
