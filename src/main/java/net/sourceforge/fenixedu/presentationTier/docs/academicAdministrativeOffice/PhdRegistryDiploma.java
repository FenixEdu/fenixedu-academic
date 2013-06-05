package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.FunctionType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.IRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;

public class PhdRegistryDiploma extends RegistryDiploma {

    private static final long serialVersionUID = 1L;

    protected PhdRegistryDiploma(IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected PhdRegistryDiplomaRequest getDocumentRequest() {
        return (PhdRegistryDiplomaRequest) super.getDocumentRequest();
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

        final Person rectorIst =
                UniversityUnit.getInstitutionsUniversityUnit().getInstitutionsUniversityResponsible(FunctionType.PRINCIPAL);

        final Person presidentIst =
                UniversityUnit.getInstitutionsUniversityUnit().getInstitutionsUniversityResponsible(FunctionType.PRESIDENT);

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

        String thirdParagraph = getResourceBundle().getString("label.phd.registryDiploma.phdThirdParagraph");

        String dateWord[] = getDateByWords(request.getConclusionDate());

        addParameter("thirdParagraph", MessageFormat.format(thirdParagraph, dateWord[0], dateWord[1], dateWord[2]));

        addParameter("fourthParagraph", getResourceBundle().getString("label.phd.registryDiploma.fourthParagraph"));

        String fifthParagraph = getResourceBundle().getString("label.phd.registryDiploma.phdFifthParagraph");
        addParameter("fifthParagraph", MessageFormat.format(fifthParagraph, getDocumentRequest().getFinalAverage(getLocale())));

        setFooter(institutionUnitName, rectorGender, presidentGender);

    }

    @Override
    protected void setHeader() {
        addParameter("thesisTitle", getDocumentRequest().getPhdIndividualProgramProcess().getThesisTitle());
        super.setHeader();
    }

    @Override
    void setSecondParagraph(Person person, IRegistryDiplomaRequest request) {

        String studentGender;
        if (person.isMale()) {
            studentGender = getResourceBundle().getString("label.phd.registryDiploma.studentHolderMale");
        } else {
            studentGender = getResourceBundle().getString("label.phd.registryDiploma.studentHolderFemale");
        }

        String parishOfBirth;
        if (person.getParishOfBirth() != null && !person.getParishOfBirth().isEmpty()) {
            parishOfBirth = person.getParishOfBirth();
        } else {
            throw new DomainException("error.personWithoutParishOfBirth");
        }

        PhdRegistryDiplomaRequest phdRequest = getDocumentRequest();
        CycleType cycle = request.getRequestedCycle();

        String secondParagraph = getResourceBundle().getString("label.phd.registryDiploma.phdSecondParagraph");
        addParameter("secondParagraph", MessageFormat.format(secondParagraph, studentGender,
                getEnumerationBundle().getString(person.getIdDocumentType().getName()), person.getDocumentIdNumber(),
                parishOfBirth, cycle.getDescription((getLocale())), phdRequest.getPhdIndividualProgramProcess().getPhdProgram()
                        .getName().getContent(getLanguage())));
    }

}
