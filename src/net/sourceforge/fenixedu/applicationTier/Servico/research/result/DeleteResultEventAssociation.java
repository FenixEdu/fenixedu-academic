package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;

public class DeleteResultEventAssociation extends Service  {

    public void run(Integer associationId) throws InvalidArgumentsServiceException  {
        final ResultEventAssociation association = rootDomainObject.readResultEventAssociationByOID(associationId);
        final Person person = AccessControl.getUserView().getPerson();
        
        if(association == null){
            throw new InvalidArgumentsServiceException();
        }
        association.delete(person.getName());   
    }
    
}
