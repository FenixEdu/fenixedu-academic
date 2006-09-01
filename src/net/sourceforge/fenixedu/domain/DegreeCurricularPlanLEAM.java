/**
 * 
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEAMBolonhaEnrolmentRule;

/**
 * @author Ricardo Rodrigues
 *
 */

public class DegreeCurricularPlanLEAM extends DegreeCurricularPlanLET_Base {
    
    public DegreeCurricularPlanLEAM() {
        setOjbConcreteClass(getClass().getName());
    }

    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();       
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LEAMBolonhaEnrolmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }

}


