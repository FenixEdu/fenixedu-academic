package net.sourceforge.fenixedu.domain.degree.enrollment.rules;

import java.util.List;

import net.sourceforge.fenixedu.domain.IExecutionPeriod;
import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;

/**
 * @author João Mota
 */

public class LECEvenAndOddNumbersEnrollmentRule implements IEnrollmentRule {
    private float studentNumber;

    private Integer oddCourseId = new Integer(2633);

    private Integer evenCourseId = new Integer(2720);

    private IExecutionPeriod executionPeriod;

    public LECEvenAndOddNumbersEnrollmentRule(IStudentCurricularPlan studentCurricularPlan,
            IExecutionPeriod executionPeriod) {
        studentNumber = studentCurricularPlan.getStudent().getNumber().floatValue();
        this.executionPeriod = executionPeriod;
    }

    public List apply(List curricularCoursesToBeEnrolledIn) {
        CurricularCourse2Enroll oddCourse = (CurricularCourse2Enroll) CollectionUtils.find(
                curricularCoursesToBeEnrolledIn, curricularCoursePredicate(oddCourseId));
        CurricularCourse2Enroll evenCourse = (CurricularCourse2Enroll) CollectionUtils.find(
                curricularCoursesToBeEnrolledIn, curricularCoursePredicate(evenCourseId));
        if ((studentNumber / 2) - Math.floor(studentNumber / 2) > 0) {
            if (executionPeriod.getSemester().intValue() == 1) {
                curricularCoursesToBeEnrolledIn.remove(oddCourse);
            } else {
                curricularCoursesToBeEnrolledIn.remove(evenCourse);
            }
        } else {
            if (executionPeriod.getSemester().intValue() == 1) {
                curricularCoursesToBeEnrolledIn.remove(evenCourse);
            } else {
                curricularCoursesToBeEnrolledIn.remove(oddCourse);
            }
        }
        return curricularCoursesToBeEnrolledIn;

    }

    /**
     * @return
     */
    private Predicate curricularCoursePredicate(final Integer idInternal) {
        return new Predicate() {

            public boolean evaluate(Object arg0) {
                CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) arg0;
                return curricularCourse2Enroll.getCurricularCourse().getIdInternal().equals(idInternal);
            }
        };
    }
}