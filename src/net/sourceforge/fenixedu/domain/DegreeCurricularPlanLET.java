/**
 * 
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.AMIIICDIIRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LETBolonhaEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LETOptionalExclusiveEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberEctsCreditsEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;

/**
 * @author Ricardo Rodrigues
 *
 */

public class DegreeCurricularPlanLET extends DegreeCurricularPlanLET_Base {
    
    public DegreeCurricularPlanLET() {
        setOjbConcreteClass(getClass().getName());
    }

    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();
	result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
	result.add(new PreviousYearsCurricularCourseEnrollmentRule(studentCurricularPlan,
		executionPeriod));
	result.add(new MaximumNumberEctsCreditsEnrolmentRule(studentCurricularPlan, executionPeriod));
        //result.add(new LETOptionalExclusiveEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new LETBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }

}


