package net.sourceforge.fenixedu.presentationTier.docs.academicAdministrativeOffice;

import java.util.Collection;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.accounting.PostingRule;
import net.sourceforge.fenixedu.domain.accounting.postingRules.serviceRequests.EnrolmentCertificateRequestPR;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.EnrolmentCertificateRequest;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.IDocumentRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.Money;
import net.sourceforge.fenixedu.util.StringUtils;

public class EnrolmentCertificate extends AdministrativeOfficeDocument {

    protected EnrolmentCertificate(final IDocumentRequest documentRequest) {
        super(documentRequest);
    }

    @Override
    protected void fillReport() {
        super.fillReport();

        addParameter("curricularYear", getCurricularYear());
        addParameter("enrolmentsInfo", getEnrolmentsInfo());
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
            addParameter("amountPerPage", requestPR.getAmountPerPage());
            addParameter("baseAmountPlusAmountForUnits", calculateAmountToPayPlusUnits(request, requestPR));
            addParameter("urgencyAmount", request.getUrgentRequest() ? requestPR.getBaseAmount() : Money.ZERO);
            addParameter("printPriceFields", printPriceParameters(request));
        } else {
            super.addPriceFields();
        }
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

            result.append(getEnumerationBundle().getString(curricularYear.toString() + ".ordinal").toUpperCase());
            result.append(" ano curricular, do ");
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
                StringUtils.multipleLineRightPadWithSuffix(getPresentationNameFor(enrolment).toUpperCase(), LINE_LENGTH,
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
