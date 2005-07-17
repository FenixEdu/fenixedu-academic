package net.sourceforge.fenixedu.domain;

/**
 * @author David Santos Jan 14, 2004
 */

public class CreditsInAnySecundaryArea extends CreditsInAnySecundaryArea_Base {

    public String toString() {
        String result = "student: ["
                + this.getStudentCurricularPlan().getStudent().getNumber().toString();
        result += "] course: [" + this.getEnrolment().getCurricularCourse().getName() + "]";
        return result;
    }
	
	
	public void delete() {
		removeEnrolment();
		removeStudentCurricularPlan();
		super.deleteDomainObject();
	}
}
