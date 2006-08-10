package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.research.result.Result;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;

public class DeleteResultParticipation extends Service {
    public void run(ResultParticipation participation) {
        final Result result = participation.getResult();
        participation.delete();  
        result.setModificationDateAndAuthor();
    }
}
