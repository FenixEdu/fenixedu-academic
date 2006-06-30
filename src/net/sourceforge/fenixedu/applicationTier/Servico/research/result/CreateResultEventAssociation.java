package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixRemoteServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationFullCreationBean;
import net.sourceforge.fenixedu.dataTransferObject.research.result.ResultEventAssociationSimpleCreationBean;
import net.sourceforge.fenixedu.domain.research.event.Event;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class CreateResultEventAssociation extends Service  {

    /**
     * Service responsible for creating an association between a result and an event
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param resultId - the identifier of the Result for whom the association is being created 
     * @return the newly created ResultEventAssociation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the result doesn't exist.
     */
    public ResultEventAssociation run(ResultEventAssociationSimpleCreationBean bean, Integer resultId) throws ExcepcaoPersistencia, FenixServiceException {
        ResultEventAssociation association = null;
        final Result result = rootDomainObject.readResultByOID(resultId);
        if(result == null){
            throw new FenixServiceException();
        }

        if(!result.hasAssociationWithEventRole(bean.getEvent(), bean.getRole())) {
            association = new ResultEventAssociation();
            association.setResult(result);
            association.setEvent(bean.getEvent());
            association.setRole(bean.getRole());    
        }
        
        return association;
    }
    
    /**
     * Service responsible for creating an association between a result and an event (also created)
     * @param bean - Bean responsible for carrying the information from the presentation to the services layer
     * @param resultId - the identifier of the Result for whom the association is being created 
     * @return the newly created ResultEventAssociation
     * @throws ExcepcaoPersistencia
     * @throws FenixServiceException - In case the result doesn't exist.
     */
    public ResultEventAssociation run(ResultEventAssociationFullCreationBean bean, Integer resultId) throws ExcepcaoPersistencia, FenixServiceException {
        ResultEventAssociation association = null;
        
        final Result result = rootDomainObject.readResultByOID(resultId);
        if(result == null){
            throw new FenixServiceException();
        }
        
        final Event event = new Event(bean.getEventName(), bean.getEventType());
        // In this case the test must always be true. If we have just created the event
        // then it is obviously that the result do not have any association with this one.
        if(!result.hasAssociationWithEventRole(event, bean.getRole())) {
            association = new ResultEventAssociation();
            association.setResult(result);
            association.setEvent(event);
            association.setRole(bean.getRole());        
        }
        else {
            event.delete();
        }
        
        return association;
    }    
}