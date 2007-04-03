package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import org.joda.time.YearMonthDay;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchEventEditionCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultEventAssociationBean;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;

public class CreateResearchEventEdition extends Service {

    public EventEdition run(ResultEventAssociationBean eventBean) {

	Event event = null;
	if (eventBean.getEvent() == null) {
	    CreateResearchEvent service = new CreateResearchEvent();
	    event = (Event) service.run(eventBean.getEventName(), eventBean.getEventType(), eventBean
		    .getLocationType(), eventBean.getEventUrl());
	} else {
	    event = eventBean.getEvent();
	}

	return run(eventBean.getEvent(), eventBean.getEdition(), eventBean.getEventLocation(), eventBean
		.getStartDate(), eventBean.getEndDate(), eventBean.getOrganization(), eventBean
		.getEventEditionUrl());
    }

    public EventEdition run(Event event, String edition, String eventLocation, YearMonthDay startDate,
	    YearMonthDay endDate, String organization, String url) {

	EventEdition eventEdition = new EventEdition(event);
	eventEdition.setEdition(edition);
	eventEdition.setEventLocation(eventLocation);
	eventEdition.setStartDate(startDate);
	eventEdition.setEndDate(endDate);
	eventEdition.setOrganization(organization);
	eventEdition.setUrl(url);

	return eventEdition;
    }
    
    public EventEdition run(Event event, ResearchEventEditionCreationBean bean) {
	return run(event,bean.getEventEditionName(), bean.getEventLocation(), bean.getStartDate(),
	    bean.getEndDate(), bean.getOrganization(), bean.getEditionUrl());
    }
}
