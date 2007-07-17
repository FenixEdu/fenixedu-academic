package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEBLBolonhaEnrolmentRule;

public class DegreeCurricularPlanLEBL extends DegreeCurricularPlanLEBL_Base {
    
    public  DegreeCurricularPlanLEBL() {
    }
    
    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LEBLBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }

    
}
