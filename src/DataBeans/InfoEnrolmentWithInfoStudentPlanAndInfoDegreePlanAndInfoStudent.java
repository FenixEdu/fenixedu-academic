/*
 * Created on Oct 11, 2004
 */
package DataBeans;

import Dominio.IEnrollment;

/**
 * @author nmgo
 * @author lmre
 */
public class InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlanAndInfoStudent extends InfoEnrolment {

    public void copyFromDomain(IEnrollment enrolment) {
        super.copyFromDomain(enrolment);
        if (enrolment != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                    .newInfoFromDomain(enrolment.getStudentCurricularPlan()));
        }
    }

    public static InfoEnrolment newInfoFromDomain(IEnrollment enrolment) {
        InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlanAndInfoStudent infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlanAndInfoStudent();
            infoEnrolment.copyFromDomain(enrolment);
        }
        return infoEnrolment;
    }
}