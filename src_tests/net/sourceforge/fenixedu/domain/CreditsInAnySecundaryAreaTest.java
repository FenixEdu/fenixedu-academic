package net.sourceforge.fenixedu.domain;


public class CreditsInAnySecundaryAreaTest extends DomainTestBase {

	private CreditsInAnySecundaryArea credits;
	
	private void setUpDelete() {
		credits = new CreditsInAnySecundaryArea();
		
		Enrolment enrolment = new Enrolment();
		
		StudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
		
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
