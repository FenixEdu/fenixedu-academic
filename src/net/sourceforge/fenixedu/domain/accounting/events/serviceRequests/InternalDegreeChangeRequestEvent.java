package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.InternalDegreeChangeRequest;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class InternalDegreeChangeRequestEvent extends InternalDegreeChangeRequestEvent_Base {
    
    protected InternalDegreeChangeRequestEvent() {
        super();
    }
    
    public InternalDegreeChangeRequestEvent(final AdministrativeOffice administrativeOffice, final Person person, final InternalDegreeChangeRequest academicServiceRequest) {
	this();
	super.init(administrativeOffice, EventType.INTERNAL_DEGREE_CHANGE_REQUEST, person, academicServiceRequest);
    }

    @Override
    public InternalDegreeChangeRequest getAcademicServiceRequest() {
        return (InternalDegreeChangeRequest) super.getAcademicServiceRequest();
    }
    
    private DegreeCurricularPlan getDestination() {
	return getAcademicServiceRequest().getDestination();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	
	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" (p. ");
	labelFormatter.appendLabel(getDestination().getName());
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel(")");
	if (getAcademicServiceRequest().hasExecutionYear()) {
	    labelFormatter.appendLabel(" - " + getExecutionYear().getYear());    
	}
	
	return labelFormatter;
    }
}
