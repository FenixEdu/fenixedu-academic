package net.sourceforge.fenixedu.domain;

import java.util.Calendar;

public class DistributionTeacherServiceByTeacherTest extends DomainTestBase {
	
	ExecutionCourse executionCourse;
	
	Teacher teacher;
		
	protected void setUp() throws Exception {
		super.setUp();
		
		// Prepare shifts of an executionCourse
		
		executionCourse = new ExecutionCourse();
		executionCourse.setIdInternal(0);
		
		/* Create Theoretical Shift 1 */
		
//		Shift shiftTeorica1 = new Shift();
//		shiftTeorica1.setIdInternal(0);
//		
//		shiftTeorica1.setTipo(ShiftType.TEORICA);
//		
//		Lesson lesson = new Lesson();
//		lesson.setIdInternal(0);
//		
//		Calendar beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 10, 0);
//		
//		lesson.setInicio(beginLessonDate);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 11, 30);
//		
//		lesson.setFim(beginLessonDate);
//		
//		shiftTeorica1.addAssociatedLessons(lesson);
//		
//		/* Create Theoretical Shift 2 */		
//		
//		Shift shiftTeorica2 = new Shift();
//		shiftTeorica2.setIdInternal(1);
//		
//		shiftTeorica2.setTipo(ShiftType.TEORICA);
//		
//		lesson = new Lesson();
//		lesson.setIdInternal(1);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 14, 0);
//		
//		lesson.setInicio(beginLessonDate);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 15, 30);
//		
//		lesson.setFim(beginLessonDate);
//		
//		shiftTeorica2.addAssociatedLessons(lesson);
//		
//		/* Create Pratical Shift 1 */
//		
//		Shift shiftPratica1 = new Shift();
//		shiftPratica1.setIdInternal(2);
//		
//		shiftPratica1.setTipo(ShiftType.PRATICA);
//		
//		lesson = new Lesson();
//		lesson.setIdInternal(2);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 14, 0);
//		
//		lesson.setInicio(beginLessonDate);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 16, 0);
//		
//		lesson.setFim(beginLessonDate);
//		
//		shiftPratica1.addAssociatedLessons(lesson);
//		
//		/* Create Pratical Shift 2 */
//		Shift shiftPratica2 = new Shift();
//		shiftPratica2.setIdInternal(3);
//		
//		shiftPratica2.setTipo(ShiftType.PRATICA);
//		
//		lesson = new Lesson();
//		lesson.setIdInternal(3);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 9, 0);
//		
//		lesson.setInicio(beginLessonDate);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 11, 0);
//		
//		lesson.setFim(beginLessonDate);
//		
//		shiftPratica2.addAssociatedLessons(lesson);
//
//		/* Create Pratical Shift 3 */
//		
//		Shift shiftPratica3 = new Shift();
//		shiftPratica3.setIdInternal(4);
//		
//		shiftPratica3.setTipo(ShiftType.PRATICA);
//		
//		lesson = new Lesson();
//		lesson.setIdInternal(4);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 17, 30);
//		
//		lesson.setInicio(beginLessonDate);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 19, 30);
//		
//		lesson.setFim(beginLessonDate);
//		
//		shiftPratica3.addAssociatedLessons(lesson);
//
//		/* Create Laboratorial Shift 1 */
//		Shift shiftLaboratorio1 = new Shift();
//		shiftLaboratorio1.setIdInternal(5);
//		
//		shiftLaboratorio1.setTipo(ShiftType.LABORATORIAL);
//		
//		lesson = new Lesson();
//		lesson.setIdInternal(5);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 8, 0);
//		
//		lesson.setInicio(beginLessonDate);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 10, 0);
//		
//		lesson.setFim(beginLessonDate);
//		
//		shiftLaboratorio1.addAssociatedLessons(lesson);
//		
//		/* Create Laboratorial Shift 2 */	
//		Shift shiftLaboratorio2 = new Shift();
//		shiftLaboratorio2.setIdInternal(6);
//		
//		shiftLaboratorio2.setTipo(ShiftType.LABORATORIAL);
//		
//		lesson = new Lesson();
//		lesson.setIdInternal(6);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 10, 0);
//		
//		lesson.setInicio(beginLessonDate);
//		
//		beginLessonDate = Calendar.getInstance();
//		
//		beginLessonDate.set(2005, 10, 31, 12, 0);
//		
//		lesson.setFim(beginLessonDate);
//		
//		shiftLaboratorio2.addAssociatedLessons(lesson);
//		
//		// Associate shifts to executionCourse
//		
//		executionCourse.addAssociatedShifts(shiftTeorica1);
//		executionCourse.addAssociatedShifts(shiftTeorica2);
//		executionCourse.addAssociatedShifts(shiftPratica1);
//		executionCourse.addAssociatedShifts(shiftPratica2);
//		executionCourse.addAssociatedShifts(shiftPratica3);
//		executionCourse.addAssociatedShifts(shiftLaboratorio1);
//		executionCourse.addAssociatedShifts(shiftLaboratorio2);
//		
//		// Create an ExecutionPeriod and associate with the ExecutionCourse
//		ExecutionPeriod executionPeriod = new ExecutionPeriod();
//		executionPeriod.setIdInternal(0);
//		executionCourse.setExecutionPeriod(executionPeriod);
//		executionPeriod.addAssociatedExecutionCourses(executionCourse);
//		
//		/* Create a Teacher and associate shifts with ProfessorShips and
//		 * ShiftProfessorship
//		 */
//		teacher = new Teacher();
//		teacher.setIdInternal(0);
//		
//		Professorship professorShip = new Professorship();
//		professorShip.setIdInternal(0);
//		professorShip.setTeacher(teacher);
//		
//		ShiftProfessorship shiftProfessorship = new ShiftProfessorship();
//		shiftProfessorship.setIdInternal(0);
//		shiftProfessorship.setProfessorship(professorShip);
//		shiftProfessorship.setShift(shiftTeorica1);
//		professorShip.addAssociatedShiftProfessorship(shiftProfessorship);
//		
//		shiftProfessorship = new ShiftProfessorship();
//		shiftProfessorship.setIdInternal(1);
//		shiftProfessorship.setProfessorship(professorShip);
//		shiftProfessorship.setShift(shiftPratica1);
//		professorShip.addAssociatedShiftProfessorship(shiftProfessorship);
//		
//		/* Add this professorShip to executionCourse */
//		executionCourse.addProfessorships(professorShip);
	}
	
	
	public void testHoursLecturedOnExecutionCourse() {
		
		/* Test if totalHours given by teacher is equal to 4.0 hours */
		assertEquals("Expected Total Hours given by teacher on executionCourse equal to 4.0", 4.0, StrictMath.ceil(teacher.getHoursLecturedOnExecutionCourse(executionCourse)) );
	}

}
