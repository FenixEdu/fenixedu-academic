package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.CurricularCourse;
import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 * @author David Santos in Jun 23, 2004
 */

public class MandatoryCurricularCoursesEnrollmentRule implements IEnrollmentRule {
    public MandatoryCurricularCoursesEnrollmentRule(StudentCurricularPlan studentCurricularPlan) {
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {
        List mandatoryCurricularCourses = new ArrayList();

        for (int iter = 0; iter < curricularCoursesToBeEnrolledIn.size(); iter++) {
            CurricularCourse curricularCourse = (CurricularCourse) curricularCoursesToBeEnrolledIn
                    .get(iter);
            if (curricularCourse.getMandatoryEnrollment().booleanValue()) {
                mandatoryCurricularCourses.add(curricularCourse);
            }
        }

        return mandatoryCurricularCourses;
    }

}