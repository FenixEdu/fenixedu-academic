package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationCreationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.activity.Event;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;

public class CreateResultEventAssociation extends Service {

    public void run(ResultEventAssociationCreationBean bean) {
	final ResearchResult result = bean.getResult();
	Event event = bean.getEvent();

	try {
	    if (event == null) {
		event = new Event(bean.getEventNameMLS(), bean.getEventType());
	    }
	    result.addEventAssociation(event, bean.getRole());
	} catch (DomainException e) {
	    if (event != null) {
		event.delete();
	    }
	    throw e;
	}
    }
}