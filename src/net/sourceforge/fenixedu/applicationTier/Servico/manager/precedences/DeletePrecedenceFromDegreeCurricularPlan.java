package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.Precedence;

public class DeletePrecedenceFromDegreeCurricularPlan extends FenixService {

    public void run(Integer precedenceID) throws FenixServiceException {
	Precedence precedence = rootDomainObject.readPrecedenceByOID(precedenceID);

	if (precedence != null)
	    precedence.delete();
    }
}