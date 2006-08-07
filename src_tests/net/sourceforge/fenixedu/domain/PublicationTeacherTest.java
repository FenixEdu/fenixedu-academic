package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.publication.Authorship;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.util.PublicationArea;

public class PublicationTeacherTest extends DomainTestBase {

    Teacher teacher1;
    
    Teacher teacher2;
    
    Person person1;
    
    Authorship authorship1;
    
    List<Teacher> teachers;
    
    Publication publication;
    
    PublicationTeacher publicationTeacher1;
    
    String area;
    
    
    protected void setUp() throws Exception {
        super.setUp();
        
        person1 = new Person();
        
//        teacher1 = new Teacher();
        teacher1.setPerson(person1);    
        
//        teacher2 = new Teacher();
        
        teachers = new ArrayList<Teacher>();
        teachers.add(teacher1);
        
        publication = new Publication();
        
        publicationTeacher1 = new PublicationTeacher();
        publicationTeacher1.setTeacher(teacher1);
        publicationTeacher1.setPublication(publication);
        publicationTeacher1.setPublicationArea(PublicationArea.CIENTIFIC);
        
        authorship1 = new Authorship(publication, person1, 1);
    }

    public void testCreatePublicationTeacher() {
        try {
            PublicationTeacher publicationTeacher = new PublicationTeacher(publication, teacher1, PublicationArea.DIDATIC);
            
            assertEquals("Publication Expected", publicationTeacher.getPublication(), publication);
            assertEquals("Teacher Expected", publicationTeacher.getTeacher(), teacher1);
            assertEquals("PublicationTeacher's Area Unexpected", PublicationArea.DIDATIC, publicationTeacher.getPublicationArea());
            
            assertEquals("PublicationTeachers size unexpected", 2, publication.getPublicationTeachersCount());
            assertEquals("Authorships size unexpected", 1, publication.getPublicationAuthorshipsCount());
        } catch (DomainException domainException) {
            fail("The teacher should be allowed to associate himself with the publication");
        }
        
    }
    
    public void testCreatePublicationTeacherNotAuthor() {
        try {
            new PublicationTeacher(publication, teacher2, PublicationArea.CIENTIFIC);
            fail("The teacher is not an author of the publication, therefore he souldn't be able to associate the publication");
        } catch (DomainException domainException) {
            //the normal course of events took place, since we were trying to associate a teacher with a publication for wich he
            //was not an author
        }
        
    }

    public void testDeletePublicationTeacher() {
        
        assertEquals("Unexpected PublicationTeachers Size", publication.getPublicationTeachersCount(), 1);
        assertTrue("Publication should be connected to teacher", publication.getPublicationTeachers().contains(publicationTeacher1));
        
        publicationTeacher1.delete();
        
        assertEquals("PublicationTeachers Size Unexpected", 0, publication.getPublicationTeachersCount());
        assertEquals("Unexpected PublicationTeachers Size", teacher1.getTeacherPublicationsCount(), 0);
        
        assertEquals("Unexpected PublicationTeachers Size", publication.getPublicationTeachersCount(), 0);
        
        assertFalse("Publication shouldn't be connected to teacher", publication.getPublicationTeachers().contains(publicationTeacher1));
        
    }

}
