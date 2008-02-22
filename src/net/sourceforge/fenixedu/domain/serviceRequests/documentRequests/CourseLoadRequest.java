package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class CourseLoadRequest extends CourseLoadRequest_Base {

    protected CourseLoadRequest() {
	super();
	setNumberOfPages(0);
    }

    public CourseLoadRequest(final Registration registration, DateTime requestDate, final ExecutionYear executionYear,
	    final DocumentPurposeType documentPurposeType, final String otherDocumentPurposeTypeDescription,
	    final Collection<Enrolment> enrolments, final Boolean urgentRequest) {
	this();
	super.init(registration, requestDate, executionYear, Boolean.FALSE, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);
	checkParameters(enrolments);
	super.getEnrolments().addAll(enrolments);
    }

    private void checkParameters(final Collection<Enrolment> enrolments) {
	for (final Enrolment enrolment : enrolments) {
	    if (!enrolment.isApproved()) {
		throw new DomainException("error.CourseLoadRequest.cannot.add.not.approved.enrolments");
	    }
	}
    }

    @Override
    public Integer getNumberOfUnits() {
	return Integer.valueOf(0);
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
    public void delete() {
	getEnrolments().clear();
	super.delete();
    }
}
