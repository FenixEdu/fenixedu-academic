package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class DeleteProjectEventAssociation {

    @Checked("ResultPredicates.author")
    @Service
    public static void run(String associationId) throws FenixServiceException {
        ProjectEventAssociation association = FenixFramework.getDomainObject(associationId);
        if (association == null) {
            throw new FenixServiceException();
        }
        association.delete();
    }

}