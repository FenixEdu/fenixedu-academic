package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.Project;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteResearchProject {

    @Checked("ResultPredicates.author")
    @Service
    public static void run(String oid) throws FenixServiceException {
        Project project = AbstractDomainObject.fromExternalId(oid);
        if (project == null) {
            throw new FenixServiceException();
        }
        project.delete();
    }
}