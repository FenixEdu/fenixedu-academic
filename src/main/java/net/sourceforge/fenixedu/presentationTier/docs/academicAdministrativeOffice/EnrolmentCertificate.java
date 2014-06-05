/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.EnrolmentCertificateRequestPR;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Bundle;
import net.sourceforge.fenixedu.util.FenixStringTools;
import net.sourceforge.fenixedu.util.Money;

import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

public class EnrolmentCertificate extends AdministrativeOfficeDocument {

    protected EnrolmentCertificate(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected void fillReport() {
        super.fillReport();

        final Unit adminOfficeUnit = getAdministrativeOffice().getUnit();
        final Person coordinator = adminOfficeUnit.getActiveUnitCoordinator();
        final EnrolmentCertificateRequest request = getDocumentRequest();
        final Registration registration = getDocumentRequest().getRegistration();

        String coordinatorTitle = getCoordinatorGender(coordinator);

        String student;
        if (registration.getStudent().getPerson().isMale()) {
            student = BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.enrolment.declaration.maleEnrolment");
        } else {
            student = BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.enrolment.declaration.femaleEnrolment");
        }

        addParameter("enrolmentsInfo", getEnrolmentsInfo());
        fillFirstParagraph(coordinator, adminOfficeUnit, coordinatorTitle);
        fillthirdthParagraph(registration, request, student);
        fillEmployeeFields();
        setFooter(getDocumentRequest());
    }

    @Override
    protected EnrolmentCertificateRequest getDocumentRequest() {
        return (EnrolmentCertificateRequest) super.getDocumentRequest();
    }

    @Override
    protected void addPriceFields() {
        final EnrolmentCertificateRequest request = getDocumentRequest();
        final PostingRule postingRule = getPostingRule();

        if (postingRule instanceof EnrolmentCertificateRequestPR) {
            final EnrolmentCertificateRequestPR requestPR = (EnrolmentCertificateRequestPR) postingRule;
            addParameter("printed", BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.certificate.printingPriceLabel"));
            addParameter("printPriceLabel", BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.certificate.issuingPriceLabel"));
            addParameter("urgency", BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.certificate.fastDeliveryPriceLabel"));
            addParameter("total", BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.certificate.totalsPriceLabel"));
            addParameter("amountPerPage", requestPR.getAmountPerPage());
            addParameter("baseAmountPlusAmountForUnits", calculateAmountToPayPlusUnits(request, requestPR));
            addParameter("urgencyAmount", request.getUrgentRequest() ? requestPR.getBaseAmount() : Money.ZERO);
            addParameter("printPriceFields", printPriceParameters(request));
        } else {
            super.addPriceFields();
        }
    }

    protected void fillFirstParagraph(Person coordinator, Unit adminOfficeUnit, String coordinatorTitle) {
        String institutionName = getInstitutionName();
        String adminOfficeName = getMLSTextContent(adminOfficeUnit.getPartyName());
        String universityName = getUniversityName(new DateTime());
        String stringTemplate = BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.declaration.firstParagraph");

        addParameter(
                "firstParagraph",
                "     "
                        + MessageFormat.format(stringTemplate, coordinator.getName(), coordinatorTitle,
                                adminOfficeName.toUpperCase(getLocale()), institutionName.toUpperCase(getLocale()),
                                universityName.toUpperCase(getLocale())));

        addParameter("certificate",
                BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.standaloneEnrolmentCertificate.secondParagraph"));
    }

    private void fillthirdthParagraph(Registration registration, EnrolmentCertificateRequest request, String student) {
        String situation = "";
        if (request.hasExecutionYear()) {
            situation = BundleUtil.getString(Bundle.ACADEMIC, getExecutionYear().containsDate(new DateTime()) ? "label.is" : "label.was");

        }

        String detailed =
                request.getDetailed() ? BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.enrolmentCertificate.detailed") : ".";
        String executionYear = BundleUtil.getString(Bundle.ACADEMIC, "message.declaration.registration.execution.year.prefix");
        String stringTemplate1 = BundleUtil.getString(Bundle.ACADEMIC, "message.academicDocument.enrolmentCertificate");
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
            return registration.getDegreeDescription(getDocumentRequest().getExecutionYear(), null);
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

        if (!getDocumentRequest().getDegreeType().hasExactlyOneCurricularYear()) {
            final Integer curricularYear =
                    Integer.valueOf(getDocumentRequest().getRegistration().getCurricularYear(getExecutionYear()));

            result.append(BundleUtil.getString(Bundle.ENUMERATION, curricularYear.toString() + ".ordinal").toUpperCase());
            result.append(BundleUtil.getString(Bundle.ACADEMIC, "label.academicDocument.enrolment.declaration.curricularYear"));
        }

        return result.toString();
    }

    final private String getEnrolmentsInfo() {
        final StringBuilder result = new StringBuilder();
        final EnrolmentCertificateRequest request = getDocumentRequest();

        if (request.getDetailed()) {
            final Collection<Enrolment> enrolments =
                    new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_YEAR_AND_NAME_AND_ID);

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

            result.append(generateEndLine());
        }

        return result.toString();
    }

    final private void reportEnrolments(final StringBuilder result, final Collection<Enrolment> enrolments) {
        for (final Enrolment enrolment : enrolments) {
            reportEnrolment(result, enrolment);
        }
    }

    final private void reportRemainingEnrolments(final StringBuilder result, final Collection<Enrolment> enrolments,
            final String title) {
        result.append(generateEndLine()).append(LINE_BREAK).append(title).append(":").append(LINE_BREAK);

        for (final Enrolment enrolment : enrolments) {
            reportEnrolment(result, enrolment);
        }
    }

    final private void reportEnrolment(final StringBuilder result, final Enrolment enrolment) {
        result.append(
                FenixStringTools.multipleLineRightPadWithSuffix(getPresentationNameFor(enrolment).toUpperCase(), LINE_LENGTH,
                        END_CHAR, getCreditsInfo(enrolment))).append(LINE_BREAK);
    }

    final private String getCreditsInfo(final Enrolment enrolment) {
        final StringBuilder result = new StringBuilder();

        if (getDocumentRequest().isToShowCredits()) {
            result.append(enrolment.getCurricularCourse().getEctsCredits(enrolment.getExecutionPeriod()).toString()).append(
                    getCreditsDescription());
        }

        return result.toString();
    }
}
