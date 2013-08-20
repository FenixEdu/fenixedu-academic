package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.guide;

import net.sourceforge.fenixedu.domain.DegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.ExecutionDegree;
import net.sourceforge.fenixedu.domain.Guide;
import net.sourceforge.fenixedu.domain.transactions.PaymentType;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

/**
 * 
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class EditGuideInformationInManager {

    @Checked("RolePredicates.MANAGER_PREDICATE")
    @Service
    public static void run(String guideID, String degreeCurricularPlanID, String executionYear, String newPaymentType) {
        Guide guide = AbstractDomainObject.fromExternalId(guideID);

        DegreeCurricularPlan degreeCurricularPlan = AbstractDomainObject.fromExternalId(degreeCurricularPlanID);
        ExecutionDegree cursoExecucao =
                ExecutionDegree.getByDegreeCurricularPlanAndExecutionYear(degreeCurricularPlan, executionYear);

        if (newPaymentType != null && newPaymentType.length() > 0) {
            guide.setPaymentType(PaymentType.valueOf(newPaymentType));
        }
        guide.setExecutionDegree(cursoExecucao);

    }

}