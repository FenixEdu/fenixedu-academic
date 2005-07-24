package net.sourceforge.fenixedu.domain;


public class CreditsInAnySecundaryAreaTest extends DomainTestBase {

	private ICreditsInAnySecundaryArea credits;
	
	protected void setUp() throws Exception {
        super.setUp();
		
		credits = new CreditsInAnySecundaryArea();
		
		IEnrolment enrolment = new Enrolment();
		
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
		
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
