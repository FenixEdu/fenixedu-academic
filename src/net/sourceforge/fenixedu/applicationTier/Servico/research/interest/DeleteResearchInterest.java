package net.sourceforge.fenixedu.applicationTier.Servico.research.interest;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import pt.ist.fenixWebFramework.services.Service;

public class DeleteResearchInterest extends FenixService {

	@Service
	public static void run(Integer oid) throws FenixServiceException {
		ResearchInterest researchInterest = rootDomainObject.readResearchInterestByOID(oid);
		if (researchInterest == null) {
			throw new FenixServiceException();
		}
		researchInterest.delete();
	}
}