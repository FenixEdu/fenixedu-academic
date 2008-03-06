package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class ProgramCertificateRequest extends ProgramCertificateRequest_Base {

    private ProgramCertificateRequest() {
	super();
	setNumberOfPages(0);
    }

    public ProgramCertificateRequest(final Registration registration, DateTime requestDate, final ExecutionYear executionYear,
	    final DocumentPurposeType purposeType, final String otherPurposeTypeDescription,
	    final Collection<Enrolment> enrolments, final Boolean urgentRequest) {
	this();
	super.init(registration, requestDate, executionYear, Boolean.FALSE, purposeType, otherPurposeTypeDescription,
		urgentRequest);
	checkParameters(enrolments);
	super.getEnrolments().addAll(enrolments);
    }

    private void checkParameters(final Collection<Enrolment> enrolments) {
	if (enrolments.isEmpty()) {
	    throw new DomainException("error.CourseLoadRequest.invalid.number.of.enrolments");
	}
	
	for (final Enrolment enrolment : enrolments) {
	    if (!enrolment.isApproved()) {
		throw new DomainException("error.ProgramCertificateRequest.cannot.add.not.approved.enrolments");
	    }
	}
    }

    @Override
    public Integer getNumberOfUnits() {
	return Integer.valueOf(0);
    }

    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.PROGRAM_CERTIFICATE;
    }

    @Override
    public String getDocumentTemplateKey() {
	return getClass().getName();
    }

    @Override
    public EventType getEventType() {
	return EventType.PROGRAM_CERTIFICATE_REQUEST;
    }

    @Override
    public void delete() {
	getEnrolments().clear();
	super.delete();
    }
}
