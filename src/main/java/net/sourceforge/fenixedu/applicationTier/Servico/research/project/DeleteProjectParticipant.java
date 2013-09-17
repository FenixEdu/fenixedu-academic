package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.ProjectParticipation;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteProjectParticipant {

    @Atomic
    public static void run(String participationId) throws FenixServiceException {
        check(ResultPredicates.author);
        ProjectParticipation participation = FenixFramework.getDomainObject(participationId);
        if (participation == null) {
            throw new FenixServiceException();
        }
        participation.delete();
    }
}