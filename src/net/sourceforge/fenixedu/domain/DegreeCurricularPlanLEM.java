package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEMIgnorePrecedenceForBranchTAEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEMOptionalPairGroupsEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfAcumulatedEnrollmentsRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfCurricularCoursesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRuleIgnoringLastYears;

/**
 * @author Jo�o Mota
 */

public class DegreeCurricularPlanLEM extends DegreeCurricularPlanLEM_Base implements IDegreeCurricularPlan {

    public DegreeCurricularPlanLEM() {
        setOjbConcreteClass(getClass().getName());
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