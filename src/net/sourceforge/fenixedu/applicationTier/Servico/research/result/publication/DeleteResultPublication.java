package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.publication.ResultPublication;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteResultPublication extends Service {

    public void run(Integer oid) throws ExcepcaoPersistencia, FenixServiceException {
        ResultPublication publication = (ResultPublication) rootDomainObject.readResultByOID(oid);
        if(publication == null)
            throw new FenixServiceException();
        publication.delete();
    }
}
