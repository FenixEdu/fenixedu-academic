package net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.patent.ResultPatent;

public class DeleteResultPatent extends Service {

    public void run(Integer oid) {
        ResultPatent patent = (ResultPatent) rootDomainObject.readResultByOID(oid);
        patent.delete();
    }
}
