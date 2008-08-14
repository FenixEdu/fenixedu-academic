package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.researchActivity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.MergeEventPageContainerBean;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.research.activity.EventParticipation;
import net.sourceforge.fenixedu.domain.research.activity.ResearchEvent;

public class MergeEvents extends Service {

    public void run(MergeEventPageContainerBean mergeEventPageContainerBean) {
	ResearchEvent event = new ResearchEvent(mergeEventPageContainerBean.getName(),
		mergeEventPageContainerBean.getEventType(), mergeEventPageContainerBean.getResearchActivityLocationType());
	event.setUrl(mergeEventPageContainerBean.getUrl());

	for (DomainObject domainObject : mergeEventPageContainerBean.getSelectedObjects()) {
	    ResearchEvent event2 = (ResearchEvent) domainObject;
	    event.getEventEditions().addAll(event2.getEventEditions());

	    for (EventParticipation eventParticipation : event2.getParticipationsSet()) {
		event.addUniqueParticipation(eventParticipation);
	    }
	    event2.delete();
	}
    }

}
