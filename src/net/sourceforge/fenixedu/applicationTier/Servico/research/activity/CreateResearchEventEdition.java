package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultEventAssociationBean;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;

public class CreateResearchEventEdition extends Service {

	public EventEdition run(ResultEventAssociationBean eventBean){
		
		Event event = null;
		if(eventBean.getEvent() == null) {
			CreateResearchEvent service = new CreateResearchEvent();
			event = (Event)service.run(eventBean.getEventName(), eventBean.getEventType(), eventBean.getLocationType(), eventBean.getEventUrl());
		}
		else {
			event = eventBean.getEvent();
		}
		
		EventEdition eventEdition = new EventEdition(event);
		eventEdition.setEdition(eventBean.getEdition());
		eventEdition.setEventLocation(eventBean.getEventLocation());
		eventEdition.setStartDate(eventBean.getStartDate());
		eventEdition.setEndDate(eventBean.getEndDate());
		eventEdition.setOrganization(eventBean.getOrganization());
		eventEdition.setUrl(eventBean.getEventEditionUrl());
		
		return eventEdition;
	}	
}
