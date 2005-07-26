package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IEnrolment;

public class InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegree extends
        InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationType {

    public void copyFromDomain(IEnrolment enrolment) {
        super.copyFromDomain(enrolment);
        setInfoCurricularCourse(InfoCurricularCourseWithInfoDegree.newInfoFromDomain(enrolment.getCurricularCourse()));
    }

    public static InfoEnrolment newInfoFromDomain(IEnrolment enrolment) {
        InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegree infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithExecutionPeriodAndYearAndEvaluationTypeAndCurricularCourseAndDegreeCurricularPlanAndDegree();
            infoEnrolment.copyFromDomain(enrolment);
        }

        return infoEnrolment;
    }
}
