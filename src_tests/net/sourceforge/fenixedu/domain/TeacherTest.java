package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.util.PublicationArea;

public class TeacherTest extends DomainTestBase {

    private ITeacher teacher;
    private IPublication publication;
    private IPublication publication2;
    private IPublication publication3;
    private IPublication publication4;
    private IPublication publication5;
    private IPublication publication6;
    private IPublication publication7;
    
    private IPublication publication8;
    private IPublication publication9;
    private ITeacher teacher2;
    
    protected void setUp() throws Exception {
        super.setUp();
        teacher = new Teacher();
        teacher.setIdInternal(1);
        publication = new Publication();
        publication.setIdInternal(1); 
        publication2 = new Publication();
        publication2.setIdInternal(2);
        publication3 = new Publication();
        publication3.setIdInternal(3);
        publication4 = new Publication();
        publication4.setIdInternal(4);
        publication5 = new Publication();
        publication5.setIdInternal(5);
        publication6 = new Publication();
        publication6.setIdInternal(6);
        publication7 = new Publication();
        publication7.setIdInternal(7);
        
        teacher2 = new Teacher();
        teacher2.setIdInternal(2);
        publication8 = new Publication();
        publication8.setIdInternal(8);
        publication9 = new Publication();
        publication9.setIdInternal(9);
        new PublicationTeacher(publication8, teacher2, PublicationArea.CIENTIFIC).setIdInternal(1);
        new PublicationTeacher(publication9, teacher2, PublicationArea.DIDATIC).setIdInternal(2);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testAddPublicationToTeacherSheet() {
        List<IPublication> publications;
        
        assertEquals(teacher.getTeacherPublicationsCount(), 0);

        
        try {
            teacher.addToTeacherSheet(publication,PublicationArea.CIENTIFIC);
        } catch (DomainException de) {
            fail("Should have inserted the publication cleanly, but threw a DomainException");
        }
        assertEquals(teacher.getTeacherPublicationsCount(), 1);
        publications = new ArrayList();
        for(IPublicationTeacher publicationTeacher : teacher.getTeacherPublications()) {
            publications.add(publicationTeacher.getPublication());
        }
        assertTrue(publications.contains(publication));
        assertEquals(publication.getPublicationTeachersCount(), 1);
        assertTrue(publication.getPublicationTeachers().get(0).getPublicationArea().equals(PublicationArea.CIENTIFIC));
        
        
        try {
            teacher.addToTeacherSheet(publication2, PublicationArea.DIDATIC);
        } catch (DomainException de) {
            fail("Should have inserted the publication cleanly, but threw a DomainException");
        }
        assertEquals(teacher.getTeacherPublicationsCount(), 2);
        publications = new ArrayList();
        for(IPublicationTeacher publicationTeacher : teacher.getTeacherPublications()) {
            publications.add(publicationTeacher.getPublication());
        }
        assertTrue(publications.contains(publication));
        assertTrue(publications.contains(publication2));
        assertEquals(publication.getPublicationTeachersCount(), 1);
        assertEquals(publication2.getPublicationTeachersCount(), 1);
        assertTrue(publication2.getPublicationTeachers().get(0).getPublicationArea().equals(PublicationArea.DIDATIC));
        
        
        try {
            teacher.addToTeacherSheet(publication3, PublicationArea.DIDATIC);
        } catch (DomainException de) {
            fail("Should have inserted the publication cleanly, but threw a DomainException");
        }
        assertEquals(teacher.getTeacherPublicationsCount(), 3);
        publications = new ArrayList();
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
            teacher.addToTeacherSheet(publication4, PublicationArea.DIDATIC);
        } catch (DomainException de) {
            fail("Should have inserted the publication cleanly, but threw a DomainException");
        }
        assertEquals(teacher.getTeacherPublicationsCount(), 4);
        publications = new ArrayList();
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
            teacher.addToTeacherSheet(publication5, PublicationArea.DIDATIC);
        } catch (DomainException de) {
            fail("Should have inserted the publication cleanly, but threw a DomainException");
        }
        assertEquals(teacher.getTeacherPublicationsCount(), 5);
        publications = new ArrayList();
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
            teacher.addToTeacherSheet(publication6, PublicationArea.DIDATIC);
        } catch (DomainException de) {
            fail("Should have inserted the publication cleanly, but threw a DomainException");
        }
        assertEquals(teacher.getTeacherPublicationsCount(), 6);
        publications = new ArrayList();
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
            teacher.addToTeacherSheet(publication7, PublicationArea.DIDATIC);
            fail("Added sixth publication to teacher sheet! Should have thrown a DomainException.");
        } catch (DomainException de) {
            //clean run, enforced expected limit
        }
        
    }
    
    public void testRemovePublicationFromTeacherSheet() {
        
        List<IPublication> publications;
        
        assertEquals(teacher2.getTeacherPublicationsCount(), 2);
        assertEquals(publication8.getPublicationTeachersCount(), 1);
        assertEquals(publication9.getPublicationTeachersCount(), 1);
        
        try {
            teacher2.removeFromTeacherSheet(publication8);
        } catch (DomainException e) {
            fail("Could not remove an associated publication");
        }
        
        publications = new ArrayList();
        for(IPublicationTeacher publicationTeacher : teacher2.getTeacherPublications()) {
            publications.add(publicationTeacher.getPublication());
        }
        assertEquals(teacher2.getTeacherPublicationsCount(), 1);
        assertFalse(publications.contains(publication8));
        assertTrue(publications.contains(publication9));
        assertEquals(publication8.getPublicationTeachersCount(), 0);
        
        try {
            teacher2.removeFromTeacherSheet(publication9);
        } catch (DomainException e) {
            fail("Could not remove an associated publication");
        }
        
        publications = new ArrayList();
        for(IPublicationTeacher publicationTeacher : teacher2.getTeacherPublications()) {
            publications.add(publicationTeacher.getPublication());
        }
        assertEquals(teacher2.getTeacherPublicationsCount(), 0);
        assertFalse(publications.contains(publication8));
        assertFalse(publications.contains(publication9));
        assertEquals(publication8.getPublicationTeachersCount(), 0);
        assertEquals(publication9.getPublicationTeachersCount(), 0);
        
        try {
            teacher2.removeFromTeacherSheet(publication9);
            fail("Accepted to remove a non-associated publication");
        } catch (DomainException e) {
            // clean expected exception
        }
    }

}
