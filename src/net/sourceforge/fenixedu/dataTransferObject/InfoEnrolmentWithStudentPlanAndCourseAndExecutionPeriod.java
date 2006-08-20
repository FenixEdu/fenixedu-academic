package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.Enrolment;

/**
 * @author Fernanda Quitério 20/Jul/2004
 */
public class InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod extends
        InfoEnrolmentWithCourseAndDegreeAndExecutionPeriodAndYear {

    public void copyFromDomain(Enrolment enrolment) {
        super.copyFromDomain(enrolment);
        if (enrolment != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlan
                    .newInfoFromDomain(enrolment.getStudentCurricularPlan()));
        }
    }

    public static InfoEnrolment newInfoFromDomain(Enrolment enrolment) {
        InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithStudentPlanAndCourseAndExecutionPeriod();
            infoEnrolment.copyFromDomain(enrolment);
        }
        return infoEnrolment;
    }
}