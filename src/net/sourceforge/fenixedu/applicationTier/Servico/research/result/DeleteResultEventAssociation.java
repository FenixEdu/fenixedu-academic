package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.ResultEventAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteResultEventAssociation extends Service  {

    public void run(Integer associationId) throws ExcepcaoPersistencia, FenixServiceException {
        ResultEventAssociation association = rootDomainObject.readResultEventAssociationByOID(associationId);
        if(association == null){
            throw new FenixServiceException();
        }
        association.delete();   
    }
    
}
