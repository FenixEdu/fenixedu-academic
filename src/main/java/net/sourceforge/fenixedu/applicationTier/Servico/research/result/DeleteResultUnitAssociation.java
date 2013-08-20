package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteResultUnitAssociation {

    @Service
    public static void run(String oid) {
        final ResultUnitAssociation association = ResultUnitAssociation.readByOid(oid);
        final ResearchResult result = association.getResult();
        result.removeUnitAssociation(association);
        ResearchResultMetaDataManager.updateMetaDataInStorageFor(result);
    }
}