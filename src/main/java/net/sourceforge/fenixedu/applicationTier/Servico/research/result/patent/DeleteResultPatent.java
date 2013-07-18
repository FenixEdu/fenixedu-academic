package net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent;


import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteResultPatent {

    @Service
    public static void run(Integer oid) {
        ResearchResultPatent patent = (ResearchResultPatent) RootDomainObject.getInstance().readResearchResultByOID(oid);
        patent.delete();
    }
}