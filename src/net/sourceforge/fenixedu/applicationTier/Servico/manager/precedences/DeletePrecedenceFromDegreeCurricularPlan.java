package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.Precedence;

public class DeletePrecedenceFromDegreeCurricularPlan extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer precedenceID) throws FenixServiceException {
	Precedence precedence = rootDomainObject.readPrecedenceByOID(precedenceID);

	if (precedence != null)
	    precedence.delete();
    }
}