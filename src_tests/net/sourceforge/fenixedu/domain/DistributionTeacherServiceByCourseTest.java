/*
 * Created by amak, jpmsit
 */
package net.sourceforge.fenixedu.domain;

import java.util.Calendar;


public class DistributionTeacherServiceByCourseTest extends DomainTestBase {

    private ExecutionCourse executionCourse;
    

    protected void setUp() throws Exception {
        super.setUp();

        executionCourse = new ExecutionCourse("name", "acronym", ExecutionPeriod.readActualExecutionPeriod());
        executionCourse.setIdInternal(0);
        
        ExecutionPeriod executionPeriod = new ExecutionPeriod();
        ExecutionPeriod otherExecutionPeriod = new ExecutionPeriod();
       
        Enrolment enrolmentStudent1 = new Enrolment();
        enrolmentStudent1.setExecutionPeriod(executionPeriod);
        Enrolment enrolmentStudent2 = new Enrolment();   
        enrolmentStudent2.setExecutionPeriod(executionPeriod);
        Enrolment enrolmentStudent3 = new Enrolment();   
        enrolmentStudent3.setExecutionPeriod(otherExecutionPeriod);
        
        StudentCurricularPlan studentCurricularPlan1 = new StudentCurricularPlan();
        studentCurricularPlan1.addEnrolments(enrolmentStudent1);
        StudentCurricularPlan studentCurricularPlan2 = new StudentCurricularPlan();
        studentCurricularPlan2.addEnrolments(enrolmentStudent2);
        studentCurricularPlan2.addEnrolments(enrolmentStudent3);
        
        CurricularCourse curricularCourse = new CurricularCourse();
//        curricularCourse.addEnrolments(enrolmentStudent1);
//        curricularCourse.addEnrolments(enrolmentStudent2);
//        curricularCourse.addEnrolments(enrolmentStudent3);
        
        executionCourse.addAssociatedCurricularCourses(curricularCourse);
        executionCourse.setExecutionPeriod(executionPeriod);
        
/* Create Theoretical Shift 1 */
		
//		Shift shiftTeorica1 = new Shift();
//		shiftTeorica1.setIdInternal(0);
//		
//		shiftTeorica1.setTipo(ShiftType.TEORICA);
//		
//		Lesson lesson = new Lesson();
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
//				
//		shiftTeorica2.setTipo(ShiftType.TEORICA);
//		
//		lesson = new Lesson();
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
//		
//		
//		shiftPratica1.setTipo(ShiftType.PRATICA);
//		
//		lesson = new Lesson();
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
//		
//		
//		shiftPratica2.setTipo(ShiftType.PRATICA);
//		
//		lesson = new Lesson();
//		
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
//		
//		
//		shiftPratica3.setTipo(ShiftType.PRATICA);
//		
//		lesson = new Lesson();
//		
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
//
//		
//		shiftLaboratorio1.setTipo(ShiftType.LABORATORIAL);
//		
//		lesson = new Lesson();
//		
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
//		
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
//        
    }

    
    public void testTotalEnrolmentStudentNumber() {
   
    	assertEquals("Number of Total Enrolments different from Expected!", 2, executionCourse.getTotalEnrolmentStudentNumber().intValue());
    }
    
    
    public void testFirstTimeEnrolmentStudentNumber(){
    	
    	assertEquals("Number of First Time Students different from Expected!", 1, executionCourse.getFirstTimeEnrolmentStudentNumber().intValue());
    }
    
    
    public void testTotalHours(){	
    	/* Test if the total hours of pratical shifts is 6.0 */
    	assertEquals("Expected Total Hours of Pratical Shifts equal to 6", 6.0, executionCourse.getTotalHours(ShiftType.PRATICA));
		 
		 /* Test if the total hours of theoretical shifts is 3.0 */
    	assertEquals("Expected Total Hours of Theoretical Shifts equal to 3", 3.0, executionCourse.getTotalHours(ShiftType.TEORICA));
		 
		 /* Test if the total hours of pratical shifts is 4.0 */
    	assertEquals("Expected Total Hours of Laboratorial Shifts equal to 4", 4.0, executionCourse.getTotalHours(ShiftType.LABORATORIAL));
    }
    
    
    public void testStudentsNumberByShift(){
		 /* Test if the total number of students per theoretical shifts is 1 */
    	assertEquals("Expected Total Students per Theoretical Shifts equal to 1", 1.0, executionCourse.getStudentsNumberByShift(ShiftType.TEORICA) );
				 
		 /* Test if the total number of students per pratical shifts is 2/3 */
		 assertEquals("Expected Total Students per Pratical Shifts equal to 2/3", (2d / 3), executionCourse.getStudentsNumberByShift(ShiftType.PRATICA) );
				 
		 /* Test if the total number of students per laboratorial shifts is 1 */
		 assertEquals("Expected Total Students per Laboratorial Shifts equal to 2", 1.0, executionCourse.getStudentsNumberByShift(ShiftType.LABORATORIAL) ); 
    }
    
 
}
