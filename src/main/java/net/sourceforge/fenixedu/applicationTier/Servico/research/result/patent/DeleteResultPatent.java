package net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent;

import net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteResultPatent {

    @Atomic
    public static void run(String oid) {
        ResearchResultPatent patent = (ResearchResultPatent) FenixFramework.getDomainObject(oid);
        patent.delete();
    }
}