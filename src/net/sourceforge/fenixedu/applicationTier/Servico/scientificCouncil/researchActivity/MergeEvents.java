package net.sourceforge.fenixedu.applicationTier.Servico.scientificCouncil.researchActivity;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.MergeEventPageContainerBean;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.activity.ScientificJournal;

public class MergeEvents extends Service {
    
    public void run(MergeEventPageContainerBean mergeEventPageContainerBean) {
	Event event = new Event(mergeEventPageContainerBean.getName(), mergeEventPageContainerBean.getEventType(), 
		mergeEventPageContainerBean.getResearchActivityLocationType());
	event.setUrl(mergeEventPageContainerBean.getUrl());
	
	for (DomainObject domainObject : mergeEventPageContainerBean.getSelectedObjects()) {
	    Event event2 = (Event) domainObject;
	    event.getEventEditions().addAll(event2.getEventEditions());
	    event.getParticipationsSet().addAll(event2.getParticipationsSet());
	    event2.delete();
	}
    }

}
