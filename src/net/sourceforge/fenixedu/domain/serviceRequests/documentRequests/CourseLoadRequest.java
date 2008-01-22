package net.sourceforge.fenixedu.domain.serviceRequests.documentRequests;

import java.util.Collection;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.CourseLoadRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class CourseLoadRequest extends CourseLoadRequest_Base {

    protected CourseLoadRequest() {
	super();
	setNumberOfPages(0);
    }

    public CourseLoadRequest(final Registration registration, final ExecutionYear executionYear,
	    final DocumentPurposeType documentPurposeType, final String otherDocumentPurposeTypeDescription,
	    final Collection<Enrolment> enrolments, final Boolean urgentRequest) {
	this();
	super.init(registration, executionYear, Boolean.FALSE, documentPurposeType, otherDocumentPurposeTypeDescription, urgentRequest);
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
    
    //TODO: it's necessary to have no gratuity debts?
    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
        super.internalChangeState(academicServiceRequestBean);
        
        if (academicServiceRequestBean.isToConclude()) {
            
	    if (!hasNumberOfPages()) {
		throw new DomainException("error.serviceRequests.documentRequests.numberOfPages.must.be.set");
	    }

	    if (!isFree()) {
		new CourseLoadRequestEvent(getAdministrativeOffice(), getPerson(), this);
	    }
	}
    }
    
    @Override
    public void delete() {
	getEnrolments().clear();
        super.delete();
    }
}
