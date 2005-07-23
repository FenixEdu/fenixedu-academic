package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.degree.DegreeType;
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
	
	protected void setUp() throws Exception {
		super.setUp();
		
		setUpForNewStudentCase();
	}
	
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

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testNewStudent() {
		IStudent student = new Student(person, studentNumber, studentKind, studentState, 
				payedTuition, enrolmentForbidden, entryPhase, degreeType);
		
		assertEquals(student.getPerson(),person);
		assertEquals(student.getNumber(),studentNumber);
		assertEquals(student.getStudentKind(),studentKind);
		assertEquals(student.getState(),studentState);
		assertEquals(student.getPayedTuition(),payedTuition);
		assertEquals(student.getEnrollmentForbidden(),enrolmentForbidden);
		assertEquals(student.getEntryPhase(),entryPhase);
		assertEquals(student.getDegreeType(),degreeType);
	}
}
