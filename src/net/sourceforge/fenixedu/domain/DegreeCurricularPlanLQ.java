package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.AMIIICDIIRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.CineticaQuimicaLQRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LQBolonhaEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberEctsCreditsEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;

public class DegreeCurricularPlanLQ extends DegreeCurricularPlanLQ_Base {
    
    public  DegreeCurricularPlanLQ() {
        super();
    }
    
    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();
	result.add(new AMIIICDIIRule(studentCurricularPlan, executionPeriod));
	result.add(new CineticaQuimicaLQRule(studentCurricularPlan, executionPeriod));
	result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
	result.add(new PreviousYearsCurricularCourseEnrollmentRule(studentCurricularPlan,
		executionPeriod));
	result.add(new MaximumNumberEctsCreditsEnrolmentRule(studentCurricularPlan, executionPeriod));
        result.add(new LQBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }

    
}
