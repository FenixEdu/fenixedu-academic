package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfAcumulatedEnrollmentsRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberOfCurricularCoursesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRuleIgnoringLastYears;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.SpecificLEFTEnrolmentRule;

public class DegreeCurricularPlanLEFT extends DegreeCurricularPlan {
	
    public DegreeCurricularPlanLEFT() {
        setOjbConcreteClass(getClass().getName());
    }

    public List getListOfEnrollmentRules(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {

        List result = new ArrayList();
        result.add(new MaximumNumberOfAcumulatedEnrollmentsRule(studentCurricularPlan, executionPeriod));
        result.add(new MaximumNumberOfCurricularCoursesEnrollmentRule(studentCurricularPlan,
                executionPeriod));
        result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new PreviousYearsCurricularCourseEnrollmentRuleIgnoringLastYears(
                studentCurricularPlan, executionPeriod, 4));
        result.add(new SpecificLEFTEnrolmentRule(studentCurricularPlan, executionPeriod));

        return result;
    }
}
