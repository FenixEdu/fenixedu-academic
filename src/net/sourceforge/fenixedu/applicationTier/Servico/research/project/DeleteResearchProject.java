package net.sourceforge.fenixedu.applicationTier.Servico.research.project;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.project.Project;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;

public class DeleteResearchProject extends Service {

    public void run(Integer oid) throws FenixServiceException {
	Project project = rootDomainObject.readProjectByOID(oid);
	if (project == null) {
	    throw new FenixServiceException();
	}
	project.delete();
    }
}
