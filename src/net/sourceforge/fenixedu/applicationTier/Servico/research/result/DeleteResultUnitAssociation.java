package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.accessControl.AccessControl;
import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteResultUnitAssociation extends Service  {

    public void run(Integer associationId) throws ExcepcaoPersistencia, FenixServiceException {
        final ResultUnitAssociation association = rootDomainObject.readResultUnitAssociationByOID(associationId);
        if(association == null){
            throw new InvalidArgumentsServiceException();
        }
        
        final Person person = AccessControl.getUserView().getPerson();
        association.delete(person.getName());
    }
}
