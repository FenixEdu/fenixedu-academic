package Dominio.degree.enrollment.rules;

import java.util.List;
import Dominio.IStudentCurricularPlan;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */
public class MinimumCurricularCoursesEnrollment implements IEnrollmentRule {

    int minCoursesToBeEnrolled;

    List temporaryEnrollments;

    public MinimumCurricularCoursesEnrollment(IStudentCurricularPlan studentCurricularPlan) {

        minCoursesToBeEnrolled = studentCurricularPlan.getMinimumNumberOfCoursesToEnroll().intValue();
        temporaryEnrollments = studentCurricularPlan.getStudentTemporarilyEnrolledEnrollments();
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {

        if (minCoursesToBeEnrolled > temporaryEnrollments.size())
            return null;
        else
            return curricularCoursesToBeEnrolledIn;
    }

}