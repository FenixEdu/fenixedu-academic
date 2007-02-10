package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.AMIIICDIIRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.DireitoEmpresasRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEGIBolonhaEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.MaximumNumberEctsCreditsEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PrecedencesEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.PreviousYearsCurricularCourseEnrollmentRule;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DegreeCurricularPlanLEGI extends DegreeCurricularPlanLEGI_Base {

    public DegreeCurricularPlanLEGI() {
        setOjbConcreteClass(getClass().getName());
    }

    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();
	result.add(new AMIIICDIIRule(studentCurricularPlan, executionPeriod));
	result.add(new DireitoEmpresasRule(studentCurricularPlan, executionPeriod));
	result.add(new PrecedencesEnrollmentRule(studentCurricularPlan, executionPeriod));
	result.add(new PreviousYearsCurricularCourseEnrollmentRule(studentCurricularPlan,
		executionPeriod));
	result.add(new MaximumNumberEctsCreditsEnrolmentRule(studentCurricularPlan, executionPeriod));
        result.add(new LEGIBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        
        return result;
    }

}
