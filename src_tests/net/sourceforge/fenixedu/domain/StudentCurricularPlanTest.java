package net.sourceforge.fenixedu.domain;

import java.util.Date;

import net.sourceforge.fenixedu.domain.degree.enrollment.INotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

public class StudentCurricularPlanTest extends DomainTestBase {

	private IStudentCurricularPlan newStudentCurricularPlan;
	private IStudentCurricularPlan activeStudentCurricularPlan;
	private IStudentCurricularPlan concludedStudentCurricularPlan;
	
	private IStudentCurricularPlan studentCurricularPlanToDelete = null;
	private IStudentCurricularPlanLEIC studentCurricularPlanLEICToDelete = null;
	private IStudentCurricularPlanLEEC studentCurricularPlanLEECToDelete = null;
	
	private IStudentCurricularPlan studentCurricularPlanToReadFrom = null;
	private IEnrolment enrolmentToBeRead = null;
	private ICurricularCourse curricularCourse = null;
	private IExecutionPeriod executionPeriod = null;
	
	private IStudent studentForNewStudentCurricularPlan = null;
	private IDegreeCurricularPlan degreeCurricularPlanForNewStudentCurricularPlan = null;
	private IBranch branchForNewStudentCurricularPlan = null;
	private Date startDateForNewStudentCurricularPlan = null;
	private StudentCurricularPlanState stateForNewStudentCurricularPlan = null;
	private Double givenCreditsForNewStudentCurricularPlan = null;
	private Specialization specializationForNewStudentCurricularPlan = null;
	
	private IStudentCurricularPlanLEIC studentCurricularPlanToSetAreas = null;
	private IBranch primaryArea = null;
	private IBranch secondaryArea = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		IStudent student = new Student();
		IDegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
		
		setUpChangeState(student, degreeCurricularPlan);
		setUpCreate(student, degreeCurricularPlan);
	}

	private void setUpSetStudentAreasWithoutRestrictions() {
		studentCurricularPlanToSetAreas = new StudentCurricularPlanLEIC();
		primaryArea = new Branch();
		secondaryArea = new Branch();		
	}

	private void setUpDelete() {
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

	private void setUpGetEnrolmentByCurricularCourseAndExecutionPeriod() {
		
		studentCurricularPlanToReadFrom = new StudentCurricularPlan();
		enrolmentToBeRead = new Enrolment();
		curricularCourse = new CurricularCourse();
		executionPeriod = new ExecutionPeriod();
		
		ICurricularCourse cc1 = new CurricularCourse();
		IExecutionPeriod ep1 = new ExecutionPeriod();
		
		IEnrolment e1 = new Enrolment();
		e1.setExecutionPeriod(executionPeriod);
		e1.setCurricularCourse(cc1);
		studentCurricularPlanToReadFrom.addEnrolments(e1);
		
		IEnrolment e2 = new Enrolment();
		e2.setExecutionPeriod(ep1);
		e2.setCurricularCourse(curricularCourse);
		studentCurricularPlanToReadFrom.addEnrolments(e2);
		
		studentCurricularPlanToReadFrom.addEnrolments(enrolmentToBeRead);
		enrolmentToBeRead.setExecutionPeriod(executionPeriod);
		enrolmentToBeRead.setCurricularCourse(curricularCourse);
		
	}




	private void setUpCreate(IStudent student, IDegreeCurricularPlan degreeCurricularPlan) {
		newStudentCurricularPlan = new StudentCurricularPlan(student, degreeCurricularPlan,
												StudentCurricularPlanState.ACTIVE, new Date());
		
		
		studentForNewStudentCurricularPlan = new Student();
		degreeCurricularPlanForNewStudentCurricularPlan = new DegreeCurricularPlan();
		branchForNewStudentCurricularPlan = new Branch();
		startDateForNewStudentCurricularPlan = new Date(2002,4,13);
		stateForNewStudentCurricularPlan = StudentCurricularPlanState.CONCLUDED;
		givenCreditsForNewStudentCurricularPlan = 10.0;
		specializationForNewStudentCurricularPlan = Specialization.MASTER_DEGREE; 
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
	
		
	
	
	public void testCreate() {
		assertTrue("Failed to assign DegreeCurricularPlan", newStudentCurricularPlan.hasDegreeCurricularPlan());
		assertTrue("Failed to assign Student", newStudentCurricularPlan.hasStudent());
		assertNotNull("Failed to assign startDate", newStudentCurricularPlan.getStartDate());
		assertNotNull("Failed to assign when", newStudentCurricularPlan.getWhen());
		
		assertTrue("Failed to activate StudentCurricularPlan", newStudentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.ACTIVE));
		assertTrue("Failed to inactivate previously active StudentCurricularPlan", activeStudentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.INACTIVE));
		
		
		
		IStudentCurricularPlan scp = new StudentCurricularPlan(studentForNewStudentCurricularPlan,
				degreeCurricularPlanForNewStudentCurricularPlan, branchForNewStudentCurricularPlan,
				startDateForNewStudentCurricularPlan, stateForNewStudentCurricularPlan, 
				givenCreditsForNewStudentCurricularPlan, specializationForNewStudentCurricularPlan) ;
		
		assertEquals("Failed to assign Student", scp.getStudent(),studentForNewStudentCurricularPlan);
		assertEquals("Failed to assign DegreeCurricularPlan", scp.getDegreeCurricularPlan(),degreeCurricularPlanForNewStudentCurricularPlan);
		assertEquals("Failed to assign Branch", scp.getBranch(),branchForNewStudentCurricularPlan);
		assertEquals("Failed to assign startDate", scp.getStartDate(),startDateForNewStudentCurricularPlan);
		assertEquals("Failed to assign currentState", scp.getCurrentState(),stateForNewStudentCurricularPlan);
		assertEquals("Failed to assign givenCredits", scp.getGivenCredits(),givenCreditsForNewStudentCurricularPlan);
		assertEquals("Failed to assign Specialization", scp.getSpecialization(),specializationForNewStudentCurricularPlan);
	}
	
	public void testChangeState() {
		try {
			concludedStudentCurricularPlan.changeState(StudentCurricularPlanState.PAST);
		} catch (DomainException e) {
			fail("Should have been changed.");
		}
		
		assertTrue("StudentCurricularPlan's state doesn't match expected state", concludedStudentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.PAST));
		
		try {
			concludedStudentCurricularPlan.changeState(StudentCurricularPlanState.ACTIVE);
			fail("Should not have been changed.");
		} catch (DomainException e) {
			
		}
		
		assertTrue("StudentCurricularPlan's state doesn't match expected state", concludedStudentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.PAST));
	}
	
	public void testDelete() {
		
		setUpDelete();
		
		try {
			studentCurricularPlanToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse("Failed to dereference DegreeCurricularPlan", studentCurricularPlanToDelete.hasDegreeCurricularPlan());
		assertFalse("Failed to dereference Student", studentCurricularPlanToDelete.hasStudent());
		assertFalse("Failed to dereference Enrolments", studentCurricularPlanToDelete.hasAnyEnrolments());
		assertFalse("Failed to dereference Branch", studentCurricularPlanToDelete.hasBranch());
		assertFalse("Failed to dereference GratuitySituations", studentCurricularPlanToDelete.hasAnyGratuitySituations());
		assertFalse("Failed to dereference MasterDegreeThesis", studentCurricularPlanToDelete.hasMasterDegreeThesis());
		assertFalse("Failed to dereference CreditsInScientificAreas", studentCurricularPlanToDelete.hasAnyCreditsInScientificAreas());
		assertFalse("Failed to dereference CreditsInAnySecundaryAreas", studentCurricularPlanToDelete.hasAnyCreditsInAnySecundaryAreas());
		assertFalse("Failed to dereference NotNeedToEnrollCurricularCourses", studentCurricularPlanToDelete.hasAnyNotNeedToEnrollCurricularCourses());
		
		try {
			studentCurricularPlanLEICToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse("Failed to dereference secondary Branch", studentCurricularPlanLEICToDelete.hasSecundaryBranch());
		
		try {
			studentCurricularPlanLEECToDelete.delete();
		} catch (DomainException e) {
			fail("Unexpected DomainException: should have been deleted.");
		}
		
		assertFalse("Failed to dereference secondary Branch", studentCurricularPlanLEECToDelete.hasSecundaryBranch());
	}
	
	public void testGetEnrolmentByCurricularCourseAndExecutionPeriod () {
		
		setUpGetEnrolmentByCurricularCourseAndExecutionPeriod();
		
		IEnrolment enrolment = studentCurricularPlanToReadFrom.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse,executionPeriod);
		
		assertEquals("Enrolment is not the one that was expected", enrolment,enrolmentToBeRead);
	}
	
	public void testSetStudentAreasWithoutRestrictions() {

		setUpSetStudentAreasWithoutRestrictions();
		
		assertFalse("StudentCurricularPlan already has Branch", studentCurricularPlanToSetAreas.hasBranch());
		assertFalse("StudentCurricularPlan already has secondary Branch", studentCurricularPlanToSetAreas.hasSecundaryBranch());
		
		try {
			studentCurricularPlanToSetAreas.setStudentAreasWithoutRestrictions(primaryArea,primaryArea);
			fail("same areas should not have been set");
		} catch (DomainException e) {
			
		}
		
		assertFalse("StudentCurricularPlan shouldn't have Branch", studentCurricularPlanToSetAreas.hasBranch());
		assertFalse("StudentCurricularPlan shouldn't have secondary Branch", studentCurricularPlanToSetAreas.hasSecundaryBranch());
		
		try {
			studentCurricularPlanToSetAreas.setStudentAreasWithoutRestrictions(primaryArea,secondaryArea);
		} catch (DomainException e) {
			fail("same areas should have been set");
		}
		
		assertEquals("StudentCurricularPlan's Branch does not match expected Branch", studentCurricularPlanToSetAreas.getBranch(),primaryArea);
		assertEquals("StudentCurricularPlan's secondary Branch does not match expected Branch", studentCurricularPlanToSetAreas.getSecundaryBranch(),secondaryArea);
	}
}
