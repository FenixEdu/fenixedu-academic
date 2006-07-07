package net.sourceforge.fenixedu.applicationTier.Servico.research.event;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.event.EventParticipation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteEventParticipant extends Service  {

    public void run(Integer participationId) throws ExcepcaoPersistencia, FenixServiceException {
        EventParticipation participation = rootDomainObject.readEventParticipationByOID(participationId);
        if(participation == null){
            throw new FenixServiceException();
        }
        participation.delete();   
    }
}
