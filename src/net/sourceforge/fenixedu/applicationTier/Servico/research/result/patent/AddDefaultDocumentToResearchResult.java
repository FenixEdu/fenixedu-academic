package net.sourceforge.fenixedu.applicationTier.Servico.research.result.patent;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;

public class AddDefaultDocumentToResearchResult extends Service {

	public void run(ResearchResult result) {
		ResearchResultMetaDataManager.addDefaultDocument(result);
	}
}
