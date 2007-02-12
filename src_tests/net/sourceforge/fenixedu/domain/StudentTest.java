package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;

public class StudentTest extends DomainTestBase {

	private Person person = null;
	private Integer studentNumber = null;
		
	private Registration studentWithActiveStudentCurricularPlan = null;
	private Registration studentWithoutActiveStudentCurricularPlan = null;
	private StudentCurricularPlan activeStudentCurricularPlan = null;
	
	private void setUpForNewStudentCase() {
		person = new Person();
		studentNumber = 49555;
	}
	
	public void testCreate() {
		
		setUpForNewStudentCase();
		
		Registration registration = new Registration(person, studentNumber);
		
		assertEquals("Failed to assign Person", registration.getPerson(),person);
		assertEquals("Failed to assign studentNumber", registration.getNumber(),studentNumber);
		assertEquals("Failed to assign state", registration.isInRegisteredState(), true);
	}


	public void testGetActiveStudentCurricularPlan () {
		
		setUpGetActiveStudentCurricularPlan();
		
		StudentCurricularPlan activeSCP = studentWithActiveStudentCurricularPlan.getActiveStudentCurricularPlan();
		assertEquals("Returned StudentCurricularPlan does not match expected", activeSCP, activeStudentCurricularPlan);
		assertEquals("Returned StudentCurricularPlan is not ACTIVE", activeSCP.getCurrentState(),StudentCurricularPlanState.ACTIVE);
		
		StudentCurricularPlan someSCP = studentWithoutActiveStudentCurricularPlan.getActiveStudentCurricularPlan();
		assertNull("Should not have returned a StudentCurricularPlan", someSCP);
	}

	private void setUpGetActiveStudentCurricularPlan() {
		studentWithActiveStudentCurricularPlan = new Registration();
		studentWithoutActiveStudentCurricularPlan = new Registration();
		
		activeStudentCurricularPlan = new StudentCurricularPlan();
		activeStudentCurricularPlan.setCurrentState(StudentCurricularPlanState.ACTIVE);
		
		StudentCurricularPlan someSCP = new StudentCurricularPlan();
		someSCP.setCurrentState(StudentCurricularPlanState.CONCLUDED);
		
		studentWithActiveStudentCurricularPlan.addStudentCurricularPlans(activeStudentCurricularPlan);
		studentWithActiveStudentCurricularPlan.addStudentCurricularPlans(someSCP);
		
		studentWithoutActiveStudentCurricularPlan.addStudentCurricularPlans(someSCP);
	}

}
