package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ChangeResultParticipationsOrder extends Service {
    public void run(Integer resultParticipationId, Integer offset, String personName) throws ExcepcaoPersistencia, FenixServiceException {
        final ResultParticipation resultParticipation = (ResultParticipation) rootDomainObject.readResultParticipationByOID(resultParticipationId);
        
        if(resultParticipation == null) {
            throw new FenixServiceException("error.ResultParticipation.not.found");
        }
        
        if(!resultParticipation.changeOrder(offset, personName)) {
            throw new FenixServiceException("error.ResultParticipation.changeOrder");
        }   
    }
}
