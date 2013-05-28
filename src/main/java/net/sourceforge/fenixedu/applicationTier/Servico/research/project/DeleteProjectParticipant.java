package net.sourceforge.fenixedu.applicationTier.Servico.research.project;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteProjectParticipant {

    @Checked("ResultPredicates.author")
    @Service
    public static void run(Integer participationId) throws FenixServiceException {
        ProjectParticipation participation = AbstractDomainObject.fromExternalId(participationId);
        if (participation == null) {
            throw new FenixServiceException();
        }
        participation.delete();
    }
}