package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventType;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivityLocationType;

import org.joda.time.YearMonthDay;

public class CreateResearchEvent extends Service {


	public Event run(String name, EventType eventType, ResearchActivityLocationType locationType,
			String location, YearMonthDay startDate, YearMonthDay endDate) {
	
		Event event = new Event();
		event.setName(name);
		event.setEventType(eventType);
		event.setLocationType(locationType);
		event.setEventLocation(location);
		event.setStartDate(startDate);
		event.setEndDate(endDate);
		
		return event;
	}
}
