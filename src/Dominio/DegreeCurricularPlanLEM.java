package Dominio;

import java.util.ArrayList;
import java.util.List;

import Dominio.degree.enrollment.rules.LEMIgnorePrecedenceForBranchTAEnrollmentRule;
import Dominio.degree.enrollment.rules.LEMOptionalPairGroupsEnrollmentRule;
import Dominio.degree.enrollment.rules.MaximumNumberOfAcumulatedEnrollmentsRule;
import Dominio.degree.enrollment.rules.MaximumNumberOfCurricularCoursesEnrollmentRule;
import Dominio.degree.enrollment.rules.PrecedencesEnrollmentRule;
import Dominio.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRuleIgnoringLastYears;

/**
 * @author João Mota
 */

public class DegreeCurricularPlanLEM extends DegreeCurricularPlan implements IDegreeCurricularPlan {

    public DegreeCurricularPlanLEM() {
        ojbConcreteClass = getClass().getName();
    }

    public List getListOfEnrollmentRules(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {

        List result = new ArrayList();

        result.add(new MaximumNumberOfAcumulatedEnrollmentsRule(studentCurricularPlan, executionPeriod));
        result.add(new MaximumNumberOfCurricularCoursesEnrollmentRule(studentCurricularPlan,
                executionPeriod));
        result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new LEMIgnorePrecedenceForBranchTAEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new LEMOptionalPairGroupsEnrollmentRule(studentCurricularPlan));
        result.add(new PreviousYearsCurricularCourseEnrollmentRuleIgnoringLastYears(
                studentCurricularPlan, executionPeriod, 4));        
        return result;
    }

}