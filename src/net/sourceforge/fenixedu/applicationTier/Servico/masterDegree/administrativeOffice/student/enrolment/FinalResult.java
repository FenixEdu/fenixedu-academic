package net.sourceforge.fenixedu.applicationTier.Servico.masterDegree.administrativeOffice.student.enrolment;

import net.sourceforge.fenixedu.dataTransferObject.InfoDegree;
import net.sourceforge.fenixedu.dataTransferObject.InfoDegreeCurricularPlan;
import net.sourceforge.fenixedu.dataTransferObject.InfoFinalResult;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.IDegree;
import net.sourceforge.fenixedu.domain.IDegreeCurricularPlan;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;
import pt.utl.ist.berserk.logic.serviceManager.IService;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class FinalResult implements IService {

    public InfoFinalResult run(InfoStudentCurricularPlan infoStudentCurricularPlan) throws Exception {

        boolean result = false;

        IStudentCurricularPlan studentCurricularPlan = InfoStudentCurricularPlan
                .newDomainFromInfo(infoStudentCurricularPlan);
        IDegreeCurricularPlan degreeCurricularPlan = InfoDegreeCurricularPlan
                .newDomainFromInfo(infoStudentCurricularPlan.getInfoDegreeCurricularPlan());
        IDegree degree = InfoDegree.newDomainFromInfo(infoStudentCurricularPlan
                .getInfoDegreeCurricularPlan().getInfoDegree());
        degreeCurricularPlan.setDegree(degree);
        studentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);

        IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
                .getInstance();

        IMasterDegreeCurricularPlanStrategy masterDegreeCurricularPlanStrategy = (IMasterDegreeCurricularPlanStrategy) degreeCurricularPlanStrategyFactory
                .getDegreeCurricularPlanStrategy(studentCurricularPlan.getDegreeCurricularPlan());

        // verify if the school part is concluded
        if (studentCurricularPlan.getDegreeCurricularPlan().getNeededCredits() == null) {
            return null;
        }

        result = masterDegreeCurricularPlanStrategy.checkEndOfScholarship(studentCurricularPlan);

        if (result == true) {
            InfoFinalResult infoFinalResult = new InfoFinalResult();

            masterDegreeCurricularPlanStrategy.calculateStudentAverage(studentCurricularPlan,
                    infoFinalResult);

            return infoFinalResult;
        }

        return null;
    }
}