package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEBMBolonhaEnrolmentRule;

public class DegreeCurricularPlanLEBM extends DegreeCurricularPlanLEBM_Base {
    
    public  DegreeCurricularPlanLEBM() {
	setOjbConcreteClass(getClass().getName());
    }
    
    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LEBMBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }

    
}
