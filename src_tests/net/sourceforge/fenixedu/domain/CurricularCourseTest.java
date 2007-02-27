/*
 * Created on Jul 19, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;

import org.joda.time.DateTime;


public class CurricularCourseTest extends DomainTestBase {

    CurricularCourse curricularCourse;
    Curriculum curriculum;
	
	private CurricularCourse curricularCourseToReadFrom = null;
	private List executionCoursesToBeRead = null;
	private ExecutionPeriod executionPeriod = null;
    
    /* ALterações jpmsi e lmam */
    private ExecutionPeriod executionPeriodToUse1 = null;
    private ExecutionPeriod executionPeriodToUse2 = null;
    private ExecutionYear executionYear = null;
    private CurricularCourse curricularCourse2 = null;
    
        
    protected void setUp() throws Exception {
        super.setUp();
        
        curricularCourse = new CurricularCourse();
		
		setUpGetExecutionCoursesByExecutionPeriod();
        setUpGetActiveEnrollmentEvaluations();
    }

    private void setUpGetExecutionCoursesByExecutionPeriod() {
		curricularCourseToReadFrom = new CurricularCourse();
		executionPeriod = new ExecutionPeriod();
		executionCoursesToBeRead = new ArrayList();
		
		ExecutionPeriod otherExecutionPeriod = new ExecutionPeriod(); 
		
		ExecutionCourse ec1 = new ExecutionCourse("name", "acronym", executionPeriod);
		curricularCourseToReadFrom.addAssociatedExecutionCourses(ec1);
		executionCoursesToBeRead.add(ec1);
		
		ExecutionCourse ec2 = new ExecutionCourse("name", "acronym", executionPeriod);
		curricularCourseToReadFrom.addAssociatedExecutionCourses(ec2);
		executionCoursesToBeRead.add(ec2);
		
		ExecutionCourse ec3 = new ExecutionCourse("name", "acronym", otherExecutionPeriod);
		curricularCourseToReadFrom.addAssociatedExecutionCourses(ec3);
		
		ExecutionCourse ec4 = new ExecutionCourse("name", "acronym", otherExecutionPeriod);
		curricularCourseToReadFrom.addAssociatedExecutionCourses(ec4);
		
	}
    
    private void setUpGetActiveEnrollmentEvaluations() {
        /* Setup some execution periods */
        executionPeriodToUse1 = new ExecutionPeriod();
        executionPeriodToUse2 = new ExecutionPeriod();
        
        /* Setup a execution year */
        executionYear = new ExecutionYear();
        executionPeriodToUse1.setExecutionYear(executionYear);
        executionPeriodToUse2.setExecutionYear(executionYear);

        /* Setup enrollments */
        Enrolment annuledEnrollment = new Enrolment();
        annuledEnrollment.setEnrollmentState(EnrollmentState.ANNULED);

        Enrolment numericApprovedEnrollment = new Enrolment();
        numericApprovedEnrollment.setEnrollmentState(EnrollmentState.APROVED);
        
        Enrolment apApprovedEnrollment = new Enrolment();
        apApprovedEnrollment.setEnrollmentState(EnrollmentState.APROVED);

        Enrolment reprovedEnrollment = new Enrolment();
        reprovedEnrollment.setEnrollmentState(EnrollmentState.NOT_APROVED);
        
        Enrolment notEvaluatedEnrollment = new Enrolment();
        notEvaluatedEnrollment.setEnrollmentState(EnrollmentState.NOT_EVALUATED);

        Enrolment otherExecutionPeriodEnrollment = new Enrolment();
        otherExecutionPeriodEnrollment.setEnrollmentState(EnrollmentState.APROVED);
        
        /* Setup enrollment evaluations */
        EnrolmentEvaluation numericFirstEnrolmentEvaluation = new EnrolmentEvaluation();
        numericFirstEnrolmentEvaluation.setGrade("10");
        
        EnrolmentEvaluation numericSecondEnrolmentEvaluation = new EnrolmentEvaluation();
        numericSecondEnrolmentEvaluation.setGrade("11");
        
        numericFirstEnrolmentEvaluation.setEnrolment(numericApprovedEnrollment);
        numericSecondEnrolmentEvaluation.setEnrolment(numericApprovedEnrollment);
        
        EnrolmentEvaluation apFirstEnrolmentEvaluation = new EnrolmentEvaluation();
        apFirstEnrolmentEvaluation.setGrade(GradeScale.AP);
        
        apFirstEnrolmentEvaluation.setEnrolment(apApprovedEnrollment);
        
        EnrolmentEvaluation reprovedEnrollmentEvaluation = new EnrolmentEvaluation();
        reprovedEnrollmentEvaluation.setGrade(GradeScale.RE);
        
        reprovedEnrollmentEvaluation.setEnrolment(reprovedEnrollment);
        
        EnrolmentEvaluation notEvaluatedEnrollmentEvaluation = new EnrolmentEvaluation();
        notEvaluatedEnrollmentEvaluation.setGrade(GradeScale.NA);
        
        notEvaluatedEnrollmentEvaluation.setEnrolment(notEvaluatedEnrollment);
        
        EnrolmentEvaluation otherExecutionPeriodEnrollmentEvaluation = new EnrolmentEvaluation();
        otherExecutionPeriodEnrollmentEvaluation.setGrade("12");
        
        otherExecutionPeriodEnrollmentEvaluation.setEnrolment(otherExecutionPeriodEnrollment);
        
        /* Setup enrollments execution periods */
        annuledEnrollment.setExecutionPeriod(executionPeriodToUse1);
        numericApprovedEnrollment.setExecutionPeriod(executionPeriodToUse1);
        apApprovedEnrollment.setExecutionPeriod(executionPeriodToUse2);
        reprovedEnrollment.setExecutionPeriod(executionPeriodToUse1);
        notEvaluatedEnrollment.setExecutionPeriod(executionPeriodToUse1);
        
        ExecutionPeriod someExecutionPeriod = new ExecutionPeriod();
        someExecutionPeriod.setExecutionYear(new ExecutionYear());
        otherExecutionPeriodEnrollment.setExecutionPeriod(someExecutionPeriod);
        
        /* Setup curricular course */
        curricularCourse2 = new CurricularCourse();
        
        annuledEnrollment.setCurricularCourse(curricularCourse2);
        numericApprovedEnrollment.setCurricularCourse(curricularCourse2);
        apApprovedEnrollment.setCurricularCourse(curricularCourse2);
        reprovedEnrollment.setCurricularCourse(curricularCourse2);
        notEvaluatedEnrollment.setCurricularCourse(curricularCourse2);
        otherExecutionPeriodEnrollment.setCurricularCourse(curricularCourse2);
    }

	protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInsertCurriculum() {
        
        curriculum = curricularCourse.insertCurriculum("program", "programEn", "opObjectives", "opObjectivesEn", "genObjectives", "genObjectivesEn", new DateTime());
                
        testProgram("program", "programEn");
        testGeneralObjectives("genObjectives", "genObjectivesEn");
        testOperacionalObjectives("opObjectives", "opObjectivesEn");
        
        assertEquals("Size Unexpected", 1, curricularCourse.getAssociatedCurriculumsCount());        
        assertEquals("Curriculum Unexpected", curriculum, curricularCourse.getAssociatedCurriculums().get(0));
    }
	
	public void testGetExecutionCoursesByExecutionPeriod() {
		List executionCourses = curricularCourseToReadFrom.getExecutionCoursesByExecutionPeriod(executionPeriod);
		
		assertTrue(executionCourses.containsAll(executionCoursesToBeRead));
	}
    
    public void testGetActiveEnrollmentEvaluations() {
//        assertEquals("Active Enrollment Evaluations Count", 6, curricularCourse2.getActiveEnrollmentEvaluations().size());
    }
    
    public void testGetActiveEnrollmentEvaluationsByExecutionYear() {
//        assertEquals("Active Enrollment Evaluations By Execution Year Count", 5, curricularCourse2.getActiveEnrollmentEvaluations(executionYear).size());
    }
    
    public void testGetActiveEnrollmentEvaluationsByExecutionPeriod() {
//        assertEquals("Active Enrollment Evaluations By Execution OccupationPeriod Count", 4, curricularCourse2.getActiveEnrollmentEvaluations(executionPeriodToUse1).size());
//        assertEquals("Active Enrollment Evaluations By Execution OccupationPeriod Count", 1, curricularCourse2.getActiveEnrollmentEvaluations(executionPeriodToUse2).size());
    }
    
	private void testGeneralObjectives(String generalObjectives, String generalObjectivesEng){
        
        assertEquals("General Objectives Unexpected", generalObjectives, curriculum.getGeneralObjectives());
        assertEquals("General Objectives Eng Unexpected", generalObjectivesEng, curriculum.getGeneralObjectivesEn());
        
        assertEquals("General Objectives Unexpected", generalObjectives, curricularCourse.getAssociatedCurriculums().get(0).getGeneralObjectives());
        assertEquals("General Objectives Eng Unexpected", generalObjectivesEng, curricularCourse.getAssociatedCurriculums().get(0).getGeneralObjectivesEn());       
    }
    
    private void testOperacionalObjectives(String operacionalObjectives, String operacionalObjectivesEng){
       
        assertEquals("Operacional Objectives Unexpected", operacionalObjectives, curriculum.getOperacionalObjectives());
        assertEquals("Operacional Objectives Eng Unexpected", operacionalObjectivesEng, curriculum.getOperacionalObjectivesEn());
        
        assertEquals("Operacional Objectives Unexpected", operacionalObjectives, curricularCourse.getAssociatedCurriculums().get(0).getOperacionalObjectives());
        assertEquals("Operacional Objectives Eng Unexpected", operacionalObjectivesEng, curricularCourse.getAssociatedCurriculums().get(0).getOperacionalObjectivesEn());
    }
    
    private void testProgram(String program, String programEng){
        
        assertEquals("Program Unexpected", program, curriculum.getProgram());
        assertEquals("Program Eng Unexpected", programEng, curriculum.getProgramEn());
        
        assertEquals("Program Unexpected", program, curricularCourse.getAssociatedCurriculums().get(0).getProgram());
        assertEquals("Program Eng Unexpected", programEng, curricularCourse.getAssociatedCurriculums().get(0).getProgramEn());
    }
}
