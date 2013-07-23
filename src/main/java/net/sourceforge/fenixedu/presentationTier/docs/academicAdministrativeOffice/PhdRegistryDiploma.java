package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.PhdRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.IRegistryDiplomaRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;

import org.apache.commons.lang.WordUtils;

import pt.utl.ist.fenix.tools.util.i18n.Language;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

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

        addParameter("institution", getMLSTextContent(RootDomainObject.getInstance().getInstitutionUnit().getPartyName()));

        setFirstParagraph(request);
        setSecondParagraph(person, request);

        addParameter("thirdParagraph", getResourceBundle().getString("label.phd.registryDiploma.phdThirdParagraph"));

        String dateWord[] = getDateByWords(request.getConclusionDate());

        String fourthParagraph = getResourceBundle().getString("label.phd.registryDiploma.phdfourthParagraph");

        addParameter("fourthParagraph", MessageFormat.format(fourthParagraph, dateWord[0], dateWord[1], dateWord[2]));

        String fifthParagraph;
        if (getUniversity(getDocumentRequest().getRequestDate()) != getUniversity(getDocumentRequest().getConclusionDate()
                .toDateTimeAtCurrentTime())) {
            fifthParagraph = getResourceBundle().getString("label.phd.registryDiploma.phdFifthParagraph.UTL");
        } else {
            fifthParagraph = getResourceBundle().getString("label.phd.registryDiploma.phdFifthParagraph");
        }

        addParameter("fifthParagraph", MessageFormat.format(fifthParagraph, getDocumentRequest().getFinalAverage(getLocale())));

        setFooter();

    }

    @Override
    protected void setHeader() {
        addParameter("thesisTitle", getThesisTitleI18N().getContent(getLanguage()));
        super.setHeader();
    }

    private MultiLanguageString getThesisTitleI18N() {
        return new MultiLanguageString(Language.pt, getDocumentRequest().getPhdIndividualProgramProcess().getThesisTitle()).with(
                Language.en, getDocumentRequest().getPhdIndividualProgramProcess().getThesisTitleEn());
    }

    @Override
    void setSecondParagraph(Person person, IRegistryDiplomaRequest request) {

        String studentGender;
        if (person.isMale()) {
            studentGender = getResourceBundle().getString("label.phd.registryDiploma.studentHolderMale");
        } else {
            studentGender = getResourceBundle().getString("label.phd.registryDiploma.studentHolderFemale");
        }

        String country;
        String countryUpperCase;
        if (person.getCountry() != null) {
            countryUpperCase = person.getCountry().getCountryNationality().getContent(getLanguage()).toLowerCase();
            country = WordUtils.capitalize(countryUpperCase);
        } else {
            throw new DomainException("error.personWithoutParishOfBirth");
        }

        PhdRegistryDiplomaRequest phdRequest = getDocumentRequest();

        String secondParagraph = getResourceBundle().getString("label.phd.registryDiploma.phdSecondParagraph");
        addParameter("secondParagraph", MessageFormat.format(secondParagraph, studentGender,
                getEnumerationBundle().getString(person.getIdDocumentType().getName()), person.getDocumentIdNumber(), country,
                phdRequest.getPhdIndividualProgramProcess().getPhdProgram().getName().getContent(getLanguage())));
    }

}
