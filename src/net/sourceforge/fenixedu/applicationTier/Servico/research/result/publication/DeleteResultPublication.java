package net.sourceforge.fenixedu.applicationTier.Servico.research.result.publication;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication;

public class DeleteResultPublication extends Service {

    public void run(Integer oid) throws FenixServiceException {
	ResearchResultPublication publication = (ResearchResultPublication) rootDomainObject.readResearchResultByOID(oid);
	if (publication == null)
	    throw new FenixServiceException();
	publication.delete();
    }
}
