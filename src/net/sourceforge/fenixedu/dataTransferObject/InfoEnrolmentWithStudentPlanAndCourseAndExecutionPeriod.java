package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IEnrolment;

/**
 * @author Fernanda Quitério 20/Jul/2004
 */
public class InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod extends
        InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear {

    public void copyFromDomain(IEnrolment enrolment) {
        super.copyFromDomain(enrolment);
        if (enrolment != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                    .newInfoFromDomain(enrolment.getStudentCurricularPlan()));
        }
    }

    public static InfoEnrolment newInfoFromDomain(IEnrolment enrolment) {
        InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod();
            infoEnrolment.copyFromDomain(enrolment);
        }
        return infoEnrolment;
    }
}