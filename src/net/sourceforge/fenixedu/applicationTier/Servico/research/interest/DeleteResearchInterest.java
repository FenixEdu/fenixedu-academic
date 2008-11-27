package net.sourceforge.fenixedu.applicationTier.Servico.research.interest;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;

public class DeleteResearchInterest extends FenixService {

    @Checked("RolePredicates.RESEARCHER_PREDICATE")
    @Service
    public static void run(Integer oid) throws FenixServiceException {
	ResearchInterest researchInterest = rootDomainObject.readResearchInterestByOID(oid);
	if (researchInterest == null) {
	    throw new FenixServiceException();
	}
	researchInterest.delete();
    }
}