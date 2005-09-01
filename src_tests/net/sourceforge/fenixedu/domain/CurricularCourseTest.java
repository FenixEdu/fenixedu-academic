/*
 * Created on Jul 19, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;


public class CurricularCourseTest extends DomainTestBase {

    ICurricularCourse curricularCourse;
    ICurriculum curriculum;
	
	private ICurricularCourse curricularCourseToReadFrom = null;
	private List executionCoursesToBeRead = null;
	private IExecutionPeriod executionPeriod = null;
        
    protected void setUp() throws Exception {
        super.setUp();
        
        curricularCourse = new CurricularCourse();
		
		setUpGetExecutionCoursesByExecutionPeriod();
    }

    private void setUpGetExecutionCoursesByExecutionPeriod() {
		curricularCourseToReadFrom = new CurricularCourse();
		executionPeriod = new ExecutionPeriod();
		executionCoursesToBeRead = new ArrayList();
		
		IExecutionPeriod otherExecutionPeriod = new ExecutionPeriod(); 
		
		IExecutionCourse ec1 = new ExecutionCourse();
		ec1.setExecutionPeriod(executionPeriod);
		curricularCourseToReadFrom.addAssociatedExecutionCourses(ec1);
		executionCoursesToBeRead.add(ec1);
		
		IExecutionCourse ec2 = new ExecutionCourse();
		ec2.setExecutionPeriod(executionPeriod);
		curricularCourseToReadFrom.addAssociatedExecutionCourses(ec2);
		executionCoursesToBeRead.add(ec2);
		
		IExecutionCourse ec3 = new ExecutionCourse();
		ec3.setExecutionPeriod(otherExecutionPeriod);
		curricularCourseToReadFrom.addAssociatedExecutionCourses(ec3);
		
		IExecutionCourse ec4 = new ExecutionCourse();
		ec4.setExecutionPeriod(otherExecutionPeriod);
		curricularCourseToReadFrom.addAssociatedExecutionCourses(ec4);
		
	}

	protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInsertCurriculum() {
        
        curriculum = curricularCourse.insertCurriculum("program", "programEn", "opObjectives", "opObjectivesEn", "genObjectives", "genObjectivesEn");
                
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
