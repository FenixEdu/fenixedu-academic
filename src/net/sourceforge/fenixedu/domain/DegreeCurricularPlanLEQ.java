package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEQBolonhaEnrolmentRule;

public class DegreeCurricularPlanLEQ extends DegreeCurricularPlanLEQ_Base {
    
    public  DegreeCurricularPlanLEQ() {
	setOjbConcreteClass(getClass().getName());
    }
    
    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LEQBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }
    
}
