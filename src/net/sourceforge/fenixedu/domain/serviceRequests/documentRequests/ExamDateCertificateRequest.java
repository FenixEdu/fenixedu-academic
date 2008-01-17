package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class ExamDateCertificateRequest extends ExamDateCertificateRequest_Base {

    public ExamDateCertificateRequest() {
	super();
    }

    public ExamDateCertificateRequest(Registration registration, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, ExecutionYear executionYear,
	    Collection<Enrolment> enrolments) {

	this();
	init(registration, executionYear, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest, enrolments);
    }

    protected void init(Registration registration, ExecutionYear executionYear, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, Collection<Enrolment> enrolments) {

	checkParameters(executionYear, enrolments);
	super.init(registration, executionYear, Boolean.FALSE, documentPurposeType, otherDocumentPurposeTypeDescription,
		urgentRequest);
	super.getEnrolments().addAll(enrolments);

    }

    private void checkParameters(ExecutionYear executionYear, Collection<Enrolment> enrolments) {
	if (executionYear == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.ExamDateCertificateRequest.executionYear.cannot.be.null");
	}

	if (enrolments == null || enrolments.isEmpty()) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.ExamDateCertificateRequest.enrolments.cannot.be.null.and.must.have.size.greater.than.zero");
	}

    }

    @Override
    public Integer getNumberOfUnits() {
	return getNumberOfPages();
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.EXAM_DATE_CERTIFICATE;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public EventType getEventType() {
	return null;
    }

}
