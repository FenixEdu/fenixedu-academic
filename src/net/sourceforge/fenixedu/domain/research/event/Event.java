package net.sourceforge.fenixedu.domain.research.event;

import org.joda.time.YearMonthDay;

public class Event extends Event_Base {
    
    public  Event(YearMonthDay endDate, YearMonthDay startDate, String eventLocation, Boolean feeOrFree, EventType type) {
        setEndDate(endDate);
        setStartDate(startDate);
        setEventLocation(eventLocation);
        setFeeOrFree(feeOrFree);
        setType(type);
    }
}
