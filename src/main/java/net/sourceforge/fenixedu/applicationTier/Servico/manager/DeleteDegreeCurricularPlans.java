/*
 * Created on 31/Jul/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.manager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * @author lmac1
 */

public class DeleteDegreeCurricularPlans {

    @Checked("RolePredicates.MANAGER_OR_OPERATOR_PREDICATE")
    @Service
    public static List run(List degreeCurricularPlansIds) throws FenixServiceException {
        Iterator<Integer> iter = degreeCurricularPlansIds.iterator();

        List<String> undeletedDegreeCurricularPlansNames = new ArrayList<String>();

        while (iter.hasNext()) {

            Integer degreeCurricularPlanId = iter.next();
            DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanId);

            try {
                degreeCurricularPlan.delete();
            } catch (DomainException e) {
                undeletedDegreeCurricularPlansNames.add(degreeCurricularPlan.getName());
            }
        }

        return undeletedDegreeCurricularPlansNames;
    }
}