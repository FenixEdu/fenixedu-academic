package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class ChangeResultParticipationsOrder extends Service {
    public void run(Integer resultId, Integer resultParticipationId, Integer offset) throws ExcepcaoPersistencia {
        List<ResultParticipation> resultParticipations = rootDomainObject.readResultByOID(resultId).getResultParticipations();
        
        ResultParticipation resultParticipation = (ResultParticipation) rootDomainObject.readResultParticipationByOID(resultParticipationId);
        Integer newOrder = resultParticipation.getPersonOrder() + offset; 
        
        if (newOrder >= 0 && newOrder < resultParticipations.size()) {
            resultParticipation.setPersonOrder(newOrder);
        }
        
        for (ResultParticipation author : resultParticipations) {
            Integer order = author.getPersonOrder();
            if ((order >= newOrder) && ((order - offset) < resultParticipations.size()) && (author != resultParticipation)) {
                author.setPersonOrder(author.getPersonOrder() - offset);
            }
        }
    }
}
