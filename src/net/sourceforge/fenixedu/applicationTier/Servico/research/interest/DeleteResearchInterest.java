package net.sourceforge.fenixedu.applicationTier.Servico.research.interest;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;

public class DeleteResearchInterest extends FenixService {

    public void run(Integer oid) throws FenixServiceException {
	ResearchInterest researchInterest = rootDomainObject.readResearchInterestByOID(oid);
	if (researchInterest == null) {
	    throw new FenixServiceException();
	}
	researchInterest.delete();
    }
}
