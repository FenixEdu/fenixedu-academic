package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.predicates.ResultPredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteResearchProject {

    @Atomic
    public static void run(String oid) throws FenixServiceException {
        check(ResultPredicates.author);
        Project project = FenixFramework.getDomainObject(oid);
        if (project == null) {
            throw new FenixServiceException();
        }
        project.delete();
    }
}