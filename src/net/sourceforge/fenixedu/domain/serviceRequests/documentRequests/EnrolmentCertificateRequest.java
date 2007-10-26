package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class EnrolmentCertificateRequest extends EnrolmentCertificateRequest_Base {

    private EnrolmentCertificateRequest() {
	super();
    }

    public EnrolmentCertificateRequest(Registration registration,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription,
	    Boolean urgentRequest, Boolean detailed, ExecutionYear executionYear) {

	this();

	init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest,
		detailed, executionYear);

    }

    protected void init(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Boolean detailed,
	    ExecutionYear executionYear) {

	init(registration, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);

	checkParameters(detailed, executionYear);
	super.setDetailed(detailed);
	super.setExecutionYear(executionYear);
    }

    private void checkParameters(Boolean detailed, ExecutionYear executionYear) {
	if (detailed == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.detailed.cannot.be.null");
	}
	if (executionYear == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.EnrolmentCertificateRequest.executionYear.cannot.be.null");
	} else if (!getRegistration().hasAnyEnrolmentsIn(executionYear)) {
	    throw new DomainException(
		    "EnrolmentCertificateRequest.no.enrolments.for.registration.in.given.executionYear");
	}
    }

    @Override
    public void delete() {
	super.setExecutionYear(null);
	super.delete();
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
    final public void setExecutionYear(ExecutionYear executionYear) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.EnrolmentCertificateRequest.cannot.modify.executionYear");
    }

    @Override
    final public void setDetailed(Boolean detailed) {
	throw new DomainException(
		"error.serviceRequests.documentRequests.EnrolmentCertificateRequest.cannot.modify.detailed");
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

}
