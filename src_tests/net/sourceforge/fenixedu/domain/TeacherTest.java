/*
 * Created on Jul 7, 2005
 *	by mrsp and jdnf
 */
package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.InvalidCategory;
import net.sourceforge.fenixedu.applicationTier.Servico.teacher.professorship.ResponsibleForValidator.MaxResponsibleForExceed;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.domain.teacher.Category;
import net.sourceforge.fenixedu.domain.teacher.ICategory;
import net.sourceforge.fenixedu.persistenceTier.ExcepcaoPersistencia;
import net.sourceforge.fenixedu.util.PublicationArea;

public class TeacherTest extends DomainTestBase {

    private ITeacher teacher, teacher2, teacher3;
    private IExecutionCourse executionCourse, executionCourse2, executionCourse3;
    private IProfessorship professorship, professorship2, professorship3, professorship4, professorship5, professorship6;
    private IExecutionYear  executionYear;
    private IExecutionPeriod executionPeriod;
    private ICategory category;   
    private IPublication publication;
    private IPublication publication2;
    private IPublication publication3;
    private IPublication publication4;
    private IPublication publication5;
    private IPublication publication6;
    private IPublication publication7;
    private IPublication publication8;
    private IPublication publication9;
        
    
    protected void setUp() throws Exception {
        super.setUp();
    
        setupTestUpdateResponsabilitiesFor();
                
        teacher = new Teacher();
        
        publication = new Publication();
        publication2 = new Publication();
        publication3 = new Publication();
        publication4 = new Publication();
        publication5 = new Publication();
        publication6 = new Publication();
        publication7 = new Publication();
        
        teacher2 = new Teacher();
        
        publication8 = new Publication();
        publication9 = new Publication();
        
        new PublicationTeacher(publication8, teacher2, PublicationArea.CIENTIFIC).setIdInternal(1);
        new PublicationTeacher(publication9, teacher2, PublicationArea.DIDATIC).setIdInternal(2);
    }

    public void testUpdateResponsabilitiesFor() {
               
        try{
            teacher3.updateResponsabilitiesFor(null, null);
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e){
            testResponsibleForValue(true, false, true);
        }
        catch (MaxResponsibleForExceed e) {
            fail("Expected NullPointerException");
        } 
        catch (InvalidCategory e) {
            fail("Expected NullPointerException");
        } 
        catch (ExcepcaoPersistencia e) {
            fail("Expected NullPointerException");
        }
        
        List newResponsabilities = new ArrayList();
        newResponsabilities.add(1);
        
        ///////////////////////////
        
        teacher3.getCategory().setCanBeExecutionCourseResponsible(true);        
        
        try {
            teacher3.updateResponsabilitiesFor(executionYear.getIdInternal(), newResponsabilities);
            fail("Expected MaxResponsibleForExceed");
            
        } catch (MaxResponsibleForExceed e) {
            testResponsibleForValue(false, false, true); 
            
            // O primeiro argumento é false porque nos testes n existe mecanismo transacional logo se ocorrer um excepção 
            // todas as alterações efectuadas ateriormente não serão repostas.
        
        } catch (InvalidCategory e) {
            fail("Expected MaxResponsibleForExceed");
       
        } catch (ExcepcaoPersistencia e) {
            fail("Expected MaxResponsibleForExceed");
        }
        
        //////////////////////////
        
        teacher3.getCategory().setCanBeExecutionCourseResponsible(false);
        
        try {
            teacher3.updateResponsabilitiesFor(executionYear.getIdInternal(), newResponsabilities);
            fail("Expected InvalidCategory");
            
        } catch (MaxResponsibleForExceed e) {
            fail("Expected InvalidCategory");            
        
        } catch (InvalidCategory e) {
            testResponsibleForValue(false, false, true);
       
        } catch (ExcepcaoPersistencia e) {
            fail("Expected InvalidCategory");
        }
        
        /////////////////////////
        
        professorship5.setResponsibleFor(false);
        teacher3.getCategory().setCanBeExecutionCourseResponsible(true);
        
        try {
            teacher3.updateResponsabilitiesFor(executionYear.getIdInternal(), newResponsabilities);
            
        } catch (MaxResponsibleForExceed e) {
            testResponsibleForValue(false, false, true);            
        
        } catch (InvalidCategory e) {
            testResponsibleForValue(false, false, true);
       
        } catch (ExcepcaoPersistencia e) {
            testResponsibleForValue(false, false, true);
        }
        
        testResponsibleForValue(false, true, false);    
    } 

    public void testAddPublicationToTeacherInformationSheet() {
        List<IPublication> publications;
        
        assertEquals(teacher.getTeacherPublicationsCount(), 0);

        
        try {
            teacher.addToTeacherInformationSheet(publication,PublicationArea.CIENTIFIC);
        } catch (DomainException de) {
            fail("Should have inserted the publication cleanly, but threw a DomainException");
        }
        assertEquals(teacher.getTeacherPublicationsCount(), 1);
        publications = new ArrayList<IPublication>();
        for(IPublicationTeacher publicationTeacher : teacher.getTeacherPublications()) {
            publications.add(publicationTeacher.getPublication());
        }
        assertTrue(publications.contains(publication));
        assertEquals(publication.getPublicationTeachersCount(), 1);
        assertTrue(publication.getPublicationTeachers().get(0).getPublicationArea().equals(PublicationArea.CIENTIFIC));
        
        
        try {
            teacher.addToTeacherInformationSheet(publication2, PublicationArea.DIDATIC);
        } catch (DomainException de) {
            fail("Should have inserted the publication cleanly, but threw a DomainException");
        }
        assertEquals(teacher.getTeacherPublicationsCount(), 2);
        publications = new ArrayList<IPublication>();
        for(IPublicationTeacher publicationTeacher : teacher.getTeacherPublications()) {
            publications.add(publicationTeacher.getPublication());
        }
        assertTrue(publications.contains(publication));
        assertTrue(publications.contains(publication2));
        assertEquals(publication.getPublicationTeachersCount(), 1);
        assertEquals(publication2.getPublicationTeachersCount(), 1);
        assertTrue(publication2.getPublicationTeachers().get(0).getPublicationArea().equals(PublicationArea.DIDATIC));
        
        
        try {
            teacher.addToTeacherInformationSheet(publication3, PublicationArea.DIDATIC);
        } catch (DomainException de) {
            fail("Should have inserted the publication cleanly, but threw a DomainException");
        }
        assertEquals(teacher.getTeacherPublicationsCount(), 3);
        publications = new ArrayList<IPublication>();
        for(IPublicationTeacher publicationTeacher : teacher.getTeacherPublications()) {
            publications.add(publicationTeacher.getPublication());
        }
        assertTrue(publications.contains(publication));
        assertTrue(publications.contains(publication2));
        assertTrue(publications.contains(publication3));
        assertEquals(publication.getPublicationTeachersCount(), 1);
        assertEquals(publication2.getPublicationTeachersCount(), 1);
        assertEquals(publication3.getPublicationTeachersCount(), 1);
        assertTrue(publication3.getPublicationTeachers().get(0).getPublicationArea().equals(PublicationArea.DIDATIC));
        
        
        try {
            teacher.addToTeacherInformationSheet(publication4, PublicationArea.DIDATIC);
        } catch (DomainException de) {
            fail("Should have inserted the publication cleanly, but threw a DomainException");
        }
        assertEquals(teacher.getTeacherPublicationsCount(), 4);
        publications = new ArrayList<IPublication>();
        for(IPublicationTeacher publicationTeacher : teacher.getTeacherPublications()) {
            publications.add(publicationTeacher.getPublication());
        }
        assertTrue(publications.contains(publication));
        assertTrue(publications.contains(publication2));
        assertTrue(publications.contains(publication3));
        assertTrue(publications.contains(publication4));
        assertEquals(publication.getPublicationTeachersCount(), 1);
        assertEquals(publication2.getPublicationTeachersCount(), 1);
        assertEquals(publication3.getPublicationTeachersCount(), 1);
        assertEquals(publication4.getPublicationTeachersCount(), 1);
        assertTrue(publication4.getPublicationTeachers().get(0).getPublicationArea().equals(PublicationArea.DIDATIC));
        
        
        try {
            teacher.addToTeacherInformationSheet(publication5, PublicationArea.DIDATIC);
        } catch (DomainException de) {
            fail("Should have inserted the publication cleanly, but threw a DomainException");
        }
        assertEquals(teacher.getTeacherPublicationsCount(), 5);
        publications = new ArrayList<IPublication>();
        for(IPublicationTeacher publicationTeacher : teacher.getTeacherPublications()) {
            publications.add(publicationTeacher.getPublication());
        }
        assertTrue(publications.contains(publication));
        assertTrue(publications.contains(publication2));
        assertTrue(publications.contains(publication3));
        assertTrue(publications.contains(publication4));
        assertTrue(publications.contains(publication5));
        assertEquals(publication.getPublicationTeachersCount(), 1);
        assertEquals(publication2.getPublicationTeachersCount(), 1);
        assertEquals(publication3.getPublicationTeachersCount(), 1);
        assertEquals(publication4.getPublicationTeachersCount(), 1);
        assertEquals(publication5.getPublicationTeachersCount(), 1);
        assertTrue(publication5.getPublicationTeachers().get(0).getPublicationArea().equals(PublicationArea.DIDATIC));
        
        
        try {
            teacher.addToTeacherInformationSheet(publication6, PublicationArea.DIDATIC);
        } catch (DomainException de) {
            fail("Should have inserted the publication cleanly, but threw a DomainException");
        }
        assertEquals(teacher.getTeacherPublicationsCount(), 6);
        publications = new ArrayList<IPublication>();
        for(IPublicationTeacher publicationTeacher : teacher.getTeacherPublications()) {
            publications.add(publicationTeacher.getPublication());
        }
        assertTrue(publications.contains(publication));
        assertTrue(publications.contains(publication2));
        assertTrue(publications.contains(publication3));
        assertTrue(publications.contains(publication4));
        assertTrue(publications.contains(publication5));
        assertTrue(publications.contains(publication6));
        assertEquals(publication.getPublicationTeachersCount(), 1);
        assertEquals(publication2.getPublicationTeachersCount(), 1);
        assertEquals(publication3.getPublicationTeachersCount(), 1);
        assertEquals(publication4.getPublicationTeachersCount(), 1);
        assertEquals(publication5.getPublicationTeachersCount(), 1);
        assertEquals(publication6.getPublicationTeachersCount(), 1);
        assertTrue(publication6.getPublicationTeachers().get(0).getPublicationArea().equals(PublicationArea.DIDATIC));
        
        
        try {
            teacher.addToTeacherInformationSheet(publication7, PublicationArea.DIDATIC);
            fail("Added sixth publication to teacher sheet! Should have thrown a DomainException.");
        } catch (DomainException de) {
            //clean run, enforced expected limit
        }
        
    }
    
    public void testRemovePublicationFromTeacherInformationSheet() {
        
        List<IPublication> publications;
        
        assertEquals(teacher2.getTeacherPublicationsCount(), 2);
        assertEquals(publication8.getPublicationTeachersCount(), 1);
        assertEquals(publication9.getPublicationTeachersCount(), 1);
        
        try {
            teacher2.removeFromTeacherInformationSheet(publication8);
        } catch (DomainException e) {
            fail("Could not remove an associated publication");
        }
        
        publications = new ArrayList<IPublication>();
        for(IPublicationTeacher publicationTeacher : teacher2.getTeacherPublications()) {
            publications.add(publicationTeacher.getPublication());
        }
        assertEquals(teacher2.getTeacherPublicationsCount(), 1);
        assertFalse(publications.contains(publication8));
        assertTrue(publications.contains(publication9));
        assertEquals(publication8.getPublicationTeachersCount(), 0);
        
        try {
            teacher2.removeFromTeacherInformationSheet(publication9);
        } catch (DomainException e) {
            fail("Could not remove an associated publication");
        }
        
        publications = new ArrayList<IPublication>();
        for(IPublicationTeacher publicationTeacher : teacher2.getTeacherPublications()) {
            publications.add(publicationTeacher.getPublication());
        }
        assertEquals(teacher2.getTeacherPublicationsCount(), 0);
        assertFalse(publications.contains(publication8));
        assertFalse(publications.contains(publication9));
        assertEquals(publication8.getPublicationTeachersCount(), 0);
        assertEquals(publication9.getPublicationTeachersCount(), 0);
        
        try {
            teacher2.removeFromTeacherInformationSheet(publication9);
            fail("Accepted to remove a non-associated publication");
        } catch (DomainException e) {
            // clean expected exception
        }
    }
    
    private void testResponsibleForValue(boolean value1, boolean value2, boolean value3){
        assertEquals("Responsible For Unexpected", value1, teacher3.getProfessorships(0).getResponsibleFor().booleanValue());
        assertEquals("Responsible For Unexpected", value2, teacher3.getProfessorships(1).getResponsibleFor().booleanValue());
        assertEquals("Responsible For Unexpected", value3, teacher3.getProfessorships(2).getResponsibleFor().booleanValue());
    }
    
    private void setupTestUpdateResponsabilitiesFor(){
        category = new Category();
        category.setIdInternal(0);
        category.setCanBeExecutionCourseResponsible(true);
        
        executionPeriod = new ExecutionPeriod();
        executionPeriod.setIdInternal(0);
        
        executionYear = new ExecutionYear();
        executionYear.setIdInternal(0);
        
        executionYear.addExecutionPeriods(executionPeriod);
        
        teacher3 = new Teacher();
        teacher3.setIdInternal(0);
        teacher3.setCategory(category);      
        
        executionCourse = new ExecutionCourse();
        executionCourse.setIdInternal(0);
        executionCourse.setExecutionPeriod(executionPeriod);
        
        executionCourse2 = new ExecutionCourse();
        executionCourse2.setIdInternal(1);
        executionCourse2.setExecutionPeriod(executionPeriod);
        
        executionCourse3 = new ExecutionCourse();
        executionCourse3.setIdInternal(2);
        executionCourse3.setExecutionPeriod(executionPeriod);
        
        professorship = new Professorship();
        professorship.setIdInternal(0);
        
        professorship2 = new Professorship();
        professorship2.setIdInternal(1);
        
        professorship3 = new Professorship();
        professorship3.setIdInternal(2);
        
        professorship4 = new Professorship();
        professorship4.setIdInternal(3);
        
        professorship5 = new Professorship();
        professorship5.setIdInternal(4);
        
        professorship6 = new Professorship();
        professorship6.setIdInternal(4);
        
        /////////////////////////////
                
        teacher3.addProfessorships(professorship);
        teacher3.addProfessorships(professorship2);
        teacher3.addProfessorships(professorship3);
        
        executionCourse.addProfessorships(professorship);
               
        executionCourse2.addProfessorships(professorship2);
        executionCourse2.addProfessorships(professorship4);
        executionCourse2.addProfessorships(professorship5);
        executionCourse2.addProfessorships(professorship6);
        
        executionCourse3.addProfessorships(professorship3);
        
        professorship.setResponsibleFor(true);
        professorship2.setResponsibleFor(false);
        professorship3.setResponsibleFor(true);
        
        professorship4.setResponsibleFor(true);
        professorship5.setResponsibleFor(true);
        professorship6.setResponsibleFor(true);        
    }
}
