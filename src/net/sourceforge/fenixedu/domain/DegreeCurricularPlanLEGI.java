package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEGIBolonhaEnrolmentRule;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DegreeCurricularPlanLEGI extends DegreeCurricularPlanLEA_Base {

    public DegreeCurricularPlanLEGI() {
        setOjbConcreteClass(getClass().getName());
    }

    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LEGIBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        
        return result;
    }

}
