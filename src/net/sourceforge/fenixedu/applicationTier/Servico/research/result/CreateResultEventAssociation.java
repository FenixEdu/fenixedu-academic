package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationSimpleCreationBean;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateResultEventAssociation extends Service  {

    public ResultEventAssociation run(ResultEventAssociationSimpleCreationBean bean, Integer resultId, String personName) throws ExcepcaoPersistencia, FenixServiceException {
        final Result result = rootDomainObject.readResultByOID(resultId);
        if(result == null){ throw new InvalidArgumentsServiceException(); }

        final ResultEventAssociation association = new ResultEventAssociation(result,bean.getEvent(), bean.getRole(), personName);
        
        return association;
    }
    
    public ResultEventAssociation run(ResultEventAssociationFullCreationBean bean, Integer resultId, String personName) throws ExcepcaoPersistencia, FenixServiceException {
        final Result result = rootDomainObject.readResultByOID(resultId);
        if(result == null){ throw new InvalidArgumentsServiceException(); }        

        final Event event = new Event(bean.getEventName(), bean.getEventType());
        ResultEventAssociation association = null;
        
        try {
            association = new ResultEventAssociation(result, event, bean.getRole(), personName);
        } catch (DomainException e) {
            event.delete();
            throw new FenixServiceException(e.getKey());
        }

        return association;
    }    
}