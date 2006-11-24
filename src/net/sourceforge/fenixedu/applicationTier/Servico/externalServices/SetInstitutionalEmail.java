package net.sourceforge.fenixedu.applicationTier.Servico.externalServices;

import net.sourceforge.fenixedu.applicationTier.Service;
import net.sourceforge.fenixedu.domain.Person;

import org.apache.commons.lang.StringUtils;

public class SetInstitutionalEmail extends Service {

    public String run(String userUID, String email) {
	
	Person person = Person.readPersonByIstUsername(userUID);
	if (person == null) {
	    return "Inexistent Person with userUID = " + userUID;
	}

	String oldInstitutionalEmail = person.getInstitutionalEmail();
	final String newInstitutionalEmail = !StringUtils.isEmpty(email) ? email : null;
	person.setInstitutionalEmail(newInstitutionalEmail);

	return "Associated instituional email with success!!! Changed from " + oldInstitutionalEmail + " to " + newInstitutionalEmail;
    }
}
