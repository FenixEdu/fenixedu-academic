/*
 * Created on Jul 18, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;


public class CurriculumTest extends DomainTestBase {

    ICurriculum curriculum;
    IPerson person, person2;
        
    protected void setUp() throws Exception {
        super.setUp();
               
        person = new Person();
        person.setIdInternal(0);
        person.setNome("Mrsp");
        
        person2 = new Person();
        person2.setIdInternal(1);
        person2.setNome("Jdnf");
       
        curriculum = new Curriculum();
        curriculum.setIdInternal(0);
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
        
        curriculum.edit("generalObjectives2", "operacionalObjectives2", "program2", "", "", "", null, person);
        
        assertEquals("General Objectives Unexpected", "generalObjectives2", curriculum.getGeneralObjectives());
        assertEquals("General Objectives Eng Unexpected", "generalObjectivesEn", curriculum.getGeneralObjectivesEn());
        
        assertEquals("General Objectives Unexpected", "operacionalObjectives2", curriculum.getOperacionalObjectives());
        assertEquals("General Objectives Eng Unexpected", "operacionalObjectivesEn", curriculum.getOperacionalObjectivesEn());
        
        assertEquals("Program Unexpected", "program2", curriculum.getProgram());
        assertEquals("Program Eng Unexpected", "programEn", curriculum.getProgramEn());
        
        assertEquals("Person Name Unexpected", person.getNome(), curriculum.getPersonWhoAltered().getNome());
        
        curriculum.edit("", "", "", "generalObjectivesEn2", "operacionalObjectivesEn2", "programEn2", "Eng", person2);
        
        assertEquals("General Objectives Unexpected", "generalObjectives2", curriculum.getGeneralObjectives());
        assertEquals("General Objectives Eng Unexpected", "generalObjectivesEn2", curriculum.getGeneralObjectivesEn());
        
        assertEquals("General Objectives Unexpected", "operacionalObjectives2", curriculum.getOperacionalObjectives());
        assertEquals("General Objectives Eng Unexpected", "operacionalObjectivesEn2", curriculum.getOperacionalObjectivesEn());
        
        assertEquals("Program Unexpected", "program2", curriculum.getProgram());
        assertEquals("Program Eng Unexpected", "programEn2", curriculum.getProgramEn());
        
        assertEquals("Person Name Unexpected", person2.getNome(), curriculum.getPersonWhoAltered().getNome());
    }
}
