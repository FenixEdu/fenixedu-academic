/*
 * Created on Jul 18, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;


public class CurriculumTest extends DomainTestBase {

    Curriculum curriculum;
    Person person, person2;
    CurricularCourse curricularCourse;
        
    protected void setUp() throws Exception {
        super.setUp();
               
        person = new Person();
        person.setIdInternal(0);
        person.setNome("Mrsp");
        
        person2 = new Person();
        person2.setIdInternal(1);
        person2.setNome("Jdnf");
       
        curricularCourse = new CurricularCourse();
        curricularCourse.setIdInternal(0);
                
        curriculum = new Curriculum();
        curriculum.setIdInternal(0);
        curriculum.setCurricularCourse(curricularCourse);
        
        curriculum.setGeneralObjectives("generalObjectives");
        curriculum.setGeneralObjectivesEn("generalObjectivesEn");
        curriculum.setOperacionalObjectives("operacionalObjectives");
        curriculum.setOperacionalObjectivesEn("operacionalObjectivesEn");
        curriculum.setProgram("program");
        curriculum.setProgramEn("programEn");
        curriculum.setPersonWhoAltered(person2);
        
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testEdit() {
        
        curriculum.edit("generalObjectives2", "operacionalObjectives2", "program2", "", "", "");
        
        testGeneralObjectives("generalObjectives2", "generalObjectivesEn");            
        testOperacionalObjectives("operacionalObjectives2", "operacionalObjectivesEn");      
        testProgram("program2", "programEn"); 
        
        assertEquals("Person Name Unexpected", person.getNome(), curriculum.getPersonWhoAltered().getNome());
        
        curriculum.edit("", "", "", "generalObjectivesEn2", "operacionalObjectivesEn2", "programEn2");
                      
        testGeneralObjectives("generalObjectives2", "generalObjectivesEn2");
        testOperacionalObjectives("operacionalObjectives2", "operacionalObjectivesEn2");
        testProgram("program2", "programEn2");
        
        assertEquals("Person Name Unexpected", person2.getNome(), curriculum.getPersonWhoAltered().getNome());
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
