package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.publication.IPublication;
import net.sourceforge.fenixedu.domain.publication.IPublicationTeacher;
import net.sourceforge.fenixedu.domain.publication.Publication;
import net.sourceforge.fenixedu.domain.publication.PublicationTeacher;
import net.sourceforge.fenixedu.util.PublicationArea;

public class PublicationTeacherTest extends DomainTestBase {

    ITeacher teacher1;
    
    List<ITeacher> teachers;
    
    IPublication publication;
    
    IPublicationTeacher publicationTeacher1;
    
    String area;
    
    
    protected void setUp() throws Exception {
        super.setUp();
        
        teacher1 = new Teacher();
        
        teachers = new ArrayList<ITeacher>();
        teachers.add(teacher1);
        
        publication = new Publication();
        
        publicationTeacher1 = new PublicationTeacher();
        publicationTeacher1.setTeacher(teacher1);
        publicationTeacher1.setPublication(publication);
        publicationTeacher1.setPublicationArea(PublicationArea.CIENTIFIC);
    }

    public void testCreatePublicationTeacher() {
        IPublicationTeacher publicationTeacher = new PublicationTeacher(publication, teacher1, PublicationArea.DIDATIC);
        
        assertEquals("Publication Expected", publicationTeacher.getPublication(), publication);
        assertEquals("Teacher Expected", publicationTeacher.getTeacher(), teacher1);
        assertEquals("PublicationTeacher's Area Unexpected", PublicationArea.DIDATIC, publicationTeacher.getPublicationArea());
        
        assertEquals("PublicationTeachers size unexpected", 2, publication.getPublicationTeachersCount());
        assertEquals("Authorships size unexpected", 0, publication.getPublicationAuthorshipsCount());
        
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
