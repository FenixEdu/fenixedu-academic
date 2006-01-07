package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InfoStudentCurricularPlanWithFirstTimeEnrolment extends
        InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree {

    private Boolean firstTimeEnrolment;

    public void copyFromDomain(StudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
    }

    public static InfoStudentCurricularPlanWithFirstTimeEnrolment newInfoFromDomain(
            StudentCurricularPlan studentCurricularPlan) {
        InfoStudentCurricularPlanWithFirstTimeEnrolment infoStudentCurricularPlan = null;
        if (studentCurricularPlan != null) {
            infoStudentCurricularPlan = new InfoStudentCurricularPlanWithFirstTimeEnrolment();
            infoStudentCurricularPlan.copyFromDomain(studentCurricularPlan);
        }
        return infoStudentCurricularPlan;
    }

    public Boolean getFirstTimeEnrolment() {
        return firstTimeEnrolment;
    }

    public void setFirstTimeEnrolment(Boolean firstTimeEnrolment) {
        this.firstTimeEnrolment = firstTimeEnrolment;
    }
}