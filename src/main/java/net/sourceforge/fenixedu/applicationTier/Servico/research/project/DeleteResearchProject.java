package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.Project;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteResearchProject {

    @Checked("ResultPredicates.author")
    @Atomic
    public static void run(String oid) throws FenixServiceException {
        Project project = FenixFramework.getDomainObject(oid);
        if (project == null) {
            throw new FenixServiceException();
        }
        project.delete();
    }
}