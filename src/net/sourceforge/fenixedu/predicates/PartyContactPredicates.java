package net.sourceforge.fenixedu.predicates;

import net.sourceforge.fenixedu.domain.Employee;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.student.Student;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.AccessControlPredicate;

public class PartyContactPredicates {

    public static final AccessControlPredicate<PartyContact> checkPermissionsToManage = new AccessControlPredicate<PartyContact>() {
	public boolean evaluate(final PartyContact contact) {
	    
	    final Person loggedPerson = AccessControl.getUserView().getPerson();
	    
	    if (isPersonalContactInformation(contact, loggedPerson)) {
		return true;
	    }
	    
	    if (loggedPerson.hasRole(RoleType.MANAGER)) {
		return true;
	    }

	    if (loggedPerson.hasRole(RoleType.PARKING_MANAGER)) {
		return true;
	    }

	    if (loggedPerson.hasRole(RoleType.ACADEMIC_ADMINISTRATIVE_OFFICE)) {
		final Party party = contact.getParty();
		
		if (party.isPerson()) {
		    final Person personContact = (Person) party;
		    
		    if (personContact.hasEmployee()) {
			return false;
		    }
		    
		    if (personContact.hasStudent()) {
			return studentBelongsToLoggedEmployeAdministrativeOffice(personContact.getStudent(), loggedPerson.getEmployee());
		    }
		}
		return true;
	    }
	    
	    return false;
	}

	private boolean isPersonalContactInformation(final PartyContact contact, final Person loggedPerson) {
	    return loggedPerson == contact.getParty();
	}
	
	private boolean studentBelongsToLoggedEmployeAdministrativeOffice(final Student student, final Employee employee) {
	    return student.hasRegistrationForOffice(employee.getAdministrativeOffice());
	}
    };
}
