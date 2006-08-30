package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationCreationBean;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;

public class CreateResultEventAssociation extends Service {

    public ResultEventAssociation run(ResultEventAssociationCreationBean bean)
	    throws Exception {
	final Result result = bean.getResult();
	ResultEventAssociation association = null;
	Event event = bean.getEvent();

	try {
	    if (event == null) {
		event = new Event(bean.getEventNameMLS(), bean.getEventType());
	    }
	    association = new ResultEventAssociation(result, event, bean.getRole());
	} catch (Exception e) {
	    if (event != null) {
		event.delete();
	    }
	    throw e;
	}
	return association;
    }
}