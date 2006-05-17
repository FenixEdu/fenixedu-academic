package net.sourceforge.fenixedu.domain.research.event;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.util.MultiLanguageString;

import org.joda.time.YearMonthDay;

public class Event extends Event_Base {

	public Event() {
		setRootDomainObject(RootDomainObject.getInstance());
	}
    
    public Event(MultiLanguageString name, EventType type) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setName(name);
        setEventType(type);
    }    

	public Event(YearMonthDay endDate, YearMonthDay startDate, String eventLocation, Boolean fee,
			EventType type, MultiLanguageString name) {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
		setEndDate(endDate);
		setStartDate(startDate);
		setEventLocation(eventLocation);
		setFee(fee);
		setName(name);
		setEventType(type);
	}

    public void delete(){
        for (;!this.getEventParticipations().isEmpty(); getEventParticipations().get(0).delete());
        removeRootDomainObject();
        deleteDomainObject();
    }
}
