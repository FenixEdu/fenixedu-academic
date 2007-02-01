package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LECEvenAndOddNumbersEnrollmentRule;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.LECOptionalPairGroupsEnrollmentRule;

/**
 * @author David Santos in Jun 25, 2004
 */

public class DegreeCurricularPlanLEC extends DegreeCurricularPlanLEC_Base {

    public DegreeCurricularPlanLEC() {
        setOjbConcreteClass(getClass().getName());
    }

    public List getListOfEnrollmentRules(StudentCurricularPlan studentCurricularPlan,
            ExecutionPeriod executionPeriod) {

        List result = new ArrayList();
        result.add(new LECEvenAndOddNumbersEnrollmentRule(studentCurricularPlan, executionPeriod));
        result.addAll(super.getListOfEnrollmentRules(studentCurricularPlan, executionPeriod));
        result.add(new LECOptionalPairGroupsEnrollmentRule(studentCurricularPlan));
        return result;
    }

}