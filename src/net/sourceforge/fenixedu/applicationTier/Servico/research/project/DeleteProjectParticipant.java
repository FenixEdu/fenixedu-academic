package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteProjectParticipant extends Service  {

    public void run(Integer participationId) throws FenixServiceException {
        ProjectParticipation participation = rootDomainObject.readProjectParticipationByOID(participationId);
        if(participation == null){
            throw new FenixServiceException();
        }
        participation.delete();   
    }
}
