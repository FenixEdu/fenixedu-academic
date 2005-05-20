package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.IStudentCurricularPlan;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InfoStudentCurricularPlanWithFirstTimeEnrolment extends
        InfoStudentCurricularPlanWithInfoStudentWithPersonAndDegree {

    private Boolean firstTimeEnrolment;

    public void copyFromDomain(IStudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
    }

    public static InfoStudentCurricularPlanWithFirstTimeEnrolment newInfoFromDomain(
            IStudentCurricularPlan studentCurricularPlan) {
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