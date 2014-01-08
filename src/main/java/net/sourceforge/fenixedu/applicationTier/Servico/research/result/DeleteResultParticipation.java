package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.ResearchResult;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;
import pt.ist.fenixframework.Atomic;

public class DeleteResultParticipation {
    @Atomic
    public static void run(ResultParticipation participation) throws FenixServiceException {
        ResearchResult result = participation.getResult();
        result.removeParticipation(participation);
    }
}