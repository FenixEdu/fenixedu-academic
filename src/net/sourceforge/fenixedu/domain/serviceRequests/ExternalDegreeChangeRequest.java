package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.ExecutionYear;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.ExternalDegreeChangeRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class ExternalDegreeChangeRequest extends ExternalDegreeChangeRequest_Base {
    
    protected ExternalDegreeChangeRequest() {
	super();
    }
    
    public ExternalDegreeChangeRequest(final Person person, final DegreeCurricularPlan destination, final Boolean urgentRequest, final Boolean freeProcessed) {
	this();
	init(person, destination, urgentRequest, freeProcessed);
    }

    protected void init(final Person person, final DegreeCurricularPlan destination, final Boolean urgentRequest, final Boolean freeProcessed) {
	super.init(person, urgentRequest, freeProcessed);
	checkParameters(destination);
	super.setDestination(destination);
    }

    private void checkParameters(final DegreeCurricularPlan destinationDegreeCurricularPlan) {
	if (destinationDegreeCurricularPlan == null) {
	    throw new DomainException("error.serviceRequests.PersonAcademicServiceRequest.destinationDegreeCurricularPlan.cannot.be.null");
	}
    }

    @Override
    public void setDestination(DegreeCurricularPlan destination) {
        throw new DomainException("error.serviceRequests.ExternalDegreeChangeRequest.cannot.modify.destinationDegreeCurricularPlan");
    }
    
    @Override
    public String getDescription() {
	return getDescription("AcademicServiceRequestType.DEGREE_CHANGE");
    }
    
    @Override
    public ExecutionYear getExecutionYear() {
	return null;
    }
    
    @Override
    public EventType getEventType() {
	return EventType.EXTERNAL_DEGREE_CHANGE_REQUEST;
    }

    @Override
    protected void internalChangeState(final AcademicServiceRequestSituationType academicServiceRequestSituationType, final Employee employee) {
	super.internalChangeState(academicServiceRequestSituationType, employee);

	if (isToProcessing(academicServiceRequestSituationType)) {
	    if (hasEvent()) {
		throw new DomainException("error.ExternalDegreeChangeRequest.already.has.event");
	    }
	    new ExternalDegreeChangeRequestEvent(getAdministrativeOffice(), getPerson(), this);
	}
    }

    @Override
    public void delete() {
        super.setDestination(null);
        super.delete();
    }
}
