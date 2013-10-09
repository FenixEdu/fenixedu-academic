/*
 * Created on 17/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

public class InsertBranch {

    @Atomic
    public static void run(InfoBranch infoBranch) throws NonExistingServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        final String degreeCurricularPlanId = infoBranch.getInfoDegreeCurricularPlan().getExternalId();
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);

        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        new Branch(infoBranch.getName(), infoBranch.getNameEn(), infoBranch.getCode(), degreeCurricularPlan);
    }

}