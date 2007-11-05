package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.StudentReingressionRequest;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class StudentReingressionRequestEvent extends InternalDegreeChangeRequestEvent_Base {
    
    protected StudentReingressionRequestEvent() {
        super();
    }
    
    public StudentReingressionRequestEvent(final AdministrativeOffice administrativeOffice, final Person person, final StudentReingressionRequest academicServiceRequest) {
	this();
	super.init(administrativeOffice, EventType.STUDENT_REINGRESSION_REQUEST, person, academicServiceRequest);
    }
    
    @Override
    public StudentReingressionRequest getAcademicServiceRequest() {
        return (StudentReingressionRequest) super.getAcademicServiceRequest();
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
