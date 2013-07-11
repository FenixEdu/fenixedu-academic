package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment;


import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;
import net.sourceforge.fenixedu.dataTransferObject.InfoFinalResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class FinalResult {

    @Checked("RolePredicates.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_PREDICATE")
    @Service
    public static InfoFinalResult run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws Exception {

        boolean result = false;

        StudentCurricularPlan studentCurricularPlan =
                FenixFramework.getDomainObject(infoStudentCurricularPlan.getExternalId());

        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory =
                DegreeCurricularPlanStrategyFactory.getInstance();

        IMasterDegreeCurricularPlanStrategy masterDegreeCurricularPlanStrategy =
                (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory
                        .getDegreeCurricularPlanStrategy(studentCurricularPlan.getDegreeCurricularPlan());

        // verify if the school part is concluded
        if (studentCurricularPlan.getDegreeCurricularPlan().getNeededCredits() == null) {
            return null;
        }

        result = masterDegreeCurricularPlanStrategy.checkEndOfScholarship(studentCurricularPlan);

        if (result == true) {
            InfoFinalResult infoFinalResult = new InfoFinalResult();

            masterDegreeCurricularPlanStrategy.calculateStudentAverage(studentCurricularPlan, infoFinalResult);

            return infoFinalResult;
        }

        return null;
    }
}