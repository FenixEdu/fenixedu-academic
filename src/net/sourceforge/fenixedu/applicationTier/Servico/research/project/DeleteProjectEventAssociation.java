package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.ProjectEventAssociation;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteProjectEventAssociation extends FenixService {

    public void run(Integer associationId) throws FenixServiceException {
	ProjectEventAssociation association = rootDomainObject.readProjectEventAssociationByOID(associationId);
	if (association == null) {
	    throw new FenixServiceException();
	}
	association.delete();
    }

}
