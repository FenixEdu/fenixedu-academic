package ServidorAplicacao.Servico.equivalence;

import java.util.ArrayList;
import java.util.List;

import pt.utl.ist.berserk.logic.serviceManager.IService;
import DataBeans.InfoStudentCurricularPlan;
import DataBeans.equivalence.InfoCurricularCourseGrade;
import DataBeans.util.Cloner;
import Dominio.IDegreeCurricularPlan;
import Dominio.IStudentCurricularPlan;
import Dominio.StudentCurricularPlan;
import ServidorAplicacao.Servico.exceptions.FenixServiceException;
import ServidorAplicacao.strategy.degreeCurricularPlan.DegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.IDegreeCurricularPlanStrategyFactory;
import ServidorAplicacao.strategy.degreeCurricularPlan.strategys.IDegreeCurricularPlanStrategy;
import Util.TipoCurso;

/**
 * @author David Santos in May 12, 2004
 */

public class ValidateCurricularCourseGradesForEnrollmentEquivalence extends
        EnrollmentEquivalenceServiceUtils implements IService {
    public ValidateCurricularCourseGradesForEnrollmentEquivalence() {
    }

    public String run(Integer studentNumber, TipoCurso degreeType,
            InfoStudentCurricularPlan infoStudentCurricularPlan, List infoCurricularCourseGrades)
            throws FenixServiceException {
        List args = new ArrayList();
        args.add(0, infoStudentCurricularPlan);
        args.add(1, infoCurricularCourseGrades);

        List result1 = (List) convertDataInput(args);
        String result2 = (String) execute(result1);

        return (String) convertDataOutput(result2);
    }

    /**
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     *      This method converts this service DataBeans input objects to their
     *      respective Domain objects. These Domain objects are to be used by
     *      the service's logic.
     */
    protected Object convertDataInput(Object object) {
        List args = (List) object;

        InfoStudentCurricularPlan infoStudentCurricularPlan = (InfoStudentCurricularPlan) args.get(0);

        IDegreeCurricularPlan degreeCurricularPlan = Cloner
                .copyInfoDegreeCurricularPlan2IDegreeCurricularPlan(infoStudentCurricularPlan
                        .getInfoDegreeCurricularPlan());

        IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
        studentCurricularPlan.setDegreeCurricularPlan(degreeCurricularPlan);

        List infoCurricularCourseGrades = (List) args.get(1);

        List result = new ArrayList();
        result.add(0, studentCurricularPlan);
        result.add(1, infoCurricularCourseGrades);

        return result;
    }

    /**
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     *      This method converts this service output Domain objects to their
     *      respective DataBeans. These DataBeans are the result of executing
     *      this service logic and are to be passed on to the uper layer of the
     *      architecture.
     */
    protected Object convertDataOutput(Object object) {
        return object;
    }

    /**
     * @param List
     * @return List
     * @throws FenixServiceException
     * @see ServidorAplicacao.Servico.Service#convertDataInput(java.lang.Object)
     *      This method implements the buisiness logic of this service.
     */
    protected Object execute(Object object) throws FenixServiceException {
        List input = (List) object;

        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) input.get(0);
        List infoCurricularCourseGrades = (List) input.get(1);

        if ((infoCurricularCourseGrades != null) && (!infoCurricularCourseGrades.isEmpty())) {
            IDegreeCurricularPlan degreeCurricularPlan = studentCurricularPlan.getDegreeCurricularPlan();

            IDegreeCurricularPlanStrategyFactory degreeCurricularPlanStrategyFactory = DegreeCurricularPlanStrategyFactory
                    .getInstance();
            IDegreeCurricularPlanStrategy degreeCurricularPlanStrategy = degreeCurricularPlanStrategyFactory
                    .getDegreeCurricularPlanStrategy(degreeCurricularPlan);

            for (int i = 0; i < infoCurricularCourseGrades.size(); i++) {
                InfoCurricularCourseGrade infoCurricularCourseGrade = (InfoCurricularCourseGrade) infoCurricularCourseGrades
                        .get(i);

                if ((infoCurricularCourseGrade.getGrade() == null)
                        || (infoCurricularCourseGrade.getGrade().equals(""))) {
                    return "errors.enrollment.equivalence.must.set.all.grades";
                } else if (!degreeCurricularPlanStrategy.checkMark(infoCurricularCourseGrade.getGrade())) {
                    return "errors.enrollment.equivalence.at.least.one.invalid.grade";
                }
            }
        } else {
            return "errors.enrollment.equivalence.must.set.all.grades";
        }

        return null;
    }

}