package net.sourceforge.fenixedu.domain.serviceRequests;

import net.sourceforge.fenixedu.dataTransferObject.serviceRequests.AcademicServiceRequestBean;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.accounting.events.serviceRequests.InternalDegreeChangeRequestEvent;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;

public class InternalDegreeChangeRequest extends InternalDegreeChangeRequest_Base {
    
    protected InternalDegreeChangeRequest() {
	super();
    }
    
    public InternalDegreeChangeRequest(final Registration registration, final DegreeCurricularPlan destinationDegreeCurricularPlan, final Boolean urgentRequest, final Boolean freeProcessed) {
	this();
	init(registration, destinationDegreeCurricularPlan, urgentRequest, freeProcessed);
    }
    
    protected void init(final Registration registration, final DegreeCurricularPlan destination, final Boolean urgentRequest, final Boolean freeProcessed) {
	super.init(registration, urgentRequest, freeProcessed);
	checkParameters(destination);
	checkIfCanCreate(registration, destination);
	super.setDestination(destination);
    }
    
    private void checkIfCanCreate(final Registration registration, final DegreeCurricularPlan destination) {
	if (registration.getLastDegreeCurricularPlan() == destination) {
	    throw new DomainException("error.serviceRequests.InternalDegreeChangeRequest.destinationDegreeCurricularPlan.must.be.different.from.source");
	}
    }

    private void checkParameters(final DegreeCurricularPlan sourceDegreeCurricularPlan) {
	if (sourceDegreeCurricularPlan == null) {
	    throw new DomainException("error.serviceRequests.InternalDegreeChangeRequest.sourceDegreeCurricularPlan.cannot.be.null");
	}
    }

    @Override
    public void setDestination(DegreeCurricularPlan destination) {
        throw new DomainException("error.serviceRequests.InternalDegreeChangeRequest.cannot.modify.destinationDegreeCurricularPlan");
    }
    
    @Override
    public EventType getEventType() {
	return EventType.INTERNAL_DEGREE_CHANGE_REQUEST;
    }
    
    @Override
    public String getDescription() {
	return getDescription("AcademicServiceRequestType.DEGREE_CHANGE");
    }

    @Override
    protected void internalChangeState(AcademicServiceRequestBean academicServiceRequestBean) {
	super.internalChangeState(academicServiceRequestBean);

	if (academicServiceRequestBean.isToProcess()) {
	    if (hasEvent()) {
		throw new DomainException("error.InternalDegreeChangeRequest.already.has.event");
	    }
	    new InternalDegreeChangeRequestEvent(getAdministrativeOffice(), getPerson(), this);
	}
    }

    @Override
    public void delete() {
        super.setDestination(null);
        super.delete();
    }
}
