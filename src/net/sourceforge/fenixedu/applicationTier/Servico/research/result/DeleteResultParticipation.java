package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteResultParticipation extends Service {
    public void run(Integer resultParticipationId, String personName) throws ExcepcaoPersistencia, FenixServiceException {
        
        ResultParticipation resultParticipation = rootDomainObject.readResultParticipationByOID(resultParticipationId);
        if(resultParticipation == null){
            throw new FenixServiceException();
        }
        resultParticipation.delete(personName);  
    }
}
