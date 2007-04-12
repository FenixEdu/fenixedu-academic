package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.publication.Authorship;
import net.sourceforge.fenixedu.domain.publication.Publication;

public class AuthorshipTest extends DomainTestBase {

    Person person1;
    Person person2;
    Person person3;
    Person person4;
    Person person5;
    
    List<Person> authors;
    
    Publication publication;
    
    Authorship authorship1;
    Authorship authorship2;
    Authorship authorship3;
    Authorship authorship4;
    
    
    Integer order;
    
    
    protected void setUp() throws Exception {
        super.setUp();
        
        person1 = new Person();
        person1.setName("Autor1");
        
        person2 = new Person();
        person2.setName("Autor2");
        
        person3 = new Person();
        person3.setName("Autor3");
        
        person4 = new Person();
        person4.setName("Autor4");
        
        person5 = new Person();
        person5.setName("Autor5");
        
        authors = new ArrayList<Person>();
        authors.add(person1);
        
        publication = new Publication();
        
        authorship1 = new Authorship();
        authorship1.setAuthor(person1);
        authorship1.setPublication(publication);
        authorship1.setAuthorOrder(1);
        
        authorship2 = new Authorship();
        authorship2.setAuthor(person2);
        authorship2.setPublication(publication);
        authorship2.setAuthorOrder(2);
        
        authorship3 = new Authorship();
        authorship3.setAuthor(person3);
        authorship3.setPublication(publication);
        authorship3.setAuthorOrder(3);
        
        authorship4 = new Authorship();
        authorship4.setAuthor(person4);
        authorship4.setPublication(publication);
        authorship4.setAuthorOrder(4);
        
    }

    public void testCreateAuthorship() {
    	try {
	        Authorship authorship = new Authorship(publication, person5, 5);
	        
	        assertEquals("Publication Expected", (Publication)authorship.getPublication(), publication);
	        assertEquals("Person Expected", authorship.getAuthor(), person5);
	        assertEquals("Authorship's Order Unexpected", new Integer(5), authorship.getAuthorOrder());
	        
	        assertEquals("Authorships size unexpected", 5, publication.getPublicationAuthorshipsCount());
	        assertEquals("Teachers size unexpected", 0, publication.getPublicationTeachersCount());
    	} catch (DomainException domainException) {
    		fail("The authorship should have been sucessfully created");
    	}
    }
    
    public void testCreateAuthorshipWithExistingOrder() {
    	try {
    		new Authorship(publication, person2, 1);
    		fail("The authorship shouldn't have been sucessfully deleted");
    	} catch (DomainException domainException) {
    		//Caso em que se tenta criar uma autoria para uma determinada publicação/pessoa com uma ordem já existente
    	}
    	
    }

    public void testDeleteAuthorship() {
        assertEquals("Unexpected Authorships Size", 4, publication.getPublicationAuthorshipsCount());
        assertTrue("Publication doen't contain the authorship being deleted", publication.getPublicationAuthorships().contains(authorship2));
        
        authorship2.delete();
        
        assertEquals("Authorships Size Unexpected", 3, publication.getPublicationAuthorshipsCount());
        assertEquals("PublicationTeachers Size Unexpected", 0, publication.getPublicationTeachersCount());
        assertEquals("Unexpected Authorships Size", 1, person1.getPersonAuthorshipsCount());
        assertEquals("Unexpected Authorships Size", 0, person2.getPersonAuthorshipsCount());
        assertEquals("Unexpected Authorships Size", 1, person3.getPersonAuthorshipsCount());
        assertEquals("Unexpected Authorships Size", 1, person4.getPersonAuthorshipsCount());
        
        //Verify if the remaining authorships order was updated
        //Note this rule enforcement is being done at the relation - PublicationAuthorship.remove
        assertEquals("Unexpected Order for Authorship1", new Integer(1), authorship1.getAuthorOrder());
        assertEquals("Unexpected Order for Authorship3", new Integer(2), authorship3.getAuthorOrder());
        assertEquals("Unexpected Order for Authorship4", new Integer(3), authorship4.getAuthorOrder());
        
        assertFalse("Publication still contains the authorship", publication.getPublicationAuthorships().contains(authorship2));
    }
    
}
