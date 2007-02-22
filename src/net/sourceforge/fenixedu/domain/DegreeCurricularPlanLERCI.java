package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LERCIBolonhaEnrolmentRule;

public class DegreeCurricularPlanLERCI extends DegreeCurricularPlanLERCI_Base {
    
    public  DegreeCurricularPlanLERCI() {
        super();
    }
    
    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LERCIBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }
}
