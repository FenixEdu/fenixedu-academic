package Dominio.degree.enrollment.rules;

import java.util.List;
import Dominio.IStudentCurricularPlan;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * @author David Santos in Jun 23, 2004
 */

public class MinimumNumberOfCurricularCoursesEnrollmentRule implements
        IEnrollmentRule {
    private int minCoursesToBeEnrolled;

    public MinimumNumberOfCurricularCoursesEnrollmentRule(
            IStudentCurricularPlan studentCurricularPlan) {
        minCoursesToBeEnrolled = studentCurricularPlan
                .getMinimumNumberOfCoursesToEnroll().intValue();
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {
        if (minCoursesToBeEnrolled > curricularCoursesToBeEnrolledIn.size()) {
            return null;
        }
        return curricularCoursesToBeEnrolledIn;

    }
}