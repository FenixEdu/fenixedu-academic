package net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;

public class DeleteResultPatent extends Service {

    public void run(Integer oid) {
        ResearchResultPatent patent = (ResearchResultPatent) rootDomainObject.readResearchResultByOID(oid);
        patent.delete();
    }
}
