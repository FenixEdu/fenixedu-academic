package net.sourceforge.fenixedu.domain.precedences;

import java.util.List;

import net.sourceforge.fenixedu.domain.ICurricularCourse;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse extends RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse_Base {
    public RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse() {
        super();
    }

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {
        ICurricularCourse curricularCourse = this.getPrecedentCurricularCourse();
        CurricularCourseEnrollmentType result1 = null;
        CurricularCourseEnrollmentType result2 = null;

        List curricularCoursesWhereStudentCanBeEnrolled = (List) CollectionUtils.collect(
                precedenceContext.getCurricularCourses2Enroll(), new Transformer() {
                    public Object transform(Object obj) {
                        CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) obj;
                        return curricularCourse2Enroll.getCurricularCourse();
                    }
                });

        if (curricularCoursesWhereStudentCanBeEnrolled.contains(curricularCourse)) {
            result1 = CurricularCourseEnrollmentType.DEFINITIVE;
        } else {
            result1 = CurricularCourseEnrollmentType.NOT_ALLOWED;
        }

        result2 = super.evaluate(precedenceContext);

        return result1.or(result2);
    }
}