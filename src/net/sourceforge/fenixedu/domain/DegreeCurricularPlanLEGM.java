package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEGMBolonhaEnrolmentRule;

public class DegreeCurricularPlanLEGM extends DegreeCurricularPlanLEGM_Base {
    
    public  DegreeCurricularPlanLEGM() {
	setOjbConcreteClass(getClass().getName());
    }
    
    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();       
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LEGMBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }

    
}
