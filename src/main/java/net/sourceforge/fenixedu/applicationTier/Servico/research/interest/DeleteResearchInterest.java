package net.sourceforge.fenixedu.applicationTier.Servico.research.interest;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.research.ResearchInterest;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

public class DeleteResearchInterest {

    @Service
    public static void run(String oid) throws FenixServiceException {
        ResearchInterest researchInterest = FenixFramework.getDomainObject(oid);
        if (researchInterest == null) {
            throw new FenixServiceException();
        }
        researchInterest.delete();
    }
}