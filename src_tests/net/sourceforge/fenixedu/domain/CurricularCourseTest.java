/*
 * Created on Jul 19, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;


public class CurricularCourseTest extends DomainTestBase {

    ICurricularCourse curricularCourse;
    ICurriculum curriculum;
        
    protected void setUp() throws Exception {
        super.setUp();
        
        curricularCourse = new CurricularCourse();
        curricularCourse.setIdInternal(0);              
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInsertCurriculum() {
        
        curriculum = curricularCourse.insertCurriculum("program", "programEn", "opObjectives", "opObjectivesEn", "genObjectives", "genObjectivesEn");
        
        assertEquals("Program Unexpected", "program" ,curriculum.getProgram());
        assertEquals("Program Eng Unexpected", "programEn" ,curriculum.getProgramEn());
        
        assertEquals("General Objectives Unexpected", "genObjectives" ,curriculum.getGeneralObjectives());
        assertEquals("General Objectives Eng Unexpected", "genObjectivesEn" ,curriculum.getGeneralObjectivesEn());
        
        assertEquals("Operacional Objectives Unexpected", "opObjectives" ,curriculum.getOperacionalObjectives());
        assertEquals("Operacional Objectives Eng Unexpected", "opObjectivesEn" ,curriculum.getOperacionalObjectivesEn());
        
        assertEquals("Size Unexpected", 1, curricularCourse.getAssociatedCurriculumsCount());        
        assertEquals("Curriculum Unexpected", curriculum, curricularCourse.getAssociatedCurriculums(0));
    }
}
