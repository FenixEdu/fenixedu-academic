package Dominio;

import java.util.ArrayList;
import java.util.List;

import Dominio.degree.enrollment.rules.LECEvenAndOddNumbersEnrollmentRule;

/**
 * @author David Santos in Jun 25, 2004
 */

public class DegreeCurricularPlanLEC extends DegreeCurricularPlan implements IDegreeCurricularPlan {

    public DegreeCurricularPlanLEC() {
        ojbConcreteClass = getClass().getName();
    }

    public List getListOfEnrollmentRules(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {

        List result = new ArrayList();
        result.add(new LECEvenAndOddNumbersEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        return result;
    }

}