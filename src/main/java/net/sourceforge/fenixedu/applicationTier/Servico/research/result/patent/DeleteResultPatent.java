package net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent;

import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class DeleteResultPatent {

    @Service
    public static void run(String oid) {
        ResearchResultPatent patent = (ResearchResultPatent) FenixFramework.getDomainObject(oid);
        patent.delete();
    }
}