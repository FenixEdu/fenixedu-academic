package net.sourceforge.fenixedu.domain;

import java.text.ParseException;
import java.util.Date;

import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.DateFormatUtil;

import org.joda.time.YearMonthDay;

public class StudentCurricularPlanTest extends DomainTestBase {

	private StudentCurricularPlan newStudentCurricularPlan;
	private StudentCurricularPlan activeStudentCurricularPlan;
	private StudentCurricularPlan concludedStudentCurricularPlan;
	
	private StudentCurricularPlan studentCurricularPlanToDelete = null;
	private StudentCurricularPlanLEIC studentCurricularPlanLEICToDelete = null;
	private StudentCurricularPlanLEEC studentCurricularPlanLEECToDelete = null;
	
	private StudentCurricularPlan studentCurricularPlanToReadFrom = null;
	private Enrolment enrolmentToBeRead = null;
	private CurricularCourse curricularCourse = null;
	private ExecutionPeriod executionPeriod = null;
	
	private Registration studentForNewStudentCurricularPlan = null;
	private DegreeCurricularPlan degreeCurricularPlanForNewStudentCurricularPlan = null;
	private Branch branchForNewStudentCurricularPlan = null;
	private Date startDateForNewStudentCurricularPlan = null;
	private StudentCurricularPlanState stateForNewStudentCurricularPlan = null;
	private Double givenCreditsForNewStudentCurricularPlan = null;
	private Specialization specializationForNewStudentCurricularPlan = null;
	
	private StudentCurricularPlanLEIC studentCurricularPlanToSetAreas = null;
	private Branch primaryArea = null;
	private Branch secondaryArea = null;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		Registration registration = new Registration();
		DegreeCurricularPlan degreeCurricularPlan = new DegreeCurricularPlan();
		
		setUpChangeState(registration, degreeCurricularPlan);
		setUpCreate(registration, degreeCurricularPlan);
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

		Branch br1 = new Branch();
		studentCurricularPlanToDelete.setBranch(br1);
		studentCurricularPlanLEICToDelete.setSecundaryBranch(br1);
		studentCurricularPlanLEECToDelete.setSecundaryBranch(br1);
		
		Enrolment e1 = new Enrolment();
		studentCurricularPlanToDelete.addEnrolments(e1);
		
		Registration s1 = new Registration();
		studentCurricularPlanToDelete.setStudent(s1);
		
		DegreeCurricularPlan dcp1 = new DegreeCurricularPlan();
		studentCurricularPlanToDelete.setDegreeCurricularPlan(dcp1);
		
//		Employee emp1 = new Employee();
//		studentCurricularPlanToDelete.setEmployee(emp1);
		
		MasterDegreeThesis mdt1 = new MasterDegreeThesis();
		studentCurricularPlanToDelete.setMasterDegreeThesis(mdt1);
		
		NotNeedToEnrollInCurricularCourse notNeed1 = new NotNeedToEnrollInCurricularCourse();
		notNeed1.setStudentCurricularPlan(studentCurricularPlanToDelete);
		
		CreditsInAnySecundaryArea cred1 = new CreditsInAnySecundaryArea();
		cred1.setStudentCurricularPlan(studentCurricularPlanToDelete);
		
		CreditsInScientificArea cred2 = new CreditsInScientificArea();
		cred2.setStudentCurricularPlan(studentCurricularPlanToDelete);
	}

	private void setUpGetEnrolmentByCurricularCourseAndExecutionPeriod() {
		
		studentCurricularPlanToReadFrom = new StudentCurricularPlan();
		enrolmentToBeRead = new Enrolment();
		curricularCourse = new CurricularCourse();
		executionPeriod = new ExecutionPeriod();
		
		CurricularCourse cc1 = new CurricularCourse();
		ExecutionPeriod ep1 = new ExecutionPeriod();
		
		Enrolment e1 = new Enrolment();
		e1.setExecutionPeriod(executionPeriod);
		e1.setCurricularCourse(cc1);
		studentCurricularPlanToReadFrom.addEnrolments(e1);
		
		Enrolment e2 = new Enrolment();
		e2.setExecutionPeriod(ep1);
		e2.setCurricularCourse(curricularCourse);
		studentCurricularPlanToReadFrom.addEnrolments(e2);
		
		studentCurricularPlanToReadFrom.addEnrolments(enrolmentToBeRead);
		enrolmentToBeRead.setExecutionPeriod(executionPeriod);
		enrolmentToBeRead.setCurricularCourse(curricularCourse);
		
	}




	private void setUpCreate(Registration student, DegreeCurricularPlan degreeCurricularPlan) throws ParseException {
		newStudentCurricularPlan = new StudentCurricularPlan(student, degreeCurricularPlan, StudentCurricularPlanState.ACTIVE, new YearMonthDay());
		
		
		studentForNewStudentCurricularPlan = new Registration();
		degreeCurricularPlanForNewStudentCurricularPlan = new DegreeCurricularPlan();
		branchForNewStudentCurricularPlan = new Branch();
		startDateForNewStudentCurricularPlan = DateFormatUtil.parse("yyyy/mm/dd", "2002/04/13");
		stateForNewStudentCurricularPlan = StudentCurricularPlanState.CONCLUDED;
		givenCreditsForNewStudentCurricularPlan = 10.0;
		specializationForNewStudentCurricularPlan = Specialization.MASTER_DEGREE; 
	}
	
	
	
	private void setUpChangeState(Registration student, DegreeCurricularPlan degreeCurricularPlan) {
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
		assertTrue("Failed to assign Registration", newStudentCurricularPlan.hasStudent());
		assertNotNull("Failed to assign startDate", newStudentCurricularPlan.getStartDate());
		assertNotNull("Failed to assign when", newStudentCurricularPlan.getWhen());
		
		assertTrue("Failed to activate StudentCurricularPlan", newStudentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.ACTIVE));
		assertTrue("Failed to inactivate previously active StudentCurricularPlan", activeStudentCurricularPlan.getCurrentState().equals(StudentCurricularPlanState.INACTIVE));
		
		
		
		StudentCurricularPlan scp = new StudentCurricularPlan(studentForNewStudentCurricularPlan,
				degreeCurricularPlanForNewStudentCurricularPlan, branchForNewStudentCurricularPlan,
				new YearMonthDay(startDateForNewStudentCurricularPlan), stateForNewStudentCurricularPlan, 
				givenCreditsForNewStudentCurricularPlan, specializationForNewStudentCurricularPlan) ;
		
		assertEquals("Failed to assign Registration", scp.getStudent(),studentForNewStudentCurricularPlan);
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
		assertFalse("Failed to dereference Registration", studentCurricularPlanToDelete.hasStudent());
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
		
		Enrolment enrolment = studentCurricularPlanToReadFrom.getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse,executionPeriod);
		
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
