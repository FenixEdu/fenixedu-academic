package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.AMIIICDIIRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEMAUBranchPSIRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEMBolonhaEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEMOptionalPairGroupsEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberEctsCreditsEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRuleIgnoringLastYears;

/**
 * @author Jo�o Mota
 */

public class DegreeCurricularPlanLEM extends DegreeCurricularPlanLEM_Base {

    public DegreeCurricularPlanLEM() {
        setOjbConcreteClass(getClass().getName());
    }

    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();

        result.add(new AMIIICDIIRule(studentCurricularPlan, executionPeriod));
        result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new LEMOptionalPairGroupsEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new PreviousYearsCurricularCourseEnrollmentRuleIgnoringLastYears(
                studentCurricularPlan, executionPeriod, 4));        
        result.add(new MaximumNumberEctsCreditsEnrolmentRule(studentCurricularPlan, executionPeriod));
        result.add(new LEMBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }

}