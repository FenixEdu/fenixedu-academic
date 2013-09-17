package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation;
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteProjectEventAssociation {

    @Atomic
    public static void run(String associationId) throws FenixServiceException {
        check(ResultPredicates.author);
        ProjectEventAssociation association = FenixFramework.getDomainObject(associationId);
        if (association == null) {
            throw new FenixServiceException();
        }
        association.delete();
    }

}