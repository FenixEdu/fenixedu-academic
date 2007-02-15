package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LQBolonhaEnrolmentRule;

public class DegreeCurricularPlanLQ extends DegreeCurricularPlanLQ_Base {
    
    public  DegreeCurricularPlanLQ() {
        super();
    }
    
    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LQBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }

    
}
