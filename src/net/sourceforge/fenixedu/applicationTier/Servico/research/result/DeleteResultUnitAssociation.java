package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteResultUnitAssociation extends Service  {

    public void run(Integer associationId) throws ExcepcaoPersistencia, FenixServiceException {
        ResultUnitAssociation association = rootDomainObject.readResultUnitAssociationByOID(associationId);
        if(association == null){
            throw new FenixServiceException();
        }
        association.delete();   
    }
}
