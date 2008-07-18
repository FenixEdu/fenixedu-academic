package net.sourceforge.fenixedu.domain.accounting.events.serviceRequests;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accounting.EntryType;
import net.sourceforge.fenixedu.domain.accounting.EventType;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.serviceRequests.CourseGroupChangeRequest;
import net.sourceforge.fenixedu.util.Money;
import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class PastCourseGroupChangeRequestEvent extends PastCourseGroupChangeRequestEvent_Base implements IPastRequestEvent {

    protected PastCourseGroupChangeRequestEvent() {
	super();
    }

    public PastCourseGroupChangeRequestEvent(final AdministrativeOffice administrativeOffice, final Person person,
	    final CourseGroupChangeRequest request) {
	this();
	super.init(administrativeOffice, EventType.PAST_COURSE_GROUP_CHANGE_REQUEST, person, request);
    }

    @Override
    public void setPastAmount(Money amount) {
	throw new DomainException("error.accounting.events.cannot.modify.pastAmount");
    }

    @Override
    public LabelFormatter getDescriptionForEntryType(EntryType entryType) {
	final LabelFormatter labelFormatter = new LabelFormatter();

	labelFormatter.appendLabel(entryType.name(), LabelFormatter.ENUMERATION_RESOURCES);
	if (getAcademicServiceRequest().hasExecutionYear()) {
	    labelFormatter.appendLabel(" - " + getExecutionYear().getYear());
	}
	return labelFormatter;
    }

}
