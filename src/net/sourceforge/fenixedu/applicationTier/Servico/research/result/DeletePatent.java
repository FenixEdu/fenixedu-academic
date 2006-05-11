package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.Authorship;
import net.sourceforge.fenixedu.domain.research.result.Patent;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeletePatent extends Service {

    public void run(Integer oid) throws ExcepcaoPersistencia {
        Patent patent = (Patent) rootDomainObject.readResultByOID(oid);
        
        for(Authorship author : patent.getResultAuthorships()){
            ((Authorship) rootDomainObject.readAuthorshipByOID(author.getIdInternal())).delete();
        }
        patent.delete();
    }
}
