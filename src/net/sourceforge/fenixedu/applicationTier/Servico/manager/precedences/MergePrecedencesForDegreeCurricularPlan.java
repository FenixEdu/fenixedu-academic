package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.domain.precedences.Precedence;

public class MergePrecedencesForDegreeCurricularPlan extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer firstPrecedenceID, Integer secondPrecedenceID) throws FenixServiceException {

	if (firstPrecedenceID.intValue() == secondPrecedenceID.intValue()) {
	    throw new InvalidArgumentsServiceException("error.manager.samePrecedencesForMerge");
	}

	Precedence firstPrecedence = rootDomainObject.readPrecedenceByOID(firstPrecedenceID);
	Precedence secondPrecedence = rootDomainObject.readPrecedenceByOID(secondPrecedenceID);

	firstPrecedence.mergePrecedences(secondPrecedence);
    }

}