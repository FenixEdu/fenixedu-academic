/*
 * Created on 17/Set/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NonExistingServiceException;
import net.sourceforge.fenixedu.dataTransferObject.InfoBranch;
import net.sourceforge.fenixedu.domain.Branch;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

public class InsertBranch {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Atomic
    public static void run(InfoBranch infoBranch) throws NonExistingServiceException {
        final String degreeCurricularPlanId = infoBranch.getInfoDegreeCurricularPlan().getExternalId();
        final DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);

        if (degreeCurricularPlan == null) {
            throw new NonExistingServiceException();
        }

        new Branch(infoBranch.getName(), infoBranch.getNameEn(), infoBranch.getCode(), degreeCurricularPlan);
    }

}