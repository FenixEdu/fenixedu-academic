package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.util.enrollment.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionByNumberOfDoneCurricularCourses extends RestrictionByNumberOfCurricularCourses
        implements IRestrictionByNumberOfCurricularCourses {
    public RestrictionByNumberOfDoneCurricularCourses() {
        super();
    }

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {
        if (precedenceContext.getStudentCurricularPlan().getNumberOfApprovedCurricularCourses() >= numberOfCurricularCourses
                .intValue()) {
            return CurricularCourseEnrollmentType.DEFINITIVE;
        }
        return CurricularCourseEnrollmentType.NOT_ALLOWED;

    }
}