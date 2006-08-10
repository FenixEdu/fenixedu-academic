package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEANBolonhaEnrolmentRule;

public class DegreeCurricularPlanLEAN extends DegreeCurricularPlanLEAN_Base {
    
    public  DegreeCurricularPlanLEAN() {
	setOjbConcreteClass(getClass().getName());
    }
    
    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LEANBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }

    
}
