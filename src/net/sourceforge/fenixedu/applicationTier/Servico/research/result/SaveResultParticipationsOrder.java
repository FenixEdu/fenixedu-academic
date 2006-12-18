package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;

public class SaveResultParticipationsOrder extends Service {
    public void run(ResearchResult result, List<ResultParticipation> newParticipationsOrder) {
	result.setParticipationsOrder(newParticipationsOrder);
    }
}
