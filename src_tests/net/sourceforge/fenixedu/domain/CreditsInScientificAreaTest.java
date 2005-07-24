package net.sourceforge.fenixedu.domain;

public class CreditsInScientificAreaTest extends DomainTestBase {

	private ICreditsInScientificArea credits;
	
	protected void setUp() throws Exception {
        super.setUp();
		
		credits = new CreditsInScientificArea();
		
		IScientificArea scientificArea = new ScientificArea();
		IEnrolment enrolment = new Enrolment();
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();

		credits.setScientificArea(scientificArea);
		credits.setEnrolment(enrolment);
		credits.setStudentCurricularPlan(studentCurricularPlan);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }
	
	public void testDeleteRestriction() {
		
		credits.delete();
		
		assertFalse(credits.hasScientificArea());
		assertFalse(credits.hasEnrolment());
		assertFalse(credits.hasStudentCurricularPlan());

	}
}
