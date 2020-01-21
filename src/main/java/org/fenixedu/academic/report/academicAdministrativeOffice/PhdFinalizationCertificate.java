/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.report.academicAdministrativeOffice;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.events.serviceRequests.AcademicServiceRequestEvent;
import org.fenixedu.academic.domain.accounting.postingRules.serviceRequests.phd.PhdFinalizationCertificateRequestPR;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.serviceRequests.documentRequests.certificates.PhdFinalizationCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.FenixStringTools;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class PhdFinalizationCertificate extends AdministrativeOfficeDocument {

    static final protected int LINE_LENGTH = 70;

    PhdFinalizationCertificate(IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    public PhdFinalizationCertificateRequest getDocumentRequest() {
        return (PhdFinalizationCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected String getDegreeDescription() {
        final PhdIndividualProgramProcess phdIndividualProgramProcess = getDocumentRequest().getPhdIndividualProgramProcess();
        final ExecutionYear executionYear = phdIndividualProgramProcess.getExecutionYear();
        return phdIndividualProgramProcess.getPhdProgram().getName(executionYear).getContent(getLanguage());
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

        if (getLanguage().equals(org.fenixedu.academic.util.LocaleUtils.PT)) {
            builder1.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.with")).append(SINGLE_SPACE);
        }

        builder1.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.finalization.certificate.identity.card"));
        builder1.append(SINGLE_SPACE).append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.number.short"));
        builder1.append(SINGLE_SPACE).append(person.getDocumentIdNumber());

        StringBuilder builder2 = new StringBuilder();
        builder2.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "documents.birthLocale"));
        builder2.append(SINGLE_SPACE).append(getBirthLocale(person, false));

        if (getDocumentRequest().getDocumentRequestType().equals(DocumentRequestType.APPROVEMENT_MOBILITY_CERTIFICATE)) {
            addParameter("name", person.getName().toUpperCase());
            addParameter("documentIdNumber", builder1.toString());
            addParameter("birthLocale", builder2.toString());
        } else {
            addParameter("name", FenixStringTools.multipleLineRightPad(person.getName().toUpperCase(), LINE_LENGTH, END_CHAR));
            addParameter("documentIdNumber", FenixStringTools.multipleLineRightPad(builder1.toString(), LINE_LENGTH, END_CHAR));
            addParameter("birthLocale", FenixStringTools.multipleLineRightPad(builder2.toString(), LINE_LENGTH, END_CHAR));
        }

        setNationality(person);
    }

    @Override
    protected void setNationality(final Person person) {
        StringBuilder builder = new StringBuilder();
        if (getLanguage().equals(org.fenixedu.academic.util.LocaleUtils.PT)) {
            builder.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.and")).append(SINGLE_SPACE);
        }

        builder.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "documents.nationality.one"));
        final String nationality = person.getCountry().getFilteredNationality(getLocale());
        builder.append(SINGLE_SPACE).append(nationality.toUpperCase()).append(SINGLE_SPACE);

        if (getDocumentRequest().getDocumentRequestType().equals(DocumentRequestType.APPROVEMENT_MOBILITY_CERTIFICATE)) {
            addParameter("nationality", builder.toString());
        } else {
            addParameter("nationality", FenixStringTools.multipleLineRightPad(builder.toString(), LINE_LENGTH, END_CHAR));
        }
    }

    @Override
    protected void fillReport() {
        super.fillReport();
        Person coordinator = getAdministrativeOffice().getCoordinator().getPerson();

        String coordinatorTitle = getCoordinatorGender(coordinator);

        fillFirstParagraph(coordinator, coordinatorTitle);

        addPersonalInfo();
        setFooter(getDocumentRequest());
        addProgrammeInfo();
        fillInstitutionAndStaffFields();

    }

    private void setFooter(PhdFinalizationCertificateRequest documentRequest) {

        PhdIndividualProgramProcess phdIndividualProgramProcess = getDocumentRequest().getPhdIndividualProgramProcess();
        String student;

        if (phdIndividualProgramProcess.getStudent().getPerson().isMale()) {
            student = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.maleStudent");
        } else {
            student = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.femaleStudent");
        }
        StringBuilder studentNumber = new StringBuilder();
        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.footer.studentNumber");
        studentNumber.append(MessageFormat.format(stringTemplate, student, phdIndividualProgramProcess.getStudent().getNumber()
                .toString()));
        studentNumber.append("/D");
        addParameter("studentNumber", studentNumber.toString());

        StringBuilder documentNumber = new StringBuilder();
        stringTemplate = BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.serviceRequestNumberYear");
        documentNumber.append(stringTemplate);
        documentNumber.append(": ");
        documentNumber.append(documentRequest.getServiceRequestNumberYear());
        addParameter("documentNumber", documentNumber.toString());

    }

    protected void fillFirstParagraph(Person coordinator, String coordinatorTitle) {

        String adminOfficeName = getI18NText(getAdministrativeOffice().getName());
        String institutionName = getInstitutionName();
        String universityName = getUniversityName(new DateTime());

        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.firstParagraph");

        addParameter(
                "firstParagraph",
                "     "
                        + MessageFormat.format(stringTemplate, coordinator.getName(), coordinatorTitle,
                                adminOfficeName.toUpperCase(getLocale()), institutionName.toUpperCase(getLocale()),
                                universityName.toUpperCase(getLocale())));

        addParameter("certificate",
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "message.phd.finalization.certificate.certifies.that"));
    }

    private void addProgrammeInfo() {
        PhdIndividualProgramProcess phdIndividualProgramProcess = getDocumentRequest().getPhdIndividualProgramProcess();
        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.finalization.certificate.concluded.in");
        String conclusionDate =
                MessageFormat.format(stringTemplate, phdIndividualProgramProcess.getConclusionDate().toString("dd/MM/yyyy"));
        addParameter("conclusionDate", conclusionDate);

        StringBuilder thesisTitle = new StringBuilder();
        thesisTitle.append("\"");
        thesisTitle.append(getThesisTitle(phdIndividualProgramProcess));
        thesisTitle.append("\"");
        addParameter("thesisTitle", thesisTitle.toString());

        StringBuilder builder = new StringBuilder();
        builder.append(
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                        "message.phd.finalization.certificate.made.thesis.presentation.on.doctoral.grade")).append(":")
                .append(SINGLE_SPACE);

        final ExecutionYear executionYear = phdIndividualProgramProcess.getExecutionYear();
        builder.append(phdIndividualProgramProcess.getPhdProgram().getName(executionYear).getContent(getLanguage()).toUpperCase());

        addParameter("phdProgram", customMultipleLineRightPad(builder.toString(), LINE_LENGTH, END_CHAR));
        addParameter("finalizationInfo", buildFinalizationInfo());

    }

    private String getThesisTitle(final PhdIndividualProgramProcess phdIndividualProgramProcess) {
        if (getLanguage().equals(org.fenixedu.academic.util.LocaleUtils.EN) && !StringUtils.isEmpty(phdIndividualProgramProcess.getThesisTitleEn())) {
            return phdIndividualProgramProcess.getThesisTitleEn();
        }

        return phdIndividualProgramProcess.getThesisTitle();
    }

    private String buildFinalizationInfo() {
        PhdIndividualProgramProcess phdIndividualProgramProcess = getDocumentRequest().getPhdIndividualProgramProcess();
        String thesisFinalGrade = phdIndividualProgramProcess.getFinalGrade().getLocalizedName(getLocale());

        if (phdIndividualProgramProcess.isBolonha() && phdIndividualProgramProcess.hasRegistryDiplomaRequest()) {
            return String.format(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                    "message.phd.finalization.info.thesis.grade.approved.by.jury.registry.diploma"), thesisFinalGrade,
                    phdIndividualProgramProcess.getRegistryDiplomaRequest().getRegistryCode().getCode());
        } else {
            return String.format(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                    "message.phd.finalization.info.thesis.grade.approved.by.jury"), thesisFinalGrade);
        }
    }

    private void addPersonalInfo() {
        Person person = getDocumentRequest().getPerson();

        String genderPrefix;

        if (person.isMale()) {
            genderPrefix =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            "label.phd.finalization.certificate.father.prefix.for.male");
        } else {
            genderPrefix =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            "label.phd.finalization.certificate.father.prefix.for.female");
        }

        StringBuilder builder = new StringBuilder();
        builder.append(genderPrefix).append(SINGLE_SPACE);
        builder.append(person.getNameOfFather().toUpperCase());

        addParameter("fatherName", FenixStringTools.multipleLineRightPad(builder.toString(), LINE_LENGTH, END_CHAR));

        builder = new StringBuilder();
        builder.append(
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.phd.finalization.certificate.mother.prefix.for.female"))
                .append(SINGLE_SPACE);
        builder.append(person.getNameOfMother().toUpperCase());
        addParameter("motherName", FenixStringTools.multipleLineRightPad(builder.toString(), LINE_LENGTH, END_CHAR));

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
