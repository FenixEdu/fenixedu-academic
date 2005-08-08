package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LEAOptionalGroupsEnrollmentRule;

/**
 * @author Ricardo Rodrigues
 * 
 */

public class DegreeCurricularPlanLEA extends DegreeCurricularPlanLEA_Base {

    public DegreeCurricularPlanLEA() {
        setOjbConcreteClass(getClass().getName());
    }

    public List getListOfEnrollmentRules(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {

        List result = new ArrayList();
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LEAOptionalGroupsEnrollmentRule(studentCurricularPlan, executionPeriod));
        return result;
    }

}
