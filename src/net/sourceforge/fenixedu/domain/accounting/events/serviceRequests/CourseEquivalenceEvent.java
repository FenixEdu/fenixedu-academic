package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.CourseEquivalenceRequest;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class CourseEquivalenceEvent extends CourseEquivalenceEvent_Base {

    public CourseEquivalenceEvent(AdministrativeOffice administrativeOffice, EventType eventType,
	    Person person, CourseEquivalenceRequest request) {
	super.init(administrativeOffice, eventType, person, request);
    }
    
    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	return labelFormatter;
    }
 
    public CourseEquivalenceRequest getCourseEquivalenceRequest() {
	return (CourseEquivalenceRequest) getAcademicServiceRequest();
    }
}
