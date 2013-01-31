package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import net.sourceforge.fenixedu.util.researcher.ResearchResultMetaDataManager;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteResultParticipation extends FenixService {
	@Service
	public static void run(ResultParticipation participation) throws FenixServiceException {
		ResearchResult result = participation.getResult();
		result.removeParticipation(participation);
		ResearchResultMetaDataManager.updateMetaDataInStorageFor(result);
	}
}