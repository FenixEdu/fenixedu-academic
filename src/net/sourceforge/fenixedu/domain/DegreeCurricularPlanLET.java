/**
 * 
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LETBolonhaEnrolmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LETOptionalExclusiveEnrollmentRule;

/**
 * @author Ricardo Rodrigues
 *
 */

public class DegreeCurricularPlanLET extends DegreeCurricularPlanLET_Base {
    
    public DegreeCurricularPlanLET() {
        setOjbConcreteClass(getClass().getName());
    }

    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();       
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LETOptionalExclusiveEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.add(new LETBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }

}


