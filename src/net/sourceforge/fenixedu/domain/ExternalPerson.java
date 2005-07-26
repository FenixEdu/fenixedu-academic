package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.person.Gender;
import net.sourceforge.fenixedu.domain.person.IDDocumentType;



public class ExternalPerson extends ExternalPerson_Base {

	public ExternalPerson() {
	}
	
    /********************************************************************
     *                        BUSINESS SERVICES                         *
     ********************************************************************/
	
	public ExternalPerson (String name, Gender gender, String address, String phone,
            String mobile, String homepage, String email, String documentIdNumber, IWorkLocation workLocation) {

		String username = "e" + documentIdNumber;
		
		IPerson person = new Person(username, name, gender, address, phone, mobile, homepage, email, documentIdNumber, IDDocumentType.EXTERNAL);
		setPerson(person);
		setWorkLocation(workLocation);
	}
	
	public void edit (String name, String address, String phone, String mobile, String homepage, String email, IWorkLocation workLocation, List<IExternalPerson> allExternalPersons) {
		if (!externalPersonsAlreadyExists(name, address, workLocation, allExternalPersons)) {
			this.getPerson().edit(name, address, phone, mobile, homepage, email);
			this.setWorkLocation(workLocation);
		} else {
			throw new DomainException("error.exception.externalPerson.existingExternalPerson");
		}
	}
	
    /********************************************************************
     *                         PRIVATE METHODS                          *
     ********************************************************************/
	
	private boolean externalPersonsAlreadyExists(String name, String address, IWorkLocation workLocation, List<IExternalPerson> allExternalPersons) {
		for (IExternalPerson externalPerson : allExternalPersons) {
			if (externalPerson.getPerson().getNome().equals(name) &&
				externalPerson.getPerson().getMorada().equals(address) &&
				externalPerson.getWorkLocation().equals(workLocation) )
				
				return true;
		}
		return false;
	}
	
    /********************************************************************
     *                          OTHER METHODS                           *
     ********************************************************************/
	
}
