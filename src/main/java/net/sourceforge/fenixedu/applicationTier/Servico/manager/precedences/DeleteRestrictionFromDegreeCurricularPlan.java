package net.sourceforge.fenixedu.applicationTier.Servico.manager.precedences;


import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.precedences.Restriction;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class DeleteRestrictionFromDegreeCurricularPlan {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static void run(Integer restrictionID) throws FenixServiceException {
        Restriction restriction = AbstractDomainObject.fromExternalId(restrictionID);

        restriction.delete();
    }
}