package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.CourseLoadRequest;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class CourseLoadRequestEvent extends CourseLoadRequestEvent_Base {
    
    protected CourseLoadRequestEvent() {
        super();
    }
    
    public CourseLoadRequestEvent(final AdministrativeOffice administrativeOffice, final Person person, final CourseLoadRequest request) {
	this();
	super.init(administrativeOffice, request.getEventType(), person, request);
    }
    
    @Override
    public CourseLoadRequest getAcademicServiceRequest() {
        return (CourseLoadRequest) super.getAcademicServiceRequest();
    }
    
    public Integer getNumberOfPages() {
	return getAcademicServiceRequest().getNumberOfPages();
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(final EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();
	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	if (getAcademicServiceRequest().hasExecutionYear()) {
	    labelFormatter.appendLabel(" - " + getExecutionYear().getYear());
	}
	return labelFormatter;
    }
    
}
