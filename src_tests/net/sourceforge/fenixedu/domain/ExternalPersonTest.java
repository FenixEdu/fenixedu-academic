package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;

public class ExternalPersonTest extends DomainTestBase {

	String name = "nome";
	Gender gender = Gender.MALE;
	String address = "address";
	String phone = "phone";
	String mobile = "mobile";
	String homepage = "homepage";
	String email = "email";
	String documentIDNumber = "documentIDNumber";
	IDDocumentType documentType = IDDocumentType.EXTERNAL;
	
	String institutionName1 = "institutionName1";
	String institutionName2 = "institutionName2";
	
	String newName = "newnome";
	String newAddress = "newaddress";
	String newPhone = "newphone";
	String newMobile = "newmobile";
	String newHomepage = "newhomepage";
	String newEmail = "newemail";
	
	IInstitution institution1;
	IInstitution institution2;
	List<IExternalPerson> allExternalPersons;

    protected void setUp() throws Exception {
        super.setUp();
        institution1 = new Institution();
        institution1.setName(institutionName1);
        institution2 = new Institution();
        institution2.setName(institutionName2);
        
        allExternalPersons = new ArrayList<IExternalPerson>();
        IExternalPerson externalPerson = new ExternalPerson();
        IPerson person = new Person ();
        person.setNome(newName);
        person.setMorada(newAddress);
        person.setTelefone(newPhone);
        person.setTelemovel(newMobile);
        person.setEnderecoWeb(newHomepage);
        person.setEmail(newEmail);
        externalPerson.setPerson(person);
        externalPerson.setinstitution(institution2);
        allExternalPersons.add(externalPerson);
        
    }

    public void testCreateExternalPerson() {
    	
    	IExternalPerson externalPerson = new ExternalPerson(name, gender, address, phone, mobile, homepage, email, documentIDNumber, institution1);
    	
    	//The Person content is not tested here because a whole test in PersonTest is dedicated to this purpouse
    	assertNotNull(externalPerson.getPerson());
    	assertNotNull(externalPerson.getInstitution());
    }
    
 
    public void testEditExternalPerson() {
    	IExternalPerson externalPerson = new ExternalPerson(name, gender, address, phone, mobile, homepage, email, documentIDNumber, institution1);

    	externalPerson.edit("novoNome", "novoAddress", "novoPhone", "novoMobile", "novoHomepage", "novoEmail", institution2, allExternalPersons);
    	
    	assertEquals(externalPerson.getInstitution(), institution2);
    	        
    }

    public void testEditExternalPersonAlreadyExists() {
    	IExternalPerson externalPerson = new ExternalPerson(name, gender, address, phone, mobile, homepage, email, documentIDNumber, institution1);
    	
    	try {
    		externalPerson.edit(newName, newAddress, newPhone, newMobile, newHomepage, newEmail, institution2, allExternalPersons);
    		fail("The external person shouldn't have been sucessfully changed since an external person already exists with that data");
    	} catch (DomainException domainException) {
    		//This exception was intended and expected since we were trying to change an external person to one that already existed
    	}
    	
    	        
    }    
    
}
 