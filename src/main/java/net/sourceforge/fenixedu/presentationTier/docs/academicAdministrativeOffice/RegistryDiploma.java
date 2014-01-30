package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.Degree;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.IRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.RegistryDiplomaRequest;

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

        addParameter("institution", getInstitutionName());
        addParameter("university", getUniversity(getDocumentRequest().getRequestDate()));

        setFirstParagraph(request);
        setSecondParagraph(person, request);
        String thirdParagraph = getResourceBundle().getString("label.phd.registryDiploma.thirdParagraph");

        String dateWord[] = getDateByWords(request.getConclusionDate());

        addParameter("thirdParagraph", MessageFormat.format(thirdParagraph, dateWord[0], dateWord[1], dateWord[2]));
        addParameter("by", getResourceBundle().getString("label.by.university"));
        if (getDocumentRequest().isRequestForRegistration()) {
            setFifthParagraph();
        }

        addParameter("studentName", person.getValidatedName());

        String graduateTitle = request.getGraduateTitle(getLocale());

        if (graduateTitle.contains("Graduated")) {
            graduateTitle = graduateTitle.replace("Graduated", "Licenciado");
        }

        if (graduateTitle.contains("Master")) {
            graduateTitle = graduateTitle.replace("Master", "Mestre");
        }
        addParameter("graduateTitle", graduateTitle);
        setFooter();
    }

    private void setFifthParagraph() {
        String fifthParagraph;
        if (getUniversity(getDocumentRequest().getRequestDate()) != getUniversity(getDocumentRequest().getConclusionDate()
                .toDateTimeAtCurrentTime())) {
            fifthParagraph = getResourceBundle().getString("label.phd.registryDiploma.fifthParagraph.UTL.UL");
        } else {
            fifthParagraph = getResourceBundle().getString("label.phd.registryDiploma.fifthParagraph");
        }
        addParameter("fifthParagraph", MessageFormat.format(fifthParagraph,
                getEnumerationBundle().getString(getDocumentRequest().getFinalAverage(getLocale())), getDocumentRequest()
                        .getFinalAverage(getLocale()),
                getResourceBundle().getString(getDocumentRequest().getQualifiedAverageGrade(getLocale()))));
    }

    protected void setHeader() {

        final CycleType cycleType = getDocumentRequest().getRequestedCycle();
        final String diplomaTypeQualifier;
        if (cycleType == CycleType.FIRST_CYCLE) {
            diplomaTypeQualifier = "label.diploma.type.qualifier.first.cycle";
        } else if (cycleType == CycleType.SECOND_CYCLE) {
            diplomaTypeQualifier = "label.diploma.type.qualifier.second.cycle";
        } else if (cycleType == CycleType.THIRD_CYCLE) {
            diplomaTypeQualifier = "label.diploma.type.qualifier.third.cycle";
        } else {
            diplomaTypeQualifier = "label.diploma.type.qualifier.unknown";
        }

        addParameter("degreeRegistrationDiploma", MessageFormat.format(
                getResourceBundle().getString("label.phd.registryDiploma.degreeRegistrationDiploma"),
                new Object[] { getResourceBundle().getString(diplomaTypeQualifier) }));
        addParameter("portugueseRepublic_1", getResourceBundle().getString("label.phd.registryDiploma.portugueseRepublic.part1"));
        addParameter("portugueseRepublic_2", getResourceBundle().getString("label.phd.registryDiploma.portugueseRepublic.part2"));
    }

    protected void setFirstParagraph(IRegistryDiplomaRequest request) {

        final UniversityUnit university = getUniversity(request.getRequestDate());
        String universityName = university.getPartyName().getPreferedContent();

        final Person rectorIst = university.getInstitutionsUniversityResponsible(FunctionType.PRINCIPAL);

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

        String country;
        if (person.getCountry() != null) {
            country = person.getCountry().getCountryNationality().getContent(getLanguage()).toLowerCase();
        } else {
            throw new DomainException("error.personWithoutParishOfBirth");
        }

        addParameter("secondParagraph", MessageFormat.format(secondParagraph, studentGender,
                getEnumerationBundle().getString(person.getIdDocumentType().getName()), person.getDocumentIdNumber(), country,
                getCycleDescription()));
    }

    protected void setFooter() {
        final UniversityUnit university = getUniversity(getDocumentRequest().getRequestDate());
        final String institutionUnitName = getInstitutionName();
        final Person presidentIst = university.getInstitutionsUniversityResponsible(FunctionType.PRESIDENT);

        final Person rectorIst = university.getInstitutionsUniversityResponsible(FunctionType.PRINCIPAL);

        String presidentGender;

        if (presidentIst.isMale()) {
            presidentGender = getResourceBundle().getString("label.phd.registryDiploma.presidentMale");
            addParameter("presidentName", getResourceBundle().getString("label.phd.registryDiploma.presidentGrantMale") + " "
                    + presidentIst.getName());

        } else {
            presidentGender = getResourceBundle().getString("label.phd.registryDiploma.presidentFemale");
            addParameter("presidentName", getResourceBundle().getString("label.phd.registryDiploma.presidentGrantFemale") + " "
                    + presidentIst.getName());
        }

        String rectorGender;
        if (rectorIst.isMale()) {
            rectorGender = getResourceBundle().getString("label.phd.registryDiploma.rectorMale");
            addParameter("rectorName", getResourceBundle().getString("label.phd.registryDiploma.presidentGrantMale") + " "
                    + rectorIst.getName());
        } else {
            rectorGender = getResourceBundle().getString("label.phd.registryDiploma.rectorFemale");
            addParameter("rectorName", getResourceBundle().getString("label.phd.registryDiploma.presidentGrantFemale") + " "
                    + rectorIst.getName());
        }

        String universityName = university.getPartyName().getPreferedContent();

        addParameter("dateParagraph", getFormatedCurrentDate(universityName));
        addParameter("rector", rectorGender);
        addParameter("president", MessageFormat.format(presidentGender, institutionUnitName));

    }

    private String getFormatedCurrentDate(String universityName) {
        final StringBuilder result = new StringBuilder();
        LocalDate date = new LocalDate();
        String day = Integer.toString(date.getDayOfMonth());
        String month = date.toString("MMMM", getLocale()).toLowerCase();
        result.append(universityName).append(", ");
        result.append(getResourceBundle().getString("label.in"));
        result.append(" " + day + " ");
        result.append(getApplicationBundle().getString("label.of"));
        result.append(" " + month + " ");
        result.append(getApplicationBundle().getString("label.of"));
        result.append(" ").append(date.getYear()).append(".");
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

        String day = Integer.toString(date.getDayOfMonth());
        String month = date.toString("MMMM", getLocale()).toLowerCase();
        String year = Integer.toString(date.getYear());
        String finalDate[] = new String[] { day, month, year };
        return finalDate;

    }

    protected String getCycleDescription() {

        final StringBuilder res = new StringBuilder();
        RegistryDiplomaRequest request = (RegistryDiplomaRequest) getDocumentRequest();
        CycleType cycle = request.getRequestedCycle();
        Degree degree = request.getDegree();
        final DegreeType degreeType = request.getDegreeType();
        if (degreeType.hasAnyCycleTypes()) {
            res.append(cycle.getDescription(getLocale()));
            res.append(" ").append(getResourceBundle().getString("label.of.both")).append(" ");
        }
        if (!degree.isEmpty()) {
            res.append(degreeType.getFilteredName(getLocale()));
        }
        return res.toString();
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
                res.append(" ").append(getResourceBundle().getString("label.of.both")).append(" ");
            }

            if (!degree.isEmpty()) {
                res.append(degreeType.getFilteredName(getLocale()));
                res.append(" ").append(getResourceBundle().getString("label.in")).append(" ");
            }

            res.append(degree.getNameI18N(request.getConclusionYear()).getContent(getLanguage()));
            return res.toString();
        } else if (getDocumentRequest().isRequestForPhd()) {
            PhdRegistryDiplomaRequest request = (PhdRegistryDiplomaRequest) getDocumentRequest();
            final StringBuilder res = new StringBuilder();
            res.append(getResourceBundle().getString("label.phd.doctoral.program.designation"));
            res.append(" ").append(getResourceBundle().getString("label.in"));
            res.append(" ").append(request.getPhdIndividualProgramProcess().getPhdProgram().getName().getContent(getLanguage()));

            return res.toString();
        }

        throw new DomainException("docs.academicAdministrativeOffice.RegistryDiploma.degreeDescription.invalid.request");
    }
}
