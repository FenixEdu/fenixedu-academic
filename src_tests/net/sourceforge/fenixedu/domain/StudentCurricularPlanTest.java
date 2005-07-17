package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.degree.enrollment.INotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public class StudentCurricularPlanTest extends DomainTestBase {

	private IStudentCurricularPlan studentCurricularPlanToDelete = null;
	private IStudentCurricularPlanLEIC studentCurricularPlanLEICToDelete = null;
	private IStudentCurricularPlanLEEC studentCurricularPlanLEECToDelete = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		studentCurricularPlanToDelete = new StudentCurricularPlan();
		studentCurricularPlanToDelete.setIdInternal(1);

		studentCurricularPlanLEICToDelete = new StudentCurricularPlanLEIC();
		studentCurricularPlanLEICToDelete.setIdInternal(2);
		
		studentCurricularPlanLEECToDelete = new StudentCurricularPlanLEEC();
		studentCurricularPlanLEECToDelete.setIdInternal(3);
		
		IBranch br1 = new Branch();
		br1.setIdInternal(1);
		studentCurricularPlanToDelete.setBranch(br1);
		
		studentCurricularPlanLEICToDelete.setSecundaryBranch(br1);
		studentCurricularPlanLEECToDelete.setSecundaryBranch(br1);
		
		IEnrolment e1 = new Enrolment();
		e1.setIdInternal(1);
		studentCurricularPlanToDelete.addEnrolments(e1);
		
		IStudent s1 = new Student();
		s1.setIdInternal(1);
		studentCurricularPlanToDelete.setStudent(s1);
		
		IDegreeCurricularPlan dcp1 = new DegreeCurricularPlan();
		dcp1.setIdInternal(1);
		studentCurricularPlanToDelete.setDegreeCurricularPlan(dcp1);
		
		IEmployee emp1 = new Employee();
		emp1.setIdInternal(1);
		studentCurricularPlanToDelete.setEmployee(emp1);
		
		IGratuitySituation gs1 = new GratuitySituation();
		gs1.setIdInternal(1);
		gs1.setStudentCurricularPlan(studentCurricularPlanToDelete);
		
		IMasterDegreeThesis mdt1 = new MasterDegreeThesis();
		mdt1.setIdInternal(1);
		studentCurricularPlanToDelete.setMasterDegreeThesis(mdt1);
		
		INotNeedToEnrollInCurricularCourse notNeed1 = new NotNeedToEnrollInCurricularCourse();
		notNeed1.setIdInternal(1);
		notNeed1.setStudentCurricularPlan(studentCurricularPlanToDelete);
		
		ICreditsInAnySecundaryArea cred1 = new CreditsInAnySecundaryArea();
		cred1.setIdInternal(1);
		cred1.setStudentCurricularPlan(studentCurricularPlanToDelete);
		
		ICreditsInScientificArea cred2 = new CreditsInScientificArea();
		cred2.setIdInternal(2);
		cred2.setStudentCurricularPlan(studentCurricularPlanToDelete);
	}
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testDelete() {
		try {
			studentCurricularPlanToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse(studentCurricularPlanToDelete.hasDegreeCurricularPlan());
		assertFalse(studentCurricularPlanToDelete.hasStudent());
		assertFalse(studentCurricularPlanToDelete.hasAnyEnrolments());
		assertFalse(studentCurricularPlanToDelete.hasBranch());
		
		assertFalse(studentCurricularPlanToDelete.hasAnyGratuitySituations());
		assertFalse(studentCurricularPlanToDelete.hasMasterDegreeThesis());
		assertFalse(studentCurricularPlanToDelete.hasAnyCreditsInScientificAreas());
		assertFalse(studentCurricularPlanToDelete.hasAnyCreditsInAnySecundaryAreas());
		assertFalse(studentCurricularPlanToDelete.hasAnyNotNeedToEnrollCurricularCourses());
		
		try {
			studentCurricularPlanLEICToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse(studentCurricularPlanLEICToDelete.hasSecundaryBranch());
		
		try {
			studentCurricularPlanLEECToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse(studentCurricularPlanLEECToDelete.hasSecundaryBranch());
	}
}
