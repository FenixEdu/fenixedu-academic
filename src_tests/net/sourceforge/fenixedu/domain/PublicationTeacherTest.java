package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.publication.Authorship;
import net.sourceforge.fenixedu.domain.publication.IAuthorship;
import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.util.PublicationArea;

public class PublicationTeacherTest extends DomainTestBase {

    ITeacher teacher1;
    
    ITeacher teacher2;
    
    IPerson person1;
    
    IAuthorship authorship1;
    
    List<ITeacher> teachers;
    
    IPublication publication;
    
    IPublicationTeacher publicationTeacher1;
    
    String area;
    
    
    protected void setUp() throws Exception {
        super.setUp();
        
        person1 = new Person();
        
        teacher1 = new Teacher();
        teacher1.setPerson(person1);    
        
        teacher2 = new Teacher();
        
        teachers = new ArrayList<ITeacher>();
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
            IPublicationTeacher publicationTeacher = new PublicationTeacher(publication, teacher1, PublicationArea.DIDATIC);
            
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
            IPublicationTeacher publicationTeacher = new PublicationTeacher(publication, teacher2, PublicationArea.CIENTIFIC);
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
