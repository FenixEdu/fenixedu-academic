package ServidorAplicacao.Servico.masterDegree.administrativeOffice.student.enrolment;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoDegree;
import DataBeans.InfoDegreeCurricularPlan;
import DataBeans.InfoFinalResult;
import DataBeans.InfoStudentCurricularPlan;
import Dominio.ICurso;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudentCurricularPlan;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IMasterDegreeCurricularPlanStrategy;

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
        ICurso degree = InfoDegree.newDomainFromInfo(infoStudentCurricularPlan
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