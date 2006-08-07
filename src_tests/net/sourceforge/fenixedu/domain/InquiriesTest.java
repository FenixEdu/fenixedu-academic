/*
 * Created on 21/Ago/2005 - 14:39:36
 * 
 */

package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesCourse;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesRoom;
import net.sourceforge.fenixedu.dataTransferObject.inquiries.InfoInquiriesTeacher;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesCourse;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRegistry;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesRoom;
import net.sourceforge.fenixedu.domain.inquiries.InquiriesTeacher;
import net.sourceforge.fenixedu.domain.space.OldRoom;

import org.apache.commons.lang.NullArgumentException;

/**
 * @author João Fialho & Rita Ferreira
 *
 */
public class InquiriesTest extends DomainTestBase {
	
	private ExecutionCourse executionCourse;
	private ExecutionDegree executionDegreeStudent;
	private ExecutionDegree executionDegreeCourse;
	private ExecutionPeriod executionPeriod;
	private SchoolClass schoolClass;
	
	private InfoInquiriesCourse infoInquiriesCourse;
	
	private InquiriesCourse inquiriesCourse;
	
	private NonAffiliatedTeacher nonAffiliatedTeacher;
	private Teacher teacher;
	
	private InfoInquiriesTeacher infoInquiriesTeacher;
	
	private InfoInquiriesRoom infoInquiriesRoom;
	private OldRoom room;
	
	private Student student;
	
	protected void setUp() throws Exception {
		super.setUp();
		
		executionCourse = new ExecutionCourse();
//		executionDegreeStudent = new ExecutionDegree();
//		executionDegreeCourse = new ExecutionDegree();
//		executionPeriod = new ExecutionPeriod();
//		schoolClass = new SchoolClass();
		
		infoInquiriesCourse = new InfoInquiriesCourse();
		
		infoInquiriesCourse.setClassCoordination(2.0);
		infoInquiriesCourse.setContributionForGraduation(2.0);
		infoInquiriesCourse.setEvaluationMethodAdequation(2.0);
		infoInquiriesCourse.setGlobalAppreciation(2.0);
		infoInquiriesCourse.setPreviousKnowledgeArticulation(2.0);
		infoInquiriesCourse.setStudentCurricularYear(3);
		infoInquiriesCourse.setStudentFirstEnrollment(1);
		infoInquiriesCourse.setStudyElementsContribution(2.0);
		infoInquiriesCourse.setWeeklySpentHours(2);
	}
	
	private void initializeInfoInquiriesTeacher() {
		infoInquiriesTeacher = new InfoInquiriesTeacher();
		
		infoInquiriesTeacher.setStudentAssiduity(2);
		infoInquiriesTeacher.setTeacherAssiduity(null);
		infoInquiriesTeacher.setTeacherPunctuality(2.0);
		infoInquiriesTeacher.setTeacherClarity(2.0);
		infoInquiriesTeacher.setTeacherAssurance(2.0);
		infoInquiriesTeacher.setTeacherInterestStimulation(3.0);
		infoInquiriesTeacher.setTeacherAvailability(3.0);
		infoInquiriesTeacher.setTeacherReasoningStimulation(3.0);
		infoInquiriesTeacher.setGlobalAppreciation(2.0);
		
	}
	
	private void initializeInfoInquiriesRoom() {
		infoInquiriesRoom = new InfoInquiriesRoom();
		
		infoInquiriesRoom.setSpaceAdequation(1);
		infoInquiriesRoom.setEnvironmentalConditions(2);
		infoInquiriesRoom.setEquipmentQuality(3);
		
	}
	
	private void setUpCreateInquiriesTeacher() {
		inquiriesCourse = new InquiriesCourse(executionCourse, executionDegreeCourse,
				executionDegreeStudent, executionPeriod, schoolClass, infoInquiriesCourse, null, null, null);
		
//		teacher = new Teacher();
		nonAffiliatedTeacher = new NonAffiliatedTeacher();
		
		initializeInfoInquiriesTeacher();
		
	}
	
	private void setUpCreateInquiriesRoom() {
		inquiriesCourse = new InquiriesCourse(executionCourse, executionDegreeCourse,
				executionDegreeStudent, executionPeriod, schoolClass, infoInquiriesCourse, null, null, null);
		
		room = new OldRoom();
		initializeInfoInquiriesRoom();	
		
	}
	
	private void setUpCreateInquiriesRegistry() {
		student = new Student();
	}
	
	public void testCreateInquiriesCourse() {
		
		try {
			new InquiriesCourse(null, executionDegreeCourse,
					executionDegreeStudent, executionPeriod, schoolClass, infoInquiriesCourse, null, null, null);
			fail("Should have thrown a DomainException");

		} catch(DomainException de) {
			
		}
		
		try {
			new InquiriesCourse(executionCourse, null,
					executionDegreeStudent, executionPeriod, schoolClass, infoInquiriesCourse, null, null, null);
			fail("Should have thrown a DomainException");

		} catch(DomainException de) {
			
		}
		
		try {
			new InquiriesCourse(executionCourse, executionDegreeCourse,
					null, executionPeriod, schoolClass, infoInquiriesCourse, null, null, null);
			fail("Should have thrown a DomainException");

		} catch(DomainException de) {
			
		}
		
		try {
			new InquiriesCourse(executionCourse, executionDegreeCourse,
					executionDegreeStudent, null, schoolClass, infoInquiriesCourse, null, null, null);
			fail("Should have thrown a DomainException");

		} catch(DomainException de) {
			
		}
		
		InquiriesCourse newInquiriesCourse = new InquiriesCourse(executionCourse, executionDegreeCourse,
				executionDegreeStudent, executionPeriod, schoolClass, infoInquiriesCourse, null, null, null);
		
		assertTrue("Failed to reference executionCourse!", newInquiriesCourse.hasExecutionCourse());
		assertTrue("Failed to reference executionDegreeCourse!", newInquiriesCourse.hasExecutionDegreeCourse());
		assertTrue("Failed to reference executionDegreeStudent!", newInquiriesCourse.hasExecutionDegreeStudent());
		assertTrue("Failed to reference executionPeriod!", newInquiriesCourse.hasExecutionPeriod());
		assertTrue("Failed to reference schoolClass!", newInquiriesCourse.hasStudentSchoolClass());
		
		assertEquals("Unexpected value for classCoordination!",
				infoInquiriesCourse.getClassCoordination(), newInquiriesCourse.getClassCoordination());
		assertEquals("Unexpected value for contributionForGraduation!",
				infoInquiriesCourse.getContributionForGraduation(), newInquiriesCourse.getContributionForGraduation());
		assertEquals("Unexpected value for evaluationMethodAdequation!",
				infoInquiriesCourse.getEvaluationMethodAdequation(), newInquiriesCourse.getEvaluationMethodAdequation());
		assertEquals("Unexpected value for globalAppreciation!",
				infoInquiriesCourse.getGlobalAppreciation(), newInquiriesCourse.getGlobalAppreciation());
		assertEquals("Unexpected value for previousKnowledgeArticulation!",
				infoInquiriesCourse.getPreviousKnowledgeArticulation(), newInquiriesCourse.getPreviousKnowledgeArticulation());
		assertEquals("Unexpected value for studentCurricularYear!",
				infoInquiriesCourse.getStudentCurricularYear(), newInquiriesCourse.getStudentCurricularYear());
		assertEquals("Unexpected value for studentFirstEnrollment!",
				infoInquiriesCourse.getStudentFirstEnrollment(), newInquiriesCourse.getStudentFirstEnrollment());
		assertEquals("Unexpected value for studyElementsContribution!",
				infoInquiriesCourse.getStudyElementsContribution(), newInquiriesCourse.getStudyElementsContribution());
		assertEquals("Unexpected value for weeklySpentHours!",
				infoInquiriesCourse.getWeeklySpentHours(), newInquiriesCourse.getWeeklySpentHours());
	}
	
	public void testCreateInquiriesTeacher() {
		
		setUpCreateInquiriesTeacher();
		
		//TEACHER
		try {
			inquiriesCourse.createInquiriesTeacher((Teacher)null, ShiftType.LABORATORIAL, infoInquiriesTeacher);
			fail("Should have thrown a DomainException");

		} catch(DomainException nae) {
			
		}
		
		try {
			inquiriesCourse.createInquiriesTeacher(teacher, null, infoInquiriesTeacher);
			fail("Should have thrown a DomainException");

		} catch(DomainException nae) {
			
		}
		
		inquiriesCourse.createInquiriesTeacher(teacher, ShiftType.TEORICA, infoInquiriesTeacher);
		assertEquals("Failed to reference inquiriesTeacher!", inquiriesCourse.getAssociatedInquiriesTeachersCount(), 1);
		
		InquiriesTeacher inquiriesTeacher = inquiriesCourse.getAssociatedInquiriesTeachers().get(0);
		assertEquals("Unexpected value for studentAssiduity!",
				inquiriesTeacher.getStudentAssiduity(), infoInquiriesTeacher.getStudentAssiduity());
		assertEquals("Unexpected value for teacherAssiduity!",
				inquiriesTeacher.getTeacherAssiduity(), infoInquiriesTeacher.getTeacherAssiduity());
		assertEquals("Unexpected value for teacherPunctuality!",
				inquiriesTeacher.getTeacherPunctuality(), infoInquiriesTeacher.getTeacherPunctuality());
		assertEquals("Unexpected value for teacherClarity!",
				inquiriesTeacher.getTeacherClarity(), infoInquiriesTeacher.getTeacherClarity());
		assertEquals("Unexpected value for teacherAssurance!",
				inquiriesTeacher.getTeacherAssurance(), infoInquiriesTeacher.getTeacherAssurance());
		assertEquals("Unexpected value for teacherInterestStimulation!",
				inquiriesTeacher.getTeacherInterestStimulation(), infoInquiriesTeacher.getTeacherInterestStimulation());
		assertEquals("Unexpected value for teacherAvailability!",
				inquiriesTeacher.getTeacherAvailability(), infoInquiriesTeacher.getTeacherAvailability());
		assertEquals("Unexpected value for teacherReasoningStimulation!",
				inquiriesTeacher.getTeacherReasoningStimulation(), infoInquiriesTeacher.getTeacherReasoningStimulation());
		assertEquals("Unexpected value for globalAppreciation!",
				inquiriesTeacher.getGlobalAppreciation(), infoInquiriesTeacher.getGlobalAppreciation());
		


		//NON_AFFILIATED_TEACHER
		try {
			inquiriesCourse.createInquiriesTeacher((NonAffiliatedTeacher)null, ShiftType.LABORATORIAL, infoInquiriesTeacher);
			fail("Should have thrown a DomainException");

		} catch(DomainException nae) {
			
		}

		try {
			inquiriesCourse.createInquiriesTeacher(nonAffiliatedTeacher, null, infoInquiriesTeacher);
			fail("Should have thrown a NullArgumentException");

		} catch(DomainException nae) {
			
		}

		
		inquiriesCourse.createInquiriesTeacher(nonAffiliatedTeacher, ShiftType.LABORATORIAL, infoInquiriesTeacher);
		assertEquals("Failed to reference inquiriesTeacher!", inquiriesCourse.getAssociatedInquiriesTeachersCount(), 2);

		inquiriesTeacher = inquiriesCourse.getAssociatedInquiriesTeachers().get(1);
		assertEquals("Unexpected value for studentAssiduity!",
				inquiriesTeacher.getStudentAssiduity(), infoInquiriesTeacher.getStudentAssiduity());
		assertEquals("Unexpected value for teacherAssiduity!",
				inquiriesTeacher.getTeacherAssiduity(), infoInquiriesTeacher.getTeacherAssiduity());
		assertEquals("Unexpected value for teacherPunctuality!",
				inquiriesTeacher.getTeacherPunctuality(), infoInquiriesTeacher.getTeacherPunctuality());
		assertEquals("Unexpected value for teacherClarity!",
				inquiriesTeacher.getTeacherClarity(), infoInquiriesTeacher.getTeacherClarity());
		assertEquals("Unexpected value for teacherAssurance!",
				inquiriesTeacher.getTeacherAssurance(), infoInquiriesTeacher.getTeacherAssurance());
		assertEquals("Unexpected value for teacherInterestStimulation!",
				inquiriesTeacher.getTeacherInterestStimulation(), infoInquiriesTeacher.getTeacherInterestStimulation());
		assertEquals("Unexpected value for teacherAvailability!",
				inquiriesTeacher.getTeacherAvailability(), infoInquiriesTeacher.getTeacherAvailability());
		assertEquals("Unexpected value for teacherReasoningStimulation!",
				inquiriesTeacher.getTeacherReasoningStimulation(), infoInquiriesTeacher.getTeacherReasoningStimulation());
		assertEquals("Unexpected value for globalAppreciation!",
				inquiriesTeacher.getGlobalAppreciation(), infoInquiriesTeacher.getGlobalAppreciation());
		
	}
	
	public void testCreateInquiriesRoom() {
		setUpCreateInquiriesRoom();

		try {
			inquiriesCourse.createInquiriesRoom(null, infoInquiriesRoom);
			fail("Should have thrown a DomainException");

		} catch(DomainException nae) {
			
		}
		
		inquiriesCourse.createInquiriesRoom(room, infoInquiriesRoom);
		assertEquals("Failed to reference inquiriesRoom!", inquiriesCourse.getAssociatedInquiriesRoomsCount(), 1);

		InquiriesRoom inquiriesRoom = inquiriesCourse.getAssociatedInquiriesRooms().get(0);

		assertEquals("Unexpected value for SpaceAdequation!",
				inquiriesRoom.getSpaceAdequation(), infoInquiriesRoom.getSpaceAdequation());
		assertEquals("Unexpected value for EnvironmentalConditions!",
				inquiriesRoom.getEnvironmentalConditions(), infoInquiriesRoom.getEnvironmentalConditions());
		assertEquals("Unexpected value for equipmentQuality!",
				inquiriesRoom.getEquipmentQuality(), infoInquiriesRoom.getEquipmentQuality());


	}
	
	public void testCreateInquiriesRegistry() {
		
		setUpCreateInquiriesRegistry();
		
		try {
			new InquiriesRegistry(null, executionPeriod, student);
			fail("Should have thrown a NullArgumentException");

		} catch(NullArgumentException nae) {
			
		}
		
		try {
			new InquiriesRegistry(executionCourse, null, student);
			fail("Should have thrown a NullArgumentException");

		} catch(NullArgumentException nae) {
			
		}
		
		try {
			new InquiriesRegistry(executionCourse, executionPeriod, null);
			fail("Should have thrown a NullArgumentException");

		} catch(NullArgumentException nae) {
			
		}
		
		InquiriesRegistry inquiriesRegistry = new InquiriesRegistry(executionCourse, executionPeriod, student);
		
		assertTrue("Failed to reference executionCourse!", inquiriesRegistry.hasExecutionCourse());
		assertTrue("Failed to reference executionPeriod!", inquiriesRegistry.hasExecutionPeriod());
		assertTrue("Failed to reference student!", inquiriesRegistry.hasStudent());
	}

}
