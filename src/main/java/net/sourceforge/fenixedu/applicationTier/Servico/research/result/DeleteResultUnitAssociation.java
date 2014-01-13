package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import pt.ist.fenixframework.Atomic;

public class DeleteResultUnitAssociation {

    @Atomic
    public static void run(String oid) {
        final ResultUnitAssociation association = ResultUnitAssociation.readByOid(oid);
        final ResearchResult result = association.getResult();
        result.removeUnitAssociation(association);
    }
}