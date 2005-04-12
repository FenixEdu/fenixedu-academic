/*
 * Created on Oct 11, 2004
 */
package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IEnrolment;

/**
 * @author nmgo
 * @author lmre
 */
public class InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlanAndInfoStudent extends InfoEnrolment {

    public void copyFromDomain(IEnrolment enrolment) {
        super.copyFromDomain(enrolment);
        if (enrolment != null) {
            setInfoStudentCurricularPlan(InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree
                    .newInfoFromDomain(enrolment.getStudentCurricularPlan()));
        }
    }

    public static InfoEnrolment newInfoFromDomain(IEnrolment enrolment) {
        InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlanAndInfoStudent infoEnrolment = null;
        if (enrolment != null) {
            infoEnrolment = new InfoEnrolmentWithInfoStudentPlanAndInfoDegreePlanAndInfoStudent();
            infoEnrolment.copyFromDomain(enrolment);
        }
        return infoEnrolment;
    }
}