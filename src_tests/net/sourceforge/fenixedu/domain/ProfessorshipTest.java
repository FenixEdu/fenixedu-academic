/*
 * Created on Jul 25, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;


public class ProfessorshipTest extends DomainTestBase {

    ExecutionCourse executionCourse;
    Teacher teacher,teacher2;
    Professorship professorship, professorship2;
    Summary summary;
    ShiftProfessorship shiftProfessorship;
    SupportLesson supportLesson;
    
    protected void setUp() throws Exception {
        super.setUp();
        
        executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(0);
        
//        teacher = new Teacher();
        teacher.setIdInternal(0);
        teacher.getCategory().setCanBeExecutionCourseResponsible(true);
        
//        teacher2 = new Teacher();
        teacher2.setIdInternal(1);
        
        summary = new Summary();
        summary.setIdInternal(0);
        
        shiftProfessorship = new ShiftProfessorship();
        shiftProfessorship.setIdInternal(0); 
        
//        supportLesson = new SupportLesson();
        supportLesson.setIdInternal(0);        
    }

    public void testCreate() {
        
        // Test NullPointerException               
        try {
            Professorship.create(null, null, null, null);
            fail("Expected NullPointerException");
            
        } catch (NullPointerException e){
            testProfessorshipCreate(0, 0, 0, false, null);
                                   
        } catch (MaxResponsibleForExceed e1) {
            fail("Expected NullPointerException");
        } catch (InvalidCategory e1) {
            fail("Expected NullPointerException");
        }
        
        // Insert 1 Professorship Responsible
        try {
            professorship = Professorship.create(true, executionCourse, teacher, new Double(0.0));
                        
        } catch (MaxResponsibleForExceed e1) {
            fail("Unexpected MaxResponsibleForExceed Exception");
        } catch (InvalidCategory e1) {
            fail("Unexpected InvalidCategory Exception");
        }
        
        testProfessorshipCreate(0, 1, 1, true, professorship);
        
        // Insert 1 Professorship Responsible
        try {
            professorship2 = Professorship.create(true, executionCourse, teacher, new Double(0.0));
                        
        } catch (MaxResponsibleForExceed e1) {
            fail("Unexpected MaxResponsibleForExceed Exception");
        } catch (InvalidCategory e1) {
            fail("Unexpected InvalidCategory Exception");
        }
        
        testProfessorshipCreate(0, 2, 2, true, professorship2);

        // Insert 1 Professorship Responsible
        try {
            professorship2 = Professorship.create(true, executionCourse, teacher, new Double(0.0));
                        
        } catch (MaxResponsibleForExceed e1) {
            fail("Unexpected MaxResponsibleForExceed Exception");
        } catch (InvalidCategory e1) {
            fail("Unexpected InvalidCategory Exception");
        }
        
        testProfessorshipCreate(0, 3, 3, true, professorship2);
        
        // Try insert one more Professorship Responsible
        // Test MaxResponsibleForExceed Exception
        try {
            Professorship.create(true, executionCourse, teacher, new Double(0.0));
            fail("Expected MaxResponsibleForExceed");           
            
        } catch (MaxResponsibleForExceed e1) {            
            testProfessorshipCreate(0, 3, 3, false, null);
            
        } catch (InvalidCategory e1) {
            fail("Expected MaxResponsibleForExceed");
        }
              
        // Insert 1 Professorship not responsible         
        try {
            professorship2 = Professorship.create(false, executionCourse, teacher, new Double(0.0));
                        
        } catch (MaxResponsibleForExceed e1) {
            fail("Unexpected MaxResponsibleForExceed Exception");
        } catch (InvalidCategory e1) {
            fail("Unexpected InvalidCategory Exception");
        }
        
        testProfessorshipCreate(0, 3, 4, true, professorship2);
        
        // Test InvalidCategory Exception 
        teacher.getCategory().setCanBeExecutionCourseResponsible(false);        
        try {
            Professorship.create(true, executionCourse, teacher, new Double(0.0));
            fail("Expected InvalidCategory");
            
        } catch (MaxResponsibleForExceed e) {
            fail("Expected InvalidCategory");
        } catch (InvalidCategory e) {
            testProfessorshipCreate(0, 3, 4, false, null);
        }
    }

    public void testDelete() {
        try {
            professorship = Professorship.create(true, executionCourse, teacher, new Double(0.0));
                        
        } catch (MaxResponsibleForExceed e1) {
            fail("Unexpected MaxResponsibleForExceed Exception");
        } catch (InvalidCategory e1) {
            fail("Unexpected InvalidCategory Exception");
        }
        
        professorship.addAssociatedSummaries(summary);
        professorship.addAssociatedShiftProfessorship(shiftProfessorship);
        professorship.addSupportLessons(supportLesson);
        
        try{
            professorship.delete();
            fail("Expected DomainException");
        
        }catch(DomainException e){
            testProfessorshipDelete(0, 1, professorship);
        }
        
        professorship.removeAssociatedShiftProfessorship(shiftProfessorship);
        
        try{
            professorship.delete();
            fail("Expected DomainException");
            
        }catch(DomainException e){
            testProfessorshipDelete(0, 1, professorship);
        }
        
        professorship.removeSupportLessons(supportLesson);
        
        try{
            professorship.delete();
            fail("Expected DomainException");
            
        }catch(DomainException e){
            testProfessorshipDelete(0, 1, professorship);
        }
        
        professorship.removeAssociatedSummaries(summary);
        
        try{
            professorship.delete();
        
        }catch(DomainException e){
            fail("Unexpected DomainException");
        }
    }

    private void testProfessorshipCreate(int value1, int value2, int value3, boolean first, Professorship professorship){
        if(first){
            assertEquals("Diferent Professorship", professorship, executionCourse.getProfessorships().get(value1));
            assertEquals("Professorship Unexpected", executionCourse.getProfessorships().get(value1), teacher.getProfessorships().get(value1));
        }
        
        assertEquals("Size Unexpected", value2, executionCourse.responsibleFors().size());
        assertEquals("Size Unexpected", value2, teacher.responsibleFors().size());
       
        assertEquals("Size Unexpected", value3, executionCourse.getProfessorshipsCount());
        assertEquals("Size Unexpected", value3, teacher.getProfessorshipsCount());
    }
    
    private void testProfessorshipDelete(int value1, int value2, Professorship professorship){
        
        assertEquals("Diferent Professorship", professorship, executionCourse.getProfessorships().get(value1));
        assertEquals("Professorship Unexpected", executionCourse.getProfessorships().get(value1), teacher.getProfessorships().get(value1));
        
        assertEquals("Size Unexpected", value2, executionCourse.getProfessorshipsCount());
        assertEquals("Size Unexpected", value2, teacher.getProfessorshipsCount());        
    }
}
