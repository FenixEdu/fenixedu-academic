package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.IEnrolment;
import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author David Santos in Jun 22, 2004
 */

public class MaximumNumberOfAcumulatedEnrollmentsRule implements IEnrollmentRule {
    private IStudentCurricularPlan studentCurricularPlan;

    private IExecutionPeriod executionPeriod;

    public MaximumNumberOfAcumulatedEnrollmentsRule(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {
        this.studentCurricularPlan = studentCurricularPlan;
        this.executionPeriod = executionPeriod;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {
        List curricularCoursesToRemove = new ArrayList();
        List allStudentEnrolledEnrollments = this.studentCurricularPlan
                .getAllStudentEnrolledEnrollmentsInExecutionPeriod(this.executionPeriod);

        int totalNAC = 0;
        int size = allStudentEnrolledEnrollments.size();

        for (int i = 0; i < size; i++) {
            IEnrolment enrollment = (IEnrolment) allStudentEnrolledEnrollments.get(i);
            totalNAC += enrollment.getAccumulatedWeight().intValue();
        }

        int maxNAC = this.studentCurricularPlan.getMaximumNumberOfAcumulatedEnrollments().intValue();

        if (totalNAC >= maxNAC) {

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

        int availableNACToEnroll = maxNAC - totalNAC;

        size = curricularCoursesToBeEnrolledIn.size();
        for (int i = 0; i < size; i++) {
            CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) curricularCoursesToBeEnrolledIn
                    .get(i);
            int ac = this.studentCurricularPlan.getCurricularCourseAcumulatedEnrollments(
                    curricularCourse2Enroll.getCurricularCourse()).intValue();
            if (ac > availableNACToEnroll) {
                curricularCoursesToRemove.add(curricularCourse2Enroll);
            }
        }

        curricularCoursesToBeEnrolledIn.removeAll(curricularCoursesToRemove);

        return curricularCoursesToBeEnrolledIn;
    }
}