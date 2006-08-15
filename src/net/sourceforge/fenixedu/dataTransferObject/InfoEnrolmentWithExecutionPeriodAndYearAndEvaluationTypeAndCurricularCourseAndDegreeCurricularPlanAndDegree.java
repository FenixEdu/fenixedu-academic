package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Enrolment;

public class InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegree extends
        InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationType {

    public void copyFromDomain(Enrolment enrolment) {
        super.copyFromDomain(enrolment);
        setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(enrolment.getCurricularCourse()));
    }

    public static InfoEnrolment newInfoFromDomain(Enrolment enrolment) {
        InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegree infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegree();
            infoEnrolment.copyFromDomain(enrolment);
        }

        return infoEnrolment;
    }
}
