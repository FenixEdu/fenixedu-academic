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
	super.getEnrolments().addAll(bean.getEnrolments());
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
	return getEnrolmentsCount();
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
		removeEnrolments(getEnrolments().get(0));
	    }
	}
    }

    @Override
    protected void disconnect() {
	super.getEnrolments().clear();
	super.disconnect();
    }

    @Override
    public boolean isFree() {
	if (FREE_PAYMENT_AGREEMENTS.contains(getRegistration().getRegistrationAgreement())) {
	    return true;
	}
	return super.isFree();
    }

}
