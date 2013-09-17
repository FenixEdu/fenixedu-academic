package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.DocumentRequestCreateBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.RegistrationAgreement;

public class CourseLoadRequest extends CourseLoadRequest_Base {

    static public final List<RegistrationAgreement> FREE_PAYMENT_AGREEMENTS = Arrays.asList(RegistrationAgreement.AFA,
            RegistrationAgreement.MA);

    protected CourseLoadRequest() {
        super();
        setNumberOfPages(0);
    }

    public CourseLoadRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.getEnrolmentsSet().addAll(bean.getEnrolments());
        super.setRequestedCycle(bean.getRequestedCycle());
    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
        if (bean.getEnrolments().isEmpty()) {
            throw new DomainException("error.CourseLoadRequest.invalid.number.of.enrolments");
        }

        for (final Enrolment enrolment : bean.getEnrolments()) {
            if (!enrolment.isApproved()) {
                throw new DomainException("error.CourseLoadRequest.cannot.add.not.approved.enrolments");
            }
            if (!getStudent().hasEnrolments(enrolment)) {
                throw new DomainException("error.ProgramCertificateRequest.enrolment.doesnot.belong.to.student");
            }
        }
    }

    @Override
    public Integer getNumberOfUnits() {
        return getEnrolmentsSet().size();
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.COURSE_LOAD;
    }

    @Override
    public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    public EventType getEventType() {
        return EventType.COURSE_LOAD_REQUEST;
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);

        if (academicServiceRequestBean.isToCancelOrReject()) {
            for (; hasAnyEnrolments();) {
                removeEnrolments(getEnrolments().iterator().next());
            }
        }
    }

    @Override
    protected void disconnect() {
        super.getEnrolmentsSet().clear();
        super.disconnect();
    }

    @Override
    public boolean isFree() {
        if (FREE_PAYMENT_AGREEMENTS.contains(getRegistration().getRegistrationAgreement())) {
            return true;
        }
        return super.isFree();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Enrolment> getEnrolments() {
        return getEnrolmentsSet();
    }

    @Deprecated
    public boolean hasAnyEnrolments() {
        return !getEnrolmentsSet().isEmpty();
    }

    @Deprecated
    public boolean hasRequestedCycle() {
        return getRequestedCycle() != null;
    }

}
