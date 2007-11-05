package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.StudentReingressionRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class StudentReingressionRequest extends StudentReingressionRequest_Base {
    
    protected StudentReingressionRequest() {
        super();
    }
    
    public StudentReingressionRequest(final Registration registration, final Boolean urgentRequest, final Boolean freeProcessed) {
	this();
	checkIfRegistrationIsInValidState(registration);
	super.init(registration, urgentRequest, freeProcessed);
    }

    private void checkIfRegistrationIsInValidState(final Registration registration) {
	throw new DomainException("error.StudentReingressionRequest.method.not.implemented");
    }

    @Override
    public String getDescription() {
	return getDescription("AcademicServiceRequestType.REINGRESSION");
    }

    @Override
    public EventType getEventType() {
	return EventType.STUDENT_REINGRESSION_REQUEST;
    }

    @Override
    public ExecutionYear getExecutionYear() {
	return null;
    }
    
    @Override
    protected void internalChangeState(final AcademicServiceRequestSituationType academicServiceRequestSituationType, final Employee employee) {
	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (isToProcessing(academicServiceRequestSituationType)) {
	    if (hasEvent()) {
		throw new DomainException("error.ExternalDegreeChangeRequest.already.has.event");
	    }
	    new StudentReingressionRequestEvent(getAdministrativeOffice(), getPerson(), this);
	}
    }
}
