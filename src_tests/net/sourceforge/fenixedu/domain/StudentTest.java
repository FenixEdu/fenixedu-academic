package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.StudentType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.StudentState;

public class StudentTest extends DomainTestBase {

	private Person person = null;
	private Integer studentNumber = null;
	private StudentKind studentKind = null;
	private StudentState studentState = null;
	private Boolean payedTuition = null;
	private Boolean enrolmentForbidden = null;
	private EntryPhase entryPhase = null;
	private DegreeType degreeType = null;
		
	private Registration studentWithActiveStudentCurricularPlan = null;
	private Registration studentWithoutActiveStudentCurricularPlan = null;
	private StudentCurricularPlan activeStudentCurricularPlan = null;
	
	private void setUpForNewStudentCase() {
		person = new Person();
		studentNumber = 49555;
//		studentKind = new StudentKind();
		studentKind.setStudentType(StudentType.NORMAL);
		studentState = new StudentState(StudentState.INSCRITO);
		payedTuition = true;
		enrolmentForbidden = false;
		entryPhase = EntryPhase.FIRST_PHASE_OBJ;
		degreeType = DegreeType.DEGREE;
	}
	
	public void testCreate() {
		
		setUpForNewStudentCase();
		
		Registration registration = new Registration(person, studentNumber, studentKind, studentState, 
				payedTuition, enrolmentForbidden, entryPhase, degreeType);
		
		assertEquals("Failed to assign Person", registration.getPerson(),person);
		assertEquals("Failed to assign studentNumber", registration.getNumber(),studentNumber);
		assertEquals("Failed to assign studentKind", registration.getStudentKind(),studentKind);
		assertEquals("Failed to assign state", registration.getState(),studentState);
		assertEquals("Failed to assign payedTuition", registration.getPayedTuition(),payedTuition);
		assertEquals("Failed to assign enrollmentForbidden", registration.getEnrollmentForbidden(),enrolmentForbidden);
		assertEquals("Failed to assign entryPhase", registration.getEntryPhase(),entryPhase);
		assertEquals("Failed to assign degreeType", registration.getDegreeType(),degreeType);
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
