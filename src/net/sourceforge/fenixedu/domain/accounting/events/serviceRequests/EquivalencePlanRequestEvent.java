package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.EquivalencePlanRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class EquivalencePlanRequestEvent extends EquivalencePlanRequestEvent_Base {
    
    protected EquivalencePlanRequestEvent() {
        super();
    }
    
    public EquivalencePlanRequestEvent(final AdministrativeOffice administrativeOffice, final Person person, final EquivalencePlanRequest academicServiceRequest) {
	this();
	super.init(administrativeOffice, EventType.EQUIVALENCE_PLAN_REQUEST, person, academicServiceRequest);
    }
    
    @Override
    public EquivalencePlanRequest getAcademicServiceRequest() {
        return (EquivalencePlanRequest) super.getAcademicServiceRequest();
    }
    
    private Registration getRegistration() {
	return getAcademicServiceRequest().getRegistration();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	
	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	labelFormatter.appendLabel(" (");
	labelFormatter.appendLabel("label.in", LabelFormatter.APPLICATION_RESOURCES);
	labelFormatter.appendLabel(" ");
	labelFormatter.appendLabel(getRegistration().getLastDegreeCurricularPlan().getName());
	labelFormatter.appendLabel(")");
	if (getAcademicServiceRequest().hasExecutionYear()) {
	    labelFormatter.appendLabel(" - " + getExecutionYear().getYear());
	}
	
	return labelFormatter;
    }
    
}
