package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;
import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class EditGuideInformationInManager {

    @Atomic
    public static void run(String guideID, String degreeCurricularPlanID, String executionYear, String newPaymentType) {
        check(RolePredicates.MANAGER_PREDICATE);
        Guide guide = FenixFramework.getDomainObject(guideID);

        DegreeCurricularPlan degreeCurricularPlan = FenixFramework.getDomainObject(degreeCurricularPlanID);
        ExecutionDegree cursoExecucao =
                ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);

        if (newPaymentType != null && newPaymentType.length() > 0) {
            guide.setPaymentType(PaymentType.valueOf(newPaymentType));
        }
        guide.setExecutionDegree(cursoExecucao);

    }

}