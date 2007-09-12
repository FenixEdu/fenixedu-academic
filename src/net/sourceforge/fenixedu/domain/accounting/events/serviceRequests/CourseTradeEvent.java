package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.CourseTradeRequest;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class CourseTradeEvent extends CourseTradeEvent_Base {

    public CourseTradeEvent(AdministrativeOffice administrativeOffice, EventType eventType,
	    Person person, CourseTradeRequest request) {
	super.init(administrativeOffice, eventType, person, request);
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	return labelFormatter;
    }

    public CourseTradeRequest getCourseTradeRequest() {
	return (CourseTradeRequest) getAcademicServiceRequest();
    }
}
