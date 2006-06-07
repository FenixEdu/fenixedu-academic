package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.Authorship;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteResultAuthorships extends Service {
    public void run(Integer resultId, String[] authorsToRemoveIds) throws ExcepcaoPersistencia {
        Result result = rootDomainObject.readResultByOID(resultId);
        List<Authorship> resultAuthorships = result.getResultAuthorships();
        
        if (authorsToRemoveIds != null) {
            for (int i = authorsToRemoveIds.length-1 ; i >= 0 ; i--) {
                Authorship author = rootDomainObject.readAuthorshipByOID(Integer
                        .valueOf(authorsToRemoveIds[i]));
                //It remains at least one author associated to Result
                if (resultAuthorships.size() > 1 && author != null) {
                    ((Authorship) rootDomainObject.readAuthorshipByOID(author.getIdInternal())).delete();
                }
            }
        }
    }
}
