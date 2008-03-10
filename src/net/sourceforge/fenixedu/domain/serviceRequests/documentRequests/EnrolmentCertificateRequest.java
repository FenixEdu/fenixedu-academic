package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class EnrolmentCertificateRequest extends EnrolmentCertificateRequest_Base {

    private EnrolmentCertificateRequest() {
	super();
    }

    public EnrolmentCertificateRequest(Registration registration, DateTime requestDate, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Boolean detailed, ExecutionYear executionYear) {

	this();
	init(registration, requestDate, executionYear, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest,
		detailed);
    }

    protected void init(Registration registration, DateTime requestDate, ExecutionYear executionYear,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
	    Boolean detailed) {

	checkParameters(registration, detailed, executionYear);
	super.init(registration, requestDate, executionYear, Boolean.FALSE, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);
	super.setDetailed(detailed);
    }

    private void checkParameters(final Registration registration, final Boolean detailed, final ExecutionYear executionYear) {
	if (detailed == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.detailed.cannot.be.null");
	}
	if (executionYear == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.executionYear.cannot.be.null");

	} else if (!registration.hasAnyEnrolmentsIn(executionYear)) {
	    throw new DomainException("EnrolmentCertificateRequest.no.enrolments.for.registration.in.given.executionYear");
	}
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.ENROLMENT_CERTIFICATE;
    }

    @Override
    final public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    final public void setDetailed(Boolean detailed) {
	throw new DomainException("error.serviceRequests.documentRequests.EnrolmentCertificateRequest.cannot.modify.detailed");
    }

    @Override
    final public EventType getEventType() {
	return EventType.ENROLMENT_CERTIFICATE_REQUEST;
    }

    final public Collection<Enrolment> getEnrolmentsToDisplay() {
	return getRegistration().getLatestCurricularCoursesEnrolments(getExecutionYear());
    }

    @Override
    final public Integer getNumberOfUnits() {
	return getEnrolmentsToDisplay().size();
    }

    @Override
    public boolean isAvailableForTransitedRegistrations() {
	return true;
    }

    @Override
    public boolean hasPersonalInfo() {
	return true;
    }

}
