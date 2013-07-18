package net.sourceforge.fenixedu.applicationTier.Servico.research.project;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteProjectEventAssociation {

    @Checked("ResultPredicates.author")
    @Service
    public static void run(Integer associationId) throws FenixServiceException {
        ProjectEventAssociation association = RootDomainObject.getInstance().readProjectEventAssociationByOID(associationId);
        if (association == null) {
            throw new FenixServiceException();
        }
        association.delete();
    }

}