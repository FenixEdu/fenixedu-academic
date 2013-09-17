package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import java.util.List;

import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import pt.ist.fenixframework.Atomic;

public class SaveResultParticipationsOrder {
    @Atomic
    public static void run(ResearchResult result, List<ResultParticipation> newParticipationsOrder) {
        result.setParticipationsOrder(newParticipationsOrder);
    }
}