package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.degree.enrollment.INotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

public class StudentCurricularPlanTest extends DomainTestBase {

	private IStudentCurricularPlan newStudentCurricularPlan;
	private IStudentCurricularPlan activeStudentCurricularPlan;
	private IStudentCurricularPlan concludedStudentCurricularPlan;
	
	private IStudentCurricularPlan studentCurricularPlanToDelete = null;
	private IStudentCurricularPlanLEIC studentCurricularPlanLEICToDelete = null;
	private IStudentCurricularPlanLEEC studentCurricularPlanLEECToDelete = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		IStudent student = new Student();
		IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
		
		setUpChangeState(student, degreeCurricularPlan);
		setUpCreate(student, degreeCurricularPlan);
		
		
		studentCurricularPlanToDelete = new StudentCurricularPlan();
		studentCurricularPlanLEICToDelete = new StudentCurricularPlanLEIC();
		studentCurricularPlanLEECToDelete = new StudentCurricularPlanLEEC();
		
		IBranch br1 = new Branch();
		studentCurricularPlanToDelete.setBranch(br1);
		
		studentCurricularPlanLEICToDelete.setSecundaryBranch(br1);
		studentCurricularPlanLEECToDelete.setSecundaryBranch(br1);
		
		IEnrolment e1 = new Enrolment();
		studentCurricularPlanToDelete.addEnrolments(e1);
		
		IStudent s1 = new Student();
		studentCurricularPlanToDelete.setStudent(s1);
		
		IDegreeCurricularPlan dcp1 = new DegreeCurricularPlan();
		studentCurricularPlanToDelete.setDegreeCurricularPlan(dcp1);
		
		IEmployee emp1 = new Employee();
		studentCurricularPlanToDelete.setEmployee(emp1);
		
		IGratuitySituation gs1 = new GratuitySituation();
		gs1.setStudentCurricularPlan(studentCurricularPlanToDelete);
		
		IMasterDegreeThesis mdt1 = new MasterDegreeThesis();
		studentCurricularPlanToDelete.setMasterDegreeThesis(mdt1);
		
		INotNeedToEnrollInCurricularCourse notNeed1 = new NotNeedToEnrollInCurricularCourse();
		notNeed1.setStudentCurricularPlan(studentCurricularPlanToDelete);
		
		ICreditsInAnySecundaryArea cred1 = new CreditsInAnySecundaryArea();
		cred1.setStudentCurricularPlan(studentCurricularPlanToDelete);
		
		ICreditsInScientificArea cred2 = new CreditsInScientificArea();
		cred2.setStudentCurricularPlan(studentCurricularPlanToDelete);
	}
	
	
	
	
	private void setUpCreate(IStudent student, IDegreeCurricularPlan degreeCurricularPlan) {
		newStudentCurricularPlan = new StudentCurricularPlan(student, degreeCurricularPlan,
												StudentCurricularPlanState.ACTIVE, new Date());
	}
	
	
	
	private void setUpChangeState(IStudent student, IDegreeCurricularPlan degreeCurricularPlan) {
		activeStudentCurricularPlan = new StudentCurricularPlan();
		concludedStudentCurricularPlan = new StudentCurricularPlan();
		
		activeStudentCurricularPlan.setCurrentState(StudentCurricularPlanState.ACTIVE);
		concludedStudentCurricularPlan.setCurrentState(StudentCurricularPlanState.CONCLUDED);
		
		
		activeStudentCurricularPlan.setStudent(student);
		concludedStudentCurricularPlan.setStudent(student);

		
		
		degreeCurricularPlan.addStudentCurricularPlans(activeStudentCurricularPlan);
		degreeCurricularPlan.addStudentCurricularPlans(concludedStudentCurricularPlan);
	}
	
	
	
	
	
	
	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	
	public void testCreate() {
		assertTrue(newStudentCurricularPlan.hasDegreeCurricularPlan());
		assertTrue(newStudentCurricularPlan.hasStudent());
		assertNotNull(newStudentCurricularPlan.getStartDate());
		assertNotNull(newStudentCurricularPlan.getWhen());
		
		assertTrue(newStudentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.ACTIVE));
		assertTrue(activeStudentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.INACTIVE));
	}
	
	public void testChangeState() {
		try {
			concludedStudentCurricularPlan.changeState(StudentCurricularPlanState.PAST);
		} catch (DomainException e) {
			fail("Should have been changed.");
		}
		
		assertTrue(concludedStudentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.PAST));
		
		try {
			concludedStudentCurricularPlan.changeState(StudentCurricularPlanState.ACTIVE);
			fail("Should not have been changed.");
		} catch (DomainException e) {
			
		}
		
		assertTrue(concludedStudentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.PAST));
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
