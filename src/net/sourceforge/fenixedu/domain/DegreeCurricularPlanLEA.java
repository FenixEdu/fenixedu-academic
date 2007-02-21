package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.AMIIICDIIRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.HelicopterosLEARule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEABolonhaEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberEctsCreditsEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DegreeCurricularPlanLEA extends DegreeCurricularPlanLEA_Base {

    public DegreeCurricularPlanLEA() {
        setOjbConcreteClass(getClass().getName());
    }

    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();
	result.add(new AMIIICDIIRule(studentCurricularPlan, executionPeriod));
	result.add(new HelicopterosLEARule(studentCurricularPlan));
	result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
	result.add(new PreviousYearsCurricularCourseEnrollmentRule(studentCurricularPlan,
		executionPeriod));
	result.add(new MaximumNumberEctsCreditsEnrolmentRule(studentCurricularPlan, executionPeriod));
        /*result.add(new LEAOptionalGroupsEnrollmentRule(studentCurricularPlan, executionPeriod));*/
        result.add(new LEABolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        
        return result;
    }

}
