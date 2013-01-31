package net.sourceforge.fenixedu.applicationTier.Servico.research.activity;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.dataTransferObject.research.activity.ResearchEventEditionCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.publication.ResultEventAssociationBean;
import net.sourceforge.fenixedu.domain.research.activity.EventEdition;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;

import org.joda.time.YearMonthDay;

import pt.ist.fenixWebFramework.services.Service;

public class CreateResearchEventEdition extends FenixService {

	@Service
	public static EventEdition run(ResultEventAssociationBean eventBean) {

		ResearchEvent event = null;
		if (eventBean.getEvent() == null) {
			CreateResearchEvent service = new CreateResearchEvent();
			event =
					service.run(eventBean.getEventName(), eventBean.getEventType(), eventBean.getLocationType(),
							eventBean.getEventUrl());
		} else {
			event = eventBean.getEvent();
		}

		return run(event, eventBean.getEdition(), eventBean.getEventLocation(), eventBean.getStartDate(), eventBean.getEndDate(),
				eventBean.getOrganization(), eventBean.getEventEditionUrl());
	}

	@Service
	public static EventEdition run(ResearchEvent event, String edition, String eventLocation, YearMonthDay startDate,
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

	@Service
	public static EventEdition run(ResearchEvent event, ResearchEventEditionCreationBean bean) {
		return run(event, bean.getEventEditionName(), bean.getEventLocation(), bean.getStartDate(), bean.getEndDate(),
				bean.getOrganization(), bean.getEditionUrl());
	}
}