package Dominio.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

import Dominio.IEnrollment;
import Dominio.IExecutionPeriod;
import Dominio.IStudentCurricularPlan;
import Dominio.degree.enrollment.CurricularCourse2Enroll;

/**
 * @author David Santos in Jun 23, 2004
 */

public class MaximumNumberOfCurricularCoursesEnrollmentRule implements IEnrollmentRule {
    private IStudentCurricularPlan studentCurricularPlan;

    private IExecutionPeriod executionPeriod;

    public MaximumNumberOfCurricularCoursesEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionPeriod = executionPeriod;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {
        List curricularCoursesToRemove = new ArrayList();
        List allStudentEnrolledEnrollments = this.studentCurricularPlan
                .getAllStudentEnrolledEnrollmentsInExecutionPeriod(this.executionPeriod);

        int numberOfEnrolledCurricularCourses = 0;
        int size = allStudentEnrolledEnrollments.size();

        for (int i = 0; i < size; i++) {
            IEnrollment enrollment = (IEnrollment) allStudentEnrolledEnrollments.get(i);
            numberOfEnrolledCurricularCourses += enrollment.getCurricularCourse().getEnrollmentWeigth()
                    .intValue();
        }

        int maxEnrolledCurricularCourses = this.studentCurricularPlan
                .getMaximumNumberOfCoursesToEnroll().intValue();

        if (numberOfEnrolledCurricularCourses >= maxEnrolledCurricularCourses) {

            List result = (List) CollectionUtils.select(curricularCoursesToBeEnrolledIn,
                    new Predicate() {
                        public boolean evaluate(Object obj) {
                            CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                            return curricularCourse2Enroll.getCurricularCourse().getEnrollmentWeigth()
                                    .intValue() == 0;
                        }
                    });

            if (result.isEmpty()) {
                return new ArrayList();
            }
        }

        int availableCurricularCoursesToEnroll = maxEnrolledCurricularCourses
                - numberOfEnrolledCurricularCourses;

        size = curricularCoursesToBeEnrolledIn.size();
        for (int i = 0; i < size; i++) {
            CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) curricularCoursesToBeEnrolledIn
                    .get(i);
            int ew = curricularCourse2Enroll.getCurricularCourse().getEnrollmentWeigth().intValue();
            if (ew > availableCurricularCoursesToEnroll) {
                curricularCoursesToRemove.add(curricularCourse2Enroll);
            }
        }

        curricularCoursesToBeEnrolledIn.removeAll(curricularCoursesToRemove);

        return curricularCoursesToBeEnrolledIn;
    }
}