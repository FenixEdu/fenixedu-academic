package net.sourceforge.fenixedu.applicationTier.Servico.research.project;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.project.Project;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteResearchProject {

    @Checked("ResultPredicates.author")
    @Service
    public static void run(Integer oid) throws FenixServiceException {
        Project project = RootDomainObject.getInstance().readProjectByOID(oid);
        if (project == null) {
            throw new FenixServiceException();
        }
        project.delete();
    }
}