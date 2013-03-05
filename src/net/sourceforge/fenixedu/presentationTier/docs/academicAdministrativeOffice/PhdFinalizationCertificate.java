package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.phd.PhdFinalizationCertificateRequestPR;
import net.sourceforge.fenixedu.domain.organizationalStructure.UniversityUnit;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess;
import net.sourceforge.fenixedu.domain.phd.serviceRequests.documentRequests.certificates.PhdFinalizationCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

import org.joda.time.LocalDate;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class PhdFinalizationCertificate extends AdministrativeOfficeDocument {

    static final protected int LINE_LENGTH = 70;

    PhdFinalizationCertificate(IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected PhdFinalizationCertificateRequest getDocumentRequest() {
        return (PhdFinalizationCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected String getDegreeDescription() {
        PhdIndividualProgramProcess phdIndividualProgramProcess = getDocumentRequest().getPhdIndividualProgramProcess();
        return phdIndividualProgramProcess.getPhdProgram().getName().getContent(getLanguage());
    }

    @Override
    protected void addPriceFields() {
        AcademicServiceRequestEvent event = getDocumentRequest().getEvent();
        PhdFinalizationCertificateRequestPR postingRule = (PhdFinalizationCertificateRequestPR) event.getPostingRule();
        addParameter("originalAmount", postingRule.getFixedAmount().toString());
        addParameter("urgentAmount",
                getDocumentRequest().isUrgentRequest() ? postingRule.getFixedAmount().toString() : Money.ZERO.toString());
        addParameter("totalAmount", event.getOriginalAmountToPay().toString());
    }

    @Override
    protected void setPersonFields() {
        final Person person = getDocumentRequest().getPerson();

        StringBuilder builder1 = new StringBuilder();

        if (getLanguage().equals(Language.pt)) {
            builder1.append(getResourceBundle().getString("label.with")).append(SINGLE_SPACE);
        }

        builder1.append(getResourceBundle().getString("label.phd.finalization.certificate.identity.card"));
        builder1.append(SINGLE_SPACE).append(getResourceBundle().getString("label.number.short"));
        builder1.append(SINGLE_SPACE).append(person.getDocumentIdNumber());

        StringBuilder builder2 = new StringBuilder();
        builder2.append(getResourceBundle().getString("documents.birthLocale"));
        builder2.append(SINGLE_SPACE).append(getBirthLocale(person, false));

        if (getDocumentRequest().getDocumentRequestType().equals(DocumentRequestType.APPROVEMENT_CERTIFICATE)) {
            addParameter("name", person.getName().toUpperCase());
            addParameter("documentIdNumber", builder1.toString());
            addParameter("birthLocale", builder2.toString());
        } else {
            addParameter("name", StringUtils.multipleLineRightPad(person.getName().toUpperCase(), LINE_LENGTH, END_CHAR));
            addParameter("documentIdNumber", StringUtils.multipleLineRightPad(builder1.toString(), LINE_LENGTH, END_CHAR));
            addParameter("birthLocale", StringUtils.multipleLineRightPad(builder2.toString(), LINE_LENGTH, END_CHAR));
        }

        setNationality(person);
    }

    @Override
    protected void setNationality(final Person person) {
        StringBuilder builder = new StringBuilder();
        if (getLanguage().equals(Language.pt)) {
            builder.append(getResourceBundle().getString("label.and")).append(SINGLE_SPACE);
        }

        builder.append(getResourceBundle().getString("documents.nationality.one"));
        final String nationality = person.getCountry().getFilteredNationality(getLocale());
        builder.append(SINGLE_SPACE).append(nationality.toUpperCase()).append(SINGLE_SPACE);

        if (getDocumentRequest().getDocumentRequestType().equals(DocumentRequestType.APPROVEMENT_CERTIFICATE)) {
            addParameter("nationality", builder.toString());
        } else {
            addParameter("nationality", StringUtils.multipleLineRightPad(builder.toString(), LINE_LENGTH, END_CHAR));
        }
    }

    @Override
    protected void fillReport() {
        super.fillReport();

        addPersonalInfo();

        addProgrammeInfo();
    }

    private void addProgrammeInfo() {
        PhdIndividualProgramProcess phdIndividualProgramProcess = getDocumentRequest().getPhdIndividualProgramProcess();
        addParameter("conclusionDate", phdIndividualProgramProcess.getConclusionDate().toString("dd/MM/yyyy"));
        String thesisFinalGrade = phdIndividualProgramProcess.getFinalGrade().getLocalizedName(getLocale());
        addParameter("thesisFinalGrade", thesisFinalGrade);
        addParameter("thesisTitle", getThesisTitle(phdIndividualProgramProcess));
        addParameter("studentNumber", phdIndividualProgramProcess.getStudent().getNumber());

        StringBuilder builder = new StringBuilder();
        builder.append(
                getResourceBundle().getString("message.phd.finalization.certificate.made.thesis.presentation.on.doctoral.grade"))
                .append(":").append(SINGLE_SPACE);

        builder.append(phdIndividualProgramProcess.getPhdProgram().getName().getContent(getLanguage()).toUpperCase());

        addParameter("phdProgram", customMultipleLineRightPad(builder.toString(), LINE_LENGTH, END_CHAR));
        addParameter("finalizationInfo", buildFinalizationInfo());

        addParameter("serviceRequestNumberYear", getDocumentRequest().getServiceRequestNumberYear());

        if (getLanguage().equals(Language.en)) {
            LocalDate localDate = new LocalDate();
            addParameter(
                    "day",
                    String.format(localDate.toString("MMMM, dd", getLocale()) + findNumeralSufixForDay(localDate.getDayOfMonth())
                            + localDate.toString(", yyyy")));
        } else {
            LocalDate localDate = new LocalDate();
            StringBuilder dayBuilder = new StringBuilder();
            dayBuilder.append(localDate.toString("dd")).append(SINGLE_SPACE).append("de").append(SINGLE_SPACE);
            dayBuilder.append(localDate.toString("MMMM", getLocale())).append(SINGLE_SPACE).append("de").append(SINGLE_SPACE);
            dayBuilder.append(localDate.toString("yyyy"));

            addParameter("day", dayBuilder.toString());
        }

        addParameter("universityName",
                getMLSTextContent(UniversityUnit.getInstitutionsUniversityUnit().getPartyName(), Language.pt));
    }

    private String getThesisTitle(final PhdIndividualProgramProcess phdIndividualProgramProcess) {
        if (getLanguage().equals(Language.en) && !StringUtils.isEmpty(phdIndividualProgramProcess.getThesisTitleEn())) {
            return phdIndividualProgramProcess.getThesisTitleEn();
        }

        return phdIndividualProgramProcess.getThesisTitle();
    }

    private String findNumeralSufixForDay(int dayOfMonth) {

        if (dayOfMonth == 1 || dayOfMonth == 21 || dayOfMonth == 31) {
            return "st";
        }

        if (dayOfMonth == 2 || dayOfMonth == 22) {
            return "nd";
        }

        if (dayOfMonth == 3 || dayOfMonth == 23) {
            return "rd";
        }

        return "th";
    }

    private String buildFinalizationInfo() {
        PhdIndividualProgramProcess phdIndividualProgramProcess = getDocumentRequest().getPhdIndividualProgramProcess();
        String thesisFinalGrade = phdIndividualProgramProcess.getFinalGrade().getLocalizedName(getLocale());

        if (phdIndividualProgramProcess.isBolonha() && phdIndividualProgramProcess.hasRegistryDiplomaRequest()) {
            return String
                    .format(getResourceBundle().getString(
                            "message.phd.finalization.info.thesis.grade.approved.by.jury.registry.diploma"), thesisFinalGrade,
                            phdIndividualProgramProcess.getRegistryDiplomaRequest().getRegistryCode().getCode());
        } else {
            return String.format(getResourceBundle().getString("message.phd.finalization.info.thesis.grade.approved.by.jury"),
                    thesisFinalGrade);
        }
    }

    private void addPersonalInfo() {
        Person person = getDocumentRequest().getPerson();

        String fatherPrefixKey = "label.phd.finalization.certificate.father.prefix";
        String motherPrefixKey = "label.phd.finalization.certificate.mother.prefix";

        if (Gender.MALE.equals(person.getGender())) {
            fatherPrefixKey += ".for.male";
            motherPrefixKey += ".for.male";
        } else {
            fatherPrefixKey += ".for.female";
            motherPrefixKey += ".for.female";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(getResourceBundle().getString(fatherPrefixKey)).append(SINGLE_SPACE);
        builder.append(person.getNameOfFather().toUpperCase());

        addParameter("fatherName", StringUtils.multipleLineRightPad(builder.toString(), LINE_LENGTH, END_CHAR));

        builder = new StringBuilder();
        builder.append(getResourceBundle().getString(motherPrefixKey)).append(SINGLE_SPACE);
        builder.append(person.getNameOfMother().toUpperCase());
        addParameter("motherName", StringUtils.multipleLineRightPad(builder.toString(), LINE_LENGTH, END_CHAR));

    }

    private static String customMultipleLineRightPad(String field, int LINE_LENGTH, char fillPaddingWith) {
        if (!org.apache.commons.lang.StringUtils.isEmpty(field) && !field.endsWith(" ")) {
            field += " ";
        }

        if (field.length() < LINE_LENGTH) {
            return org.apache.commons.lang.StringUtils.rightPad(field, LINE_LENGTH, fillPaddingWith);
        } else {
            final List<String> words = Arrays.asList(field.split(" "));
            int currentLineLength = 0;
            String result = StringUtils.EMPTY;

            for (final String word : words) {
                final String toAdd = word + " ";

                if (currentLineLength + toAdd.length() > LINE_LENGTH) {
                    result = org.apache.commons.lang.StringUtils.rightPad(result, LINE_LENGTH, SINGLE_SPACE) + '\n';
                    currentLineLength = toAdd.length();
                } else {
                    currentLineLength += toAdd.length();
                }

                result += toAdd;
            }

            if (currentLineLength < LINE_LENGTH) {
                return org.apache.commons.lang.StringUtils.rightPad(result, result.length() + (LINE_LENGTH - currentLineLength),
                        fillPaddingWith);
            }

            return result;
        }
    }

}
