package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * @author David Santos in Jun 23, 2004
 */

public class MinimumNumberOfCurricularCoursesEnrollmentRule implements IEnrollmentRule {
    private int minCoursesToBeEnrolled;

    public MinimumNumberOfCurricularCoursesEnrollmentRule(StudentCurricularPlan studentCurricularPlan) {
        minCoursesToBeEnrolled = studentCurricularPlan.getMinimumNumberOfCoursesToEnroll().intValue();
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {
        if (minCoursesToBeEnrolled > curricularCoursesToBeEnrolledIn.size()) {
            return null;
        }
        return curricularCoursesToBeEnrolledIn;

    }
}