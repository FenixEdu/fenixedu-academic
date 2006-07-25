package net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteResultPatent extends Service {

    public void run(Integer oid) throws ExcepcaoPersistencia, FenixServiceException {
        ResultPatent patent = (ResultPatent) rootDomainObject.readResultByOID(oid);
        
        if(patent==null) {
            throw new FenixServiceException();
        }
        patent.delete();
    }
}
