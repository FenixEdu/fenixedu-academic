package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.ExternalDegreeChangeRequest;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class ExternalDegreeChangeRequestEvent extends ExternalDegreeChangeRequestEvent_Base {
    
    protected ExternalDegreeChangeRequestEvent() {
        super();
    }
    
    public ExternalDegreeChangeRequestEvent(final AdministrativeOffice administrativeOffice, final Person person, final ExternalDegreeChangeRequest academicServiceRequest) {
	this();
	super.init(administrativeOffice, EventType.EXTERNAL_DEGREE_CHANGE_REQUEST, person, academicServiceRequest);
    }
    
    @Override
    public ExternalDegreeChangeRequest getAcademicServiceRequest() {
        return (ExternalDegreeChangeRequest) super.getAcademicServiceRequest();
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
