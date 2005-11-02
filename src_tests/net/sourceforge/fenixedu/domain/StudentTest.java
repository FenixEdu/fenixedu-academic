package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.StudentState;
import net.sourceforge.fenixedu.util.StudentType;

public class StudentTest extends DomainTestBase {

	private IPerson person = null;
	private Integer studentNumber = null;
	private IStudentKind studentKind = null;
	private StudentState studentState = null;
	private Boolean payedTuition = null;
	private Boolean enrolmentForbidden = null;
	private EntryPhase entryPhase = null;
	private DegreeType degreeType = null;
		
	private IStudent studentWithActiveStudentCurricularPlan = null;
	private IStudent studentWithoutActiveStudentCurricularPlan = null;
	private IStudentCurricularPlan activeStudentCurricularPlan = null;
	
	private void setUpForNewStudentCase() {
		person = new Person();
		studentNumber = 49555;
		studentKind = new StudentKind();
		studentKind.setStudentType(new StudentType(StudentType.NORMAL));
		studentState = new StudentState(StudentState.INSCRITO);
		payedTuition = true;
		enrolmentForbidden = false;
		entryPhase = EntryPhase.FIRST_PHASE_OBJ;
		degreeType = DegreeType.DEGREE;
	}
	
	public void testCreate() {
		
		setUpForNewStudentCase();
		
		IStudent student = new Student(person, studentNumber, studentKind, studentState, 
				payedTuition, enrolmentForbidden, entryPhase, degreeType);
		
		assertEquals("Failed to assign Person", student.getPerson(),person);
		assertEquals("Failed to assign studentNumber", student.getNumber(),studentNumber);
		assertEquals("Failed to assign studentKind", student.getStudentKind(),studentKind);
		assertEquals("Failed to assign state", student.getState(),studentState);
		assertEquals("Failed to assign payedTuition", student.getPayedTuition(),payedTuition);
		assertEquals("Failed to assign enrollmentForbidden", student.getEnrollmentForbidden(),enrolmentForbidden);
		assertEquals("Failed to assign entryPhase", student.getEntryPhase(),entryPhase);
		assertEquals("Failed to assign degreeType", student.getDegreeType(),degreeType);
	}


	public void testGetActiveStudentCurricularPlan () {
		
		setUpGetActiveStudentCurricularPlan();
		
		IStudentCurricularPlan activeSCP = studentWithActiveStudentCurricularPlan.getActiveStudentCurricularPlan();
		assertEquals("Returned StudentCurricularPlan does not match expected", activeSCP, activeStudentCurricularPlan);
		assertEquals("Returned StudentCurricularPlan is not ACTIVE", activeSCP.getCurrentState(),StudentCurricularPlanState.ACTIVE);
		
		IStudentCurricularPlan someSCP = studentWithoutActiveStudentCurricularPlan.getActiveStudentCurricularPlan();
		assertNull("Should not have returned a StudentCurricularPlan", someSCP);
	}

	private void setUpGetActiveStudentCurricularPlan() {
		studentWithActiveStudentCurricularPlan = new Student();
		studentWithoutActiveStudentCurricularPlan = new Student();
		
		activeStudentCurricularPlan = new StudentCurricularPlan();
		activeStudentCurricularPlan.setCurrentState(StudentCurricularPlanState.ACTIVE);
		
		IStudentCurricularPlan someSCP = new StudentCurricularPlan();
		someSCP.setCurrentState(StudentCurricularPlanState.CONCLUDED);
		
		studentWithActiveStudentCurricularPlan.addStudentCurricularPlans(activeStudentCurricularPlan);
		studentWithActiveStudentCurricularPlan.addStudentCurricularPlans(someSCP);
		
		studentWithoutActiveStudentCurricularPlan.addStudentCurricularPlans(someSCP);
	}

}
