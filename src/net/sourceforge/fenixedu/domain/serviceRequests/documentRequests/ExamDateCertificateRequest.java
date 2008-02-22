package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionPeriod;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class ExamDateCertificateRequest extends ExamDateCertificateRequest_Base {

    protected ExamDateCertificateRequest() {
	super();
    }

    public ExamDateCertificateRequest(Registration registration, DateTime requestDate, DocumentPurposeType documentPurposeType,
	    String otherDocumentPurposeTypeDescription, Boolean urgentRequest, ExecutionYear executionYear,
	    Collection<Enrolment> enrolments, ExecutionPeriod executionPeriod) {

	this();
	init(registration, requestDate, executionYear, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest,
		enrolments, executionPeriod);
    }

    protected void init(Registration registration, DateTime requestDate, ExecutionYear executionYear,
	    DocumentPurposeType documentPurposeType, String otherDocumentPurposeTypeDescription, Boolean urgentRequest,
	    Collection<Enrolment> enrolments, ExecutionPeriod executionPeriod) {

	checkParameters(executionYear, enrolments, executionPeriod);
	super.init(registration, requestDate, executionYear, Boolean.FALSE, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);
	super.getEnrolments().addAll(enrolments);
	super.setExecutionPeriod(executionPeriod);

    }

    private void checkParameters(ExecutionYear executionYear, Collection<Enrolment> enrolments, ExecutionPeriod executionPeriod) {
	if (executionYear == null) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.ExamDateCertificateRequest.executionYear.cannot.be.null");
	}

	if (enrolments == null || enrolments.isEmpty()) {
	    throw new DomainException(
		    "error.serviceRequests.documentRequests.ExamDateCertificateRequest.enrolments.cannot.be.null.and.must.have.size.greater.than.zero");
	}

	if (executionPeriod == null) {
	    throw new DomainException(
		    "error.net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.ExamDateCertificateRequest.executionPeriod.cannot.be.null");
	}

    }

    @Override
    public Integer getNumberOfUnits() {
	return 0;
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
