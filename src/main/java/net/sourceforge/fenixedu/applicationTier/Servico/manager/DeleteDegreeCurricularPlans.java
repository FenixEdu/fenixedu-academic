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
import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author lmac1
 */

public class DeleteDegreeCurricularPlans {

    @Atomic
    public static List run(List<String> degreeCurricularPlansIds) throws FenixServiceException {
        check(RolePredicates.MANAGER_OR_OPERATOR_PREDICATE);
        Iterator<String> iter = degreeCurricularPlansIds.iterator();

        List<String> undeletedDegreeCurricularPlansNames = new ArrayList<String>();

        while (iter.hasNext()) {

            String degreeCurricularPlanId = iter.next();
            DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanId);

            try {
                degreeCurricularPlan.delete();
            } catch (DomainException e) {
                undeletedDegreeCurricularPlansNames.add(degreeCurricularPlan.getName());
            }
        }

        return undeletedDegreeCurricularPlansNames;
    }
}