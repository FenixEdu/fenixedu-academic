package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.student.Registration;

import org.joda.time.DateTime;

public class ExternalCourseLoadRequest extends ExternalCourseLoadRequest_Base {

    private ExternalCourseLoadRequest() {
	super();
    }

    public ExternalCourseLoadRequest(final Registration registration, DateTime requestDate, final ExecutionYear executionYear,
	    final DocumentPurposeType documentPurposeType, final String otherDocumentPurposeTypeDescription,
	    final Integer numberOfCourseLoads, final Unit institution, final Boolean urgentRequest) {
	this();
	super.init(registration, requestDate, executionYear, Boolean.FALSE, documentPurposeType,
		otherDocumentPurposeTypeDescription, urgentRequest);
	checkParameters(numberOfCourseLoads, institution);
	setNumberOfCourseLoads(numberOfCourseLoads);
	setInstitution(institution);
    }

    private void checkParameters(final Integer numberOfCourseLoads, final Unit institution) {
	if (numberOfCourseLoads == null || numberOfCourseLoads.intValue() == 0) {
	    throw new DomainException("error.ExternalCourseLoadRequest.invalid.numberOfCourseLoads");
	}
	if (institution == null) {
	    throw new DomainException("error.ExternalCourseLoadRequest.invalid.institution");
	}
    }

    @Override
    public List<Enrolment> getEnrolments() {
	return Collections.unmodifiableList(super.getEnrolments());
    }

    @Override
    public Set<Enrolment> getEnrolmentsSet() {
	return Collections.unmodifiableSet(super.getEnrolmentsSet());
    }

    @Override
    public Iterator<Enrolment> getEnrolmentsIterator() {
	return getEnrolmentsSet().iterator();
    }

    @Override
    public void addEnrolments(Enrolment enrolments) {
	throw new DomainException("error.ExternalCourseLoadRequest.cannot.add.enrolments");
    }

    @Override
    public void removeEnrolments(Enrolment enrolments) {
	throw new DomainException("error.ExternalCourseLoadRequest.cannot.remove.enrolments");
    }
    
    @Override
    public DocumentRequestType getDocumentRequestType() {
	return DocumentRequestType.EXTERNAL_COURSE_LOAD;
    }

    @Override
    public EventType getEventType() {
	return EventType.EXTERNAL_COURSE_LOAD_REQUEST;
    }
}
