package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;

import pt.ist.fenixWebFramework.services.Service;

import pt.ist.fenixWebFramework.security.accessControl.Checked;

import net.sourceforge.fenixedu.applicationTier.FenixService;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.Restriction;

public class DeleteRestrictionFromDegreeCurricularPlan extends FenixService {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(Integer restrictionID) throws FenixServiceException {
	Restriction restriction = rootDomainObject.readRestrictionByOID(restrictionID);

	restriction.delete();
    }
}