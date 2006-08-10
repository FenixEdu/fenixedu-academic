package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationCreationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;

public class CreateResultEventAssociation extends Service  {

    public ResultEventAssociation run(ResultEventAssociationCreationBean bean) throws FenixServiceException {
        final Result result = bean.getResult();
        
        ResultEventAssociation association = null;
        if(bean.getEvent()==null) {
            final Event event = new Event(bean.getEventNameMLS(), bean.getEventType());
            
            try {
                association = new ResultEventAssociation(result, event, bean.getRole());
            } catch (DomainException e) {
                event.delete();
                throw new FenixServiceException(e.getKey());
            }
        }
        else {
            association = new ResultEventAssociation(result,bean.getEvent(), bean.getRole()); 
        }      
        return association;
    }
}