package net.sourceforge.fenixedu.dataTransferObject;

import net.sourceforge.fenixedu.domain.StudentCurricularPlan;

/**
 * 
 * @author - Shezad Anavarali (shezad@ist.utl.pt)
 * 
 */
public class InfoStudentCurricularPlanWithFirstTimeEnrolment extends InfoStudentCurricularPlan {

	private Boolean firstTimeEnrolment;

    public InfoStudentCurricularPlanWithFirstTimeEnrolment(StudentCurricularPlan studentCurricularPlan) {
		super(studentCurricularPlan);
	}

    public void copyFromDomain(StudentCurricularPlan studentCurricularPlan) {
        super.copyFromDomain(studentCurricularPlan);
    }

    public static InfoStudentCurricularPlanWithFirstTimeEnrolment newInfoFromDomain(StudentCurricularPlan studentCurricularPlan) {
    	return studentCurricularPlan == null ? null : new InfoStudentCurricularPlanWithFirstTimeEnrolment(studentCurricularPlan);
    }

    public Boolean getFirstTimeEnrolment() {
        return firstTimeEnrolment;
    }

    public void setFirstTimeEnrolment(Boolean firstTimeEnrolment) {
        this.firstTimeEnrolment = firstTimeEnrolment;
    }
}