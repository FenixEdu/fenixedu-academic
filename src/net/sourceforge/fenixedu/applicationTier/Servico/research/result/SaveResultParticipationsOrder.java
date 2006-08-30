package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;

public class SaveResultParticipationsOrder extends Service {
    public void run(Result result, List<ResultParticipation> newParticipationsOrder) {
	result.setParticipationsOrder(newParticipationsOrder);
    }
}
