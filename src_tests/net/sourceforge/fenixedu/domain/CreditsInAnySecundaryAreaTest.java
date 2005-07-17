package net.sourceforge.fenixedu.domain;


public class CreditsInAnySecundaryAreaTest extends DomainTestBase {

	private ICreditsInAnySecundaryArea credits;
	
	protected void setUp() throws Exception {
        super.setUp();
		
		credits = new CreditsInAnySecundaryArea();
		credits.setIdInternal(1);
		
		IEnrolment enrolment = new Enrolment();
		enrolment.setIdInternal(1);
		
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
		studentCurricularPlan.setIdInternal(1);
		
		credits.setEnrolment(enrolment);
		credits.setStudentCurricularPlan(studentCurricularPlan);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testDeleteRestriction() {
		
		credits.delete();
		
		assertFalse(credits.hasEnrolment());
		assertFalse(credits.hasStudentCurricularPlan());

	}
}
