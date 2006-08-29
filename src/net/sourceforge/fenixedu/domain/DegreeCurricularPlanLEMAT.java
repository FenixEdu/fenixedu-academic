package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEMATBolonhaEnrolmentRule;

public class DegreeCurricularPlanLEMAT extends DegreeCurricularPlanLEGM_Base {
    
    public  DegreeCurricularPlanLEMAT() {
	setOjbConcreteClass(getClass().getName());
    }
    
    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();       
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LEMATBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }

    
}
