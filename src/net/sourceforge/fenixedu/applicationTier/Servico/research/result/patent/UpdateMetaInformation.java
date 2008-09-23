package net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;

public class UpdateMetaInformation extends FenixService {

    public void run(ResearchResult result) {
	ResearchResultMetaDataManager.updateMetaDataInStorageFor(result);
    }
}
