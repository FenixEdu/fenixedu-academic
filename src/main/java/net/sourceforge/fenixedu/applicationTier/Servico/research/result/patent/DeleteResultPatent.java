package net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent;


import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteResultPatent {

    @Service
    public static void run(Integer oid) {
        ResearchResultPatent patent = (ResearchResultPatent) AbstractDomainObject.fromExternalId(oid);
        patent.delete();
    }
}