package net.sourceforge.fenixedu.domain;

public class CreditsInScientificAreaTest extends DomainTestBase {

	private ICreditsInScientificArea credits;

	private void setUpDelete() {
		credits = new CreditsInScientificArea();
		
		IScientificArea scientificArea = new ScientificArea();
		IEnrolment enrolment = new Enrolment();
		IStudentCurricularPlan studentCurricularPlan = new StudentCurricularPlan();
		
		credits.setScientificArea(scientificArea);
		credits.setEnrolment(enrolment);
		credits.setStudentCurricularPlan(studentCurricularPlan);
	}
	
	public void testDelete() {
		
		setUpDelete();
		
		credits.delete();
		
		assertFalse("Failed to dereference ScientificArea", credits.hasScientificArea());
		assertFalse("Failed to dereference Enrolment", credits.hasEnrolment());
		assertFalse("Failed to dereference StudentCurricularPlan", credits.hasStudentCurricularPlan());
	}
}
