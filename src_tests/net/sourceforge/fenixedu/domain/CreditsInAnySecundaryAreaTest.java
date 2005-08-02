package net.sourceforge.fenixedu.domain;


public class CreditsInAnySecundaryAreaTest extends DomainTestBase {

	private ICreditsInAnySecundaryArea credits;
	
	private void setUpDelete() {
		credits = new CreditsInAnySecundaryArea();
		
		IEnrolment enrolment = new Enrolment();
		
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
		
		credits.setEnrolment(enrolment);
		credits.setStudentCurricularPlan(studentCurricularPlan);
	}

    public void testDelete() {
		
		setUpDelete();
		
		credits.delete();
		
		assertFalse("Failed to dereference Enrolment", credits.hasEnrolment());
		assertFalse("Failed to dereference StudentCurricularPlan", credits.hasStudentCurricularPlan());
	}
}
