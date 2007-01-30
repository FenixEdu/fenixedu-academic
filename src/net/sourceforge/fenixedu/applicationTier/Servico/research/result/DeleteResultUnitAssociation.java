package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultUnitAssociation;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;

public class DeleteResultUnitAssociation extends Service {

    public void run(Integer oid) {
	final ResultUnitAssociation association = ResultUnitAssociation.readByOid(oid);
	final ResearchResult result =association.getResult(); 
	result.removeUnitAssociation(association);
	ResearchResultMetaDataManager.updateMetaDataInStorageFor(result);
    }
}
