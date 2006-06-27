package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.domain.research.result.ResultPatent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteResultPatent extends Service {

    public void run(Integer oid) throws ExcepcaoPersistencia {
        ResultPatent patent = (ResultPatent) rootDomainObject.readResultByOID(oid);
        
        for(ResultParticipation resultParticipation : patent.getResultParticipations()){
            ((ResultParticipation) rootDomainObject.readResultParticipationByOID(resultParticipation.getIdInternal())).delete();
        }
        patent.delete();
    }
}
