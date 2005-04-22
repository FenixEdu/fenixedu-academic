package net.sourceforge.fenixedu.domain.precedences;

import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;

/**
 * @author David Santos in Jun 9, 2004
 */

public class RestrictionByNumberOfDoneCurricularCourses extends RestrictionByNumberOfDoneCurricularCourses_Base {
    public RestrictionByNumberOfDoneCurricularCourses() {
        super();
    }

    public CurricularCourseEnrollmentType evaluate(PrecedenceContext precedenceContext) {
        if (precedenceContext.getStudentCurricularPlan().getNumberOfApprovedCurricularCourses() >= getNumberOfCurricularCourses()
                .intValue()) {
            return CurricularCourseEnrollmentType.DEFINITIVE;
        }
        return CurricularCourseEnrollmentType.NOT_ALLOWED;

    }
}