package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;

public class DeleteResultEventAssociation extends Service  {

    public void run(Integer associationId, String personName) throws InvalidArgumentsServiceException  {
        final ResultEventAssociation association = rootDomainObject.readResultEventAssociationByOID(associationId);
        if(association == null){
            throw new InvalidArgumentsServiceException();
        }
        association.delete(personName);   
    }
    
}
