package net.sourceforge.fenixedu.applicationTier.Servico.research.result;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.ResultParticipation;

public class DeleteResultParticipation extends Service {
    public void run(ResultParticipation participation) throws FenixServiceException {
	participation.delete();
    }
}
