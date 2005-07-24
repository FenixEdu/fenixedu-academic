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
	
	String workLocationName1 = "workLocationName1";
	String workLocationName2 = "workLocationName2";
	
	String newName = "newnome";
	String newAddress = "newaddress";
	String newPhone = "newphone";
	String newMobile = "newmobile";
	String newHomepage = "newhomepage";
	String newEmail = "newemail";
	
	IWorkLocation workLocation1;
	IWorkLocation workLocation2;
	List<IExternalPerson> allExternalPersons;

    protected void setUp() throws Exception {
        super.setUp();
        workLocation1 = new WorkLocation();
        workLocation1.setName(workLocationName1);
        workLocation2 = new WorkLocation();
        workLocation2.setName(workLocationName2);
        
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
        externalPerson.setWorkLocation(workLocation2);
        allExternalPersons.add(externalPerson);
        
    }

    public void testCreateExternalPerson() {
    	
    	IExternalPerson externalPerson = new ExternalPerson(name, gender, address, phone, mobile, homepage, email, documentIDNumber, workLocation1);
    	
    	//The Person content is not tested here because a whole test in PersonTest is dedicated to this purpouse
    	assertNotNull(externalPerson.getPerson());
    	assertNotNull(externalPerson.getWorkLocation());
    }
    
 
    public void testEditExternalPerson() {
    	IExternalPerson externalPerson = new ExternalPerson(name, gender, address, phone, mobile, homepage, email, documentIDNumber, workLocation1);

    	externalPerson.edit("novoNome", "novoAddress", "novoPhone", "novoMobile", "novoHomepage", "novoEmail", workLocation2, allExternalPersons);
    	
    	assertEquals(externalPerson.getWorkLocation(), workLocation2);
    	        
    }

    public void testEditExternalPersonAlreadyExists() {
    	IExternalPerson externalPerson = new ExternalPerson(name, gender, address, phone, mobile, homepage, email, documentIDNumber, workLocation1);
    	
    	try {
    		externalPerson.edit(newName, newAddress, newPhone, newMobile, newHomepage, newEmail, workLocation2, allExternalPersons);
    		fail("The external person shouldn't have been sucessfully changed since an external person already exists with that data");
    	} catch (DomainException domainException) {
    		//This exception was intended and expected since we were trying to change an external person to one that already existed
    	}
    	
    	        
    }    
    
}
 