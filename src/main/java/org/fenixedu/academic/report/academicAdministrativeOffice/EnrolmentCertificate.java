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
import java.util.Collection;
import java.util.TreeSet;

import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.Enrolment;
import org.fenixedu.academic.domain.IEnrolment;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.PostingRule;
import org.fenixedu.academic.domain.accounting.postingRules.serviceRequests.EnrolmentCertificateRequestPR;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CycleType;
import org.fenixedu.academic.domain.degreeStructure.ProgramConclusion;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.EnrolmentCertificateRequest;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.IDocumentRequest;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.FenixStringTools;
import org.fenixedu.academic.util.Money;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class EnrolmentCertificate extends AdministrativeOfficeDocument {

    protected EnrolmentCertificate(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected void fillReport() {
        super.fillReport();

        final Person coordinator = getAdministrativeOffice().getCoordinator().getPerson();
        final EnrolmentCertificateRequest request = getDocumentRequest();
        final Registration registration = getDocumentRequest().getRegistration();

        String coordinatorTitle = getCoordinatorGender(coordinator);

        String student;
        if (registration.getStudent().getPerson().isMale()) {
            student =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            "label.academicDocument.enrolment.declaration.maleEnrolment");
        } else {
            student =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            "label.academicDocument.enrolment.declaration.femaleEnrolment");
        }

        addParameter("enrolmentsInfo", getEnrolmentsInfo());
        fillFirstParagraph(coordinator, coordinatorTitle);
        fillthirdthParagraph(registration, request, student);
        fillInstitutionAndStaffFields();
        setFooter(getDocumentRequest());
    }

    @Override
    public EnrolmentCertificateRequest getDocumentRequest() {
        return (EnrolmentCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected void addPriceFields() {
        final EnrolmentCertificateRequest request = getDocumentRequest();
        final PostingRule postingRule = getPostingRule();

        if (postingRule instanceof EnrolmentCertificateRequestPR) {
            final EnrolmentCertificateRequestPR requestPR = (EnrolmentCertificateRequestPR) postingRule;
            addParameter("printed",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.certificate.printingPriceLabel"));
            addParameter("printPriceLabel",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.certificate.issuingPriceLabel"));
            addParameter("urgency", BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                    "label.academicDocument.certificate.fastDeliveryPriceLabel"));
            addParameter("total",
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.certificate.totalsPriceLabel"));
            addParameter("amountPerPage", requestPR.getAmountPerPage());
            addParameter("baseAmountPlusAmountForUnits", calculateAmountToPayPlusUnits(request, requestPR));
            addParameter("urgencyAmount", request.getUrgentRequest() ? requestPR.getBaseAmount() : Money.ZERO);
            addParameter("printPriceFields", printPriceParameters(request));
        } else {
            super.addPriceFields();
        }
    }

    protected void fillFirstParagraph(Person coordinator, String coordinatorTitle) {
        String institutionName = getInstitutionName();
        String adminOfficeName = getI18NText(getAdministrativeOffice().getName());
        String universityName = getUniversityName(new DateTime());
        String stringTemplate =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "label.academicDocument.declaration.firstParagraph");

        addParameter(
                "firstParagraph",
                "     "
                        + MessageFormat.format(stringTemplate, coordinator.getName(), coordinatorTitle,
                                adminOfficeName.toUpperCase(getLocale()), institutionName.toUpperCase(getLocale()),
                                universityName.toUpperCase(getLocale())));

        addParameter("certificate", BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                "label.academicDocument.standaloneEnrolmentCertificate.secondParagraph"));
    }

    private void fillthirdthParagraph(Registration registration, EnrolmentCertificateRequest request, String student) {
        String situation = "";
        if (request.getExecutionYear() != null) {
            situation =
                    BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                            getExecutionYear().containsDate(new DateTime()) ? "label.is" : "label.was");

        }

        String detailed =
                request.getDetailed() ? BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                        "label.academicDocument.enrolmentCertificate.detailed") : ".";
        String executionYear =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "message.declaration.registration.execution.year.prefix");
        String stringTemplate1 =
                BundleUtil.getString(Bundle.ACADEMIC, getLocale(), "message.academicDocument.enrolmentCertificate");
        addParameter(
                "secondParagraph",
                MessageFormat.format(stringTemplate1, situation, student, executionYear, getDocumentRequest().getExecutionYear()
                        .getYear().toString(), getCurricularYear(), getDegreeDescription(), detailed));

    }

    private Money calculateAmountToPayPlusUnits(final EnrolmentCertificateRequest request,
            final EnrolmentCertificateRequestPR requestPR) {
        Money total = requestPR.getBaseAmount();
        if (request.getDetailed() != null && request.getDetailed().booleanValue()) {
            total = total.add(getAmountForUnits(request, requestPR));
        }
        return total;

    }

    private Money getAmountForUnits(final EnrolmentCertificateRequest request, final EnrolmentCertificateRequestPR requestPR) {
        return requestPR.getAmountForUnits(request.getNumberOfUnits());
    }

    @Override
    protected String getDegreeDescription() {
        final Registration registration = getDocumentRequest().getRegistration();

        if (registration.getDegreeType().isComposite() && hasMoreThanOneCycle(registration)) {
            return registration.getDegreeDescription(getDocumentRequest().getExecutionYear(), (ProgramConclusion) null,
                    getLocale());
        } else {
            final DegreeType degreeType = registration.getDegreeType();
            final CycleType cycleType =
                    degreeType.hasExactlyOneCycleType() ? degreeType.getCycleType() : registration
                            .getCycleType(getExecutionYear());
            return registration.getDegreeDescription(getExecutionYear(), cycleType, getLocale());
        }
    }

    private boolean hasMoreThanOneCycle(final Registration registration) {
        return registration.getInternalCycleCurriculumGrops().size() > 1;
    }

    final private String getCurricularYear() {
        final StringBuilder result = new StringBuilder();

        final DegreeCurricularPlan degreeCurricularPlan =
                getRegistration().getStudentCurricularPlan(getExecutionYear()).getDegreeCurricularPlan();

        if (degreeCurricularPlan.getDurationInYears() != 1) {
            final Integer curricularYear =
                    Integer.valueOf(getDocumentRequest().getRegistration().getCurricularYear(getExecutionYear()));

            result.append(BundleUtil.getString(Bundle.ENUMERATION, getLocale(), curricularYear.toString() + ".ordinal")
                    .toUpperCase());
            result.append(BundleUtil.getString(Bundle.ACADEMIC, getLocale(),
                    "label.academicDocument.enrolment.declaration.curricularYear"));
        }

        return result.toString();
    }

    final private String getEnrolmentsInfo() {
        final StringBuilder result = new StringBuilder();
        final EnrolmentCertificateRequest request = getDocumentRequest();

        if (request.getDetailed()) {
            final Collection<IEnrolment> enrolments =
                    new TreeSet<>(Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID);

            enrolments.addAll(request.getEntriesToReport());
            reportEnrolments(result, enrolments);
            enrolments.clear();

            enrolments.addAll(request.getExtraCurricularEntriesToReport());
            if (!enrolments.isEmpty()) {
                reportRemainingEnrolments(result, enrolments, "Extra-Curriculares");
            }
            enrolments.clear();

            enrolments.addAll(request.getPropaedeuticEntriesToReport());
            if (!enrolments.isEmpty()) {
                reportRemainingEnrolments(result, enrolments, "Propedeuticas");
            }
            enrolments.clear();

            enrolments.addAll(request.getExternalEnrolments());
            if (!enrolments.isEmpty()) {
                reportRemainingEnrolments(result, enrolments, "Externas");
            }

            result.append(generateEndLine());
        }

        return result.toString();
    }

    final private void reportEnrolments(final StringBuilder result, final Collection<IEnrolment> enrolments) {
        for (final IEnrolment enrolment : enrolments) {
            reportEnrolment(result, enrolment);
        }
    }

    final private void reportRemainingEnrolments(final StringBuilder result, final Collection<IEnrolment> enrolments,
            final String title) {
        result.append(generateEndLine()).append(LINE_BREAK).append(title).append(":").append(LINE_BREAK);

        for (final IEnrolment enrolment : enrolments) {
            reportEnrolment(result, enrolment);
        }
    }

    final private void reportEnrolment(final StringBuilder result, final IEnrolment enrolment) {
        result.append(
                FenixStringTools.multipleLineRightPadWithSuffix(getPresentationNameFor(enrolment).toUpperCase(), LINE_LENGTH,
                        END_CHAR, getCreditsInfo(enrolment))).append(LINE_BREAK);
    }

    final private String getCreditsInfo(final IEnrolment enrolment) {
        final StringBuilder result = new StringBuilder();

        if (getDocumentRequest().isToShowCredits()) {

            result.append(enrolment.getEctsCreditsForCurriculum().toString()).append(
                    getCreditsDescription());
        }

        return result.toString();
    }
}
